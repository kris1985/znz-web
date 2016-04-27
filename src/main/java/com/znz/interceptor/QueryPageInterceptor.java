package com.znz.interceptor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.RowBounds;

import com.znz.vo.PageParameter;

/**
 * user:pengzz
 * Time: 2014/5/27 17:18.
 */
@Slf4j
@Intercepts({ @Signature(method = "prepare", type = StatementHandler.class, args = { Connection.class }) })
public class QueryPageInterceptor implements Interceptor {
    private static String       defaultPageSqlId = ".*Page$"; // 需要拦截的ID(正则匹配)
    private final static String DEFAULTDATABASE  = "mysql"; // 需要拦截的ID(正则匹配)
    private String              sqlmapId         = "";       // 需要拦截的ID(正则匹配)
    private String              databaseType;                //默认是oracle

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
        StatementHandler delegate = (StatementHandler) getFieldValue(handler, "delegate");
        MappedStatement mappedStatement = (MappedStatement) getFieldValue(delegate,
            "mappedStatement");

        // 只重写需要分页的sql语句。通过MappedStatement的ID匹配，默认重写以Page结尾的MappedStatement的sql
        if (mappedStatement.getId().matches(sqlmapId)) {
            BoundSql boundSql = delegate.getBoundSql();
            Object parameterObject = boundSql.getParameterObject();
            if (parameterObject == null) {
                throw new NullPointerException("parameterObject is null!");
            } else {
                PageParameter page = (PageParameter) PropertyUtils.getProperty(parameterObject,
                    "page");
                String sql = boundSql.getSql();
                // 重设分页参数里的总页数等
                Connection connection = (Connection) invocation.getArgs()[0];
                setPageParameter(sql, connection, mappedStatement, boundSql, page);

                // 重写sql
                String pageSql = buildPageSql(sql, page);
                setFieldValue(boundSql, "sql", pageSql);
                // 采用物理分页后，就不需要mybatis的内存分页了
                RowBounds rowBounds = (RowBounds) getFieldValue(delegate, "rowBounds");
                setFieldValue(rowBounds, "offset", 0);
            }
        }
        // 将执行权交给下一个拦截器
        return invocation.proceed();
    }

    /**
     * 从数据库里查询总的记录数并计算总页数，回写进分页参数<code>PageParameter</code>,这样调用者就可用通过 分页参数
     * <code>PageParameter</code>获得相关信息。
     *
     * @param sql
     * @param connection
     * @param mappedStatement
     * @param boundSql
     * @param page
     */
    private void setPageParameter(String sql, Connection connection,
                                  MappedStatement mappedStatement, BoundSql boundSql,
                                  PageParameter page) {
        // 记录总记录数
        String reg = "(?i)order\\s*(?i)BY(\\s*.*)*";
        String countSql = "select count(0)  as total from (" + sql.replaceAll(reg, "") + ") as count_total";
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try {
            countStmt = connection.prepareStatement(countSql);
            BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,
                boundSql.getParameterMappings(), boundSql.getParameterObject());
            //给countBs中的参数metaParameters赋值；解决因物理分页，setParameters时，metaObject.getValue(propertyName)报的空指针异常的问题
            Object metaParamsField = getFieldValue(boundSql, "metaParameters");
            if (metaParamsField != null) {
                MetaObject mo = (MetaObject) getFieldValue(boundSql, "metaParameters");
                setFieldValue(countBS, "metaParameters", mo);
            }
            setParameters(countStmt, mappedStatement, countBS, boundSql.getParameterObject());
            rs = countStmt.executeQuery();
            int totalCount = 0;
            if (rs.next()) {
                totalCount = rs.getInt(1);
            }
            page.setTotalCount(totalCount);
            //            long totalPage = totalCount / page.getPageSize() + ((totalCount % page.getPageSize() == 0) ? 0 : 1);
            //page.setTotalPage(totalPage);

        } catch (SQLException e) {
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (countStmt != null) {
                    countStmt.close();
                }
            } catch (SQLException e) {

            }

        }

    }

    /**
     * 对SQL参数(?)设值
     *
     * @param ps
     * @param mappedStatement
     * @param boundSql
     * @param parameterObject
     * @throws SQLException
     */
    private void setParameters(PreparedStatement ps, MappedStatement mappedStatement,
                               BoundSql boundSql, Object parameterObject) throws SQLException {
        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement,
            parameterObject, boundSql);
        parameterHandler.setParameters(ps);
    }

    /**
     * 根据数据库类型，生成特定的分页sql
     *
     * @param sql
     * @param page
     * @return
     */
    private String buildPageSql(String sql, PageParameter page) {
        if ("oracle".equals(databaseType)){
            StringBuilder pageSql = buildPageSqlForOracle(sql, page);
            return pageSql.toString();
        } else if("mysql".equals(databaseType)){
            StringBuilder pageSql = buildPageSqlForMySql(sql, page);
            return pageSql.toString();
        }else {
            throw new RuntimeException("目前只支持其它数据库");
        }
    }

    private StringBuilder buildPageSqlForMySql(String sql, PageParameter page) {
        StringBuilder pageSql = new StringBuilder();
        long beginrow = (page.getCurrentPage() - 1) * page.getPageSize();
        pageSql.append(sql);
        pageSql.append(" limit ").append(beginrow).append(",").append(page.getPageSize());
        return pageSql;
    }

    /**
     * 参考hibernate的实现完成oracle的分页
     *
     * @param sql
     * @param page
     * @return String
     */
    public StringBuilder buildPageSqlForOracle(String sql, PageParameter page) {
        StringBuilder pageSql = new StringBuilder(100);
        long beginrow = (page.getCurrentPage() - 1) * page.getPageSize();
        long endrow = page.getCurrentPage() * page.getPageSize();

        pageSql.append("select * from ( select row_.*, rownum rownum_ from ( ");
        pageSql.append(sql);
        pageSql.append(" ) row_ where rownum <= " + endrow + ") where rownum_ > " + beginrow);
        return pageSql;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        sqlmapId = properties.getProperty("sqlmapId");
        if (StringUtils.isEmpty(sqlmapId)) {
            throw new RuntimeException("sqlmapId property is not found!");
        }
        databaseType = properties.getProperty("databaseType");
        if (StringUtils.isEmpty(databaseType)) {
            databaseType = DEFAULTDATABASE;
        }
    }

    public static Object getFieldValue(Object obj, String fieldName) {
        Object result = null;
        Field field = getField(obj, fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                result = field.get(obj);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return result;
    }

    private static Field getField(Object obj, String fieldName) {
        Field field = null;
        for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                //这里不用做处理，子类没有该字段可能对应的父类有，都没有就返回null。
            }
        }
        return field;
    }

    public static void setFieldValue(Object obj, String fieldName, Object fieldValue) {
        Field field = getField(obj, fieldName);
        if (field != null) {
            try {
                field.setAccessible(true);
                field.set(obj, fieldValue);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}

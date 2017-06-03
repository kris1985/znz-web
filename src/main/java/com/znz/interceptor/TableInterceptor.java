/**
 * ykse.com.cn
 * Copyright (c) 1997-2015 All Rights Reserved.
 */
package com.znz.interceptor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Properties;

import com.znz.util.PartionCodeHoder;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

/**
 * 动态替换表名
 * @author huangtao
 * @version Id: TableInterceptor.java, v 0.1 2017/3/29 10:29 huagntao Exp $$
 */
//@Intercepts(@Signature(type=StatementHandler.class,method="prepare",args=Connection.class))
public class TableInterceptor implements Interceptor {

    public static final String SQLMAPID_REG = "cn.com.ykse.commons.push";

    public TableInterceptor() {
    }



    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler)invocation.getTarget();
        StatementHandler delegate = (StatementHandler) getFieldValue(statementHandler, "delegate");
        MappedStatement mappedStatement = (MappedStatement) getFieldValue(delegate,
            "mappedStatement");
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();
        String partionCode = PartionCodeHoder.get();
        if (mappedStatement.getId().matches(SQLMAPID_REG) && StringUtils.isNoneBlank(partionCode)) {
            sql = sql.replaceAll("t\\_picture","t_picture"+partionCode);
        }
        setFieldValue(boundSql,"sql",sql);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    public static Object getFieldValue(Object obj, String fieldName) {
        Object result = null;
        Field field = getField(obj, fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                result = field.get(obj);
            } catch (Exception e) {
               throw new RuntimeException(e);
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
                throw new RuntimeException(e);
            }
        }
    }

}
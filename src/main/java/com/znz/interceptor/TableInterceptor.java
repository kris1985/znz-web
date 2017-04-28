/**
 * ykse.com.cn
 * Copyright (c) 1997-2015 All Rights Reserved.
 */
package com.znz.interceptor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;

import static org.apache.ibatis.reflection.SystemMetaObject.DEFAULT_OBJECT_FACTORY;
import static org.apache.ibatis.reflection.SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY;

/**
 * 动态替换表名
 * @author huangtao
 * @version Id: TableInterceptor.java, v 0.1 2017/3/29 10:29 huagntao Exp $$
 */
@Intercepts(@Signature(type=StatementHandler.class,method="prepare",args=Connection.class))
public class TableInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler)invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(
            statementHandler ,
            DEFAULT_OBJECT_FACTORY ,
            DEFAULT_OBJECT_WRAPPER_FACTORY);

        BoundSql boundSql = statementHandler.getBoundSql();
        Map map = (Map) boundSql.getParameterObject();
        if(map!=null){
            Object o =  map.get("partionKey");
           if(o!=null){
              Integer partionCode =  (Integer)o;
           }
        }
        //String sql = boundSql.getSql().replace("t_","");
        //Object parameterObject = statementHandler.getParameterHandler().getParameterObject();
     //  metaObject.setValue("delegate.boundSql.sql",sql);
        Class<?> cls = boundSql.getClass();
        Field field = cls.getDeclaredField("sql");
        field.setAccessible(true);
     //   field.set(boundSql, sql);
        //   FieldUtils.writeField(boundSql, "sql",sql);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
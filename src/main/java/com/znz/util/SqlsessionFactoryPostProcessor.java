/**
 * ykse.com.cn
 * Copyright (c) 1997-2015 All Rights Reserved.
 */
package com.znz.util;

import com.znz.interceptor.TableInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author huangtao
 * @version Id: SqlsessionFactoryPostProcessor.java, v 0.1 2017/3/29 11:48 huagntao Exp $$
 */
@Component
//BeanFactoryPostProcessor
public class SqlsessionFactoryPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof SqlSessionFactory) {
            SqlSessionFactory sessionFactory = (SqlSessionFactory)bean;
          //  sessionFactory.getConfiguration().getMapperRegistry().addMappers("cn.com.ykse.commons.push.mappers");
           // sessionFactory.getConfiguration().addInterceptor(new TableInterceptor());
        }
        return bean;
    }
}
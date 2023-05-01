package com.bondarenko.bean.factory.destroy;

import com.bondarenko.bean.factory.annotation.PreDestroy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class DestroyBeanImpl implements DestroyBean {
    @Override
    public void close(Map<String, Object> beans) {
        for (Object bean : beans.values()) {
            for (Method method : bean.getClass().getMethods()) {
                if (method.isAnnotationPresent(PreDestroy.class)) {
                    try {
                        method.invoke(bean);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException("Can't access ", e);
                    }
                }
            }
        }
    }
}

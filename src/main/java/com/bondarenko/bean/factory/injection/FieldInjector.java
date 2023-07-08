package com.bondarenko.bean.factory.injection;

import com.bondarenko.bean.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class FieldInjector {

    public void inject(Map<String, Object> beans) {
        log.info("Start field inject");
        Map<Class<?>, List<Object>> beansByType = beans.values().stream()
                .collect(Collectors.groupingBy(Object::getClass));

        log.info("Iterate over fields annotated with @Autowired");
        beans.values().forEach(bean -> {
            Arrays.stream(getFieldsFromBean(bean))
                    .filter(field -> field.isAnnotationPresent(Autowired.class))
                    .forEach(field -> injectFieldDependenci(beansByType, bean, field));
        });
    }

    private void injectFieldDependenci(Map<Class<?>, List<Object>> beansByType, Object bean, Field field) {
        List<Object> dependencies = beansByType.get(field.getType());
        String setterName = createSetMethodName(field);
        try {
            Method set = bean.getClass().getMethod(setterName, field.getType());
            set.invoke(bean, dependencies.get(0));
        } catch (NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            log.error("Can't found method to inject setter");
            throw new RuntimeException("Can't found method", e);
        }
    }

    private String createSetMethodName(Field field) {
        return "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
    }

    private Field[] getFieldsFromBean(Object bean) {
        return bean.getClass().getDeclaredFields();
    }
}
package com.bondarenko.bean.factory;

import com.bondarenko.bean.factory.annotation.Autowired;
import com.bondarenko.bean.factory.annotation.PreDestroy;
import com.bondarenko.bean.factory.annotation.stereotype.*;
import com.bondarenko.bean.factory.config.BeanPostProcessor;
import com.bondarenko.bean.factory.util.ScanPackage;
import lombok.SneakyThrows;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class BeanFactory {

    private static Logger log = LogManager.getLogger(BeanFactory.class);
    private final Map<String, Object> beans = new HashMap<>();

    private List<BeanPostProcessor> postProcessors = new ArrayList<>();

    public void addPostProcessor(BeanPostProcessor postProcessor) {
        postProcessors.add(postProcessor);
    }


    public Object getBean(String beanName) {
        return beans.get(beanName);
    }

    public Object getBean(Class<?> bean) {
        return beans.get(bean);
    }

    @SneakyThrows
    public void init(String directory) {
        List<? extends String> classNames = ScanPackage.getPackageContent(directory);

        for (String fileName : classNames) {
            String className = fileName.substring(fileName.lastIndexOf("/") + 1);
            System.out.println(className);

            Class<?> classObject = Class.forName(fileName.replace("/", "."));
            if (classObject.isAnnotationPresent(Component.class) || classObject.isAnnotationPresent(Service.class)) {
                Object bean = classObject.getDeclaredConstructor().newInstance();
                String beanId = className.substring(0, 1).toLowerCase() + className.substring(1);
                beans.put(beanId, bean);
            }
        }
    }

    public void dependencyInjector() {
        for (Object object : beans.values()) {
            Class<?> clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();
            Method[] methods = clazz.getDeclaredMethods();
        }
    }

    public void setterInjector() {
        log.info("Start populateProperties ");

        for (Object object : beans.values()) {
            for (Field field : object.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {

                    for (Object dependency : beans.values()) {
                        if (dependency.getClass().equals(field.getType())) {
                            String setterName = "set" + field.getName().substring(0, 1).toUpperCase()
                                    + field.getName().substring(1);
                            try {
                                Method set = object.getClass().getMethod(setterName, dependency.getClass());
                                set.invoke(object, dependency);
                            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                                log.error("Can't found method");
                                throw new RuntimeException("Can't found method", e);
                            }

                        }
                    }
                }
            }
        }
    }

    public void constructorInjection() {
        for (Object object : beans.values()) {
            Constructor<?>[] constructors = object.getClass().getDeclaredConstructors();
            for (Constructor<?> constructor : constructors) {
                Autowired annotation = constructor.getAnnotation(Autowired.class);
                if (annotation != null) {
                    for (Object dependency : beans.values()) {
                        String constructorName = constructor.getName();
                        try {
                            Constructor<?> constructorInject = object.getClass().getConstructor(Class.forName(constructorName), dependency.getClass());
                            constructorInject.newInstance();
                        } catch (NoSuchMethodException | ClassNotFoundException e) {
                            throw new RuntimeException("Can't found constructor");
                        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void injectionBeanName() {
        for (String name : beans.keySet()) {
            Object bean = beans.get(name);
            if (bean instanceof BeanNameAware) {
                ((BeanNameAware) bean).setBeanName(name);
            }
        }
    }

    public void close() {
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
            if (bean instanceof DisposableBean) {
                ((DisposableBean) bean).destroy();
            }
        }
    }


}
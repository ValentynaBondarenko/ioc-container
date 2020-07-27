package com.bondarenko.bean.factory;

import com.bondarenko.bean.factory.stereotype.Autowired;
import com.bondarenko.bean.factory.stereotype.Component;
import com.bondarenko.bean.factory.stereotype.Service;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

//singletons
public class BeanProcessor {
    private final Logger log = LogManager.getLogger(BeanProcessor.class);
    private Map<String, Object> singletons = new HashMap<String, Object>();

    public Object getBean(String beanName) {
        return singletons.get(beanName);
    }

    public void searchClass(String directory) {
        try {
            log.info("Start search classes in directory");
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();

            String path = directory.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File file = new File(resource.toURI());

                for (File classFile : Objects.requireNonNull(file.listFiles())) {
                    String fileName = classFile.getName();
                    log.info("Found class " + fileName);
                    createBean(fileName, directory);
                }
            }

        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    void createBean(String fileName, String directory) {

        if (fileName.endsWith(".class")) {
            String className = fileName.substring(0, fileName.indexOf("."));
            try {
                Class<?> classObject = Class.forName(directory + "." + className);//for full name get instance Class
                if (classObject.isAnnotationPresent(Component.class) || classObject.isAnnotationPresent(Service.class)) {
                    log.info("All class with annotation Component:  " + fileName);

                    //create new Instance
                    Object bean = classObject.getDeclaredConstructor().newInstance();
                    String beanId = className.substring(0, 1).toLowerCase() + className.substring(1);
                    singletons.put(beanId, bean);
                }
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException |
                    InvocationTargetException | NullPointerException e) {
                log.error("Can't read file");
                throw new RuntimeException("Can't read file");
            }
        }
    }

    public void getBeanProperties() {
        for (Object object : singletons.values()) {
            for (Field field : object.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {

                    for (Object dependency : singletons.values()) {
                        if (dependency.getClass().equals(field.getType())) {
                            //inject properties
                            String setterName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                            try {
                                Method set = object.getClass().getMethod(setterName, dependency.getClass());
                                set.invoke(object, dependency);
                            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            }

        }

    }
}
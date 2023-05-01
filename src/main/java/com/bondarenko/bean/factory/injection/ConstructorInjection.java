package com.bondarenko.bean.factory.injection;

import com.bondarenko.bean.factory.annotation.Autowired;
import com.bondarenko.bean.factory.exception.CountConstructorException;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Map;

public class ConstructorInjection implements Injection {
    private static final Logger LOGGER = LogManager.getLogger(ConstructorInjection.class);

    @Override
    public void inject(Map<String, Object> beans) {
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Object object = entry.getValue();
            Constructor<?>[] constructors = object.getClass().getDeclaredConstructors();

            for (Constructor<?> constructor : constructors) {
                constructor.setAccessible(true);
                if (constructor.isAnnotationPresent(Autowired.class) && validateCountConstructor(constructors)) {
                    LOGGER.info("Check constructor : {} annotation @Autowired", constructor.getName());

                    Type[] typesParametersConstructor = constructor.getParameterTypes();
                    for (Type param : typesParametersConstructor) {
                        String paramName = param.getTypeName();
                        LOGGER.info("Get constructor parameter : {} ", paramName);
                        initClass(beans, paramName);
                    }

                    Object[] parametersObject = new Object[typesParametersConstructor.length];
                    for (int i = 0; i < typesParametersConstructor.length; i++) {
                        parametersObject[i] = getObjectType(beans, typesParametersConstructor[i]);
                    }
                    try {
                        entry.setValue(constructor.newInstance(parametersObject));

                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        LOGGER.error("Can't inject constructor");
                        throw new RuntimeException("Can't inject constructor", e);
                    }
                }
            }
        }

    }

    @SneakyThrows
    private Object getObjectType(Map<String, Object> beans, Type type) {
        for (Object object : beans.values()) {
            if (object.getClass().getTypeName().equals(type.getTypeName())) {
                return object;
            }
        }
        return null;
    }

    @SneakyThrows
    private void initClass(Map<String, Object> beans, String fileName) {

        String className = fileName.substring(fileName.lastIndexOf(".") + 1);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            String objectKey = entry.getKey();
            if (!objectKey.equalsIgnoreCase(fileName)) {
                Class<?> classObject = Class.forName(fileName);
                Object bean = classObject.getDeclaredConstructor().newInstance();
                String beanId = createBeanId(className);
                beans.put(beanId, bean);
            }
        }
    }

    /**
     * Must be only one constructor in class with annotation @Autowired
     */
    @SneakyThrows
    public boolean validateCountConstructor(Constructor<?>[] constructors) {
        int count = 0;
        for (Constructor<?> constructor : constructors) {
            if (constructor.isAnnotationPresent(Autowired.class)) {
                count++;
            }
        }
        if (count == 1) {
            return true;
        } else {
            LOGGER.error("More than two constructors in class with annotation @Autowired");
            throw new CountConstructorException("More than two constructors in class with annotation @Autowired");
        }

    }

    private String createBeanId(String classObject) {
        int index = classObject.lastIndexOf(".");
        return classObject.substring(index).replace(".", "").toLowerCase();
    }
}

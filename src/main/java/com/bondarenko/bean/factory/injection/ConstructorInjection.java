package com.bondarenko.bean.factory.injection;

import com.bondarenko.bean.factory.annotation.Autowired;
import com.bondarenko.bean.factory.exception.CountConstructorException;
import com.bondarenko.bean.factory.util.StringParsUtil;
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
            Constructor<?>[] constructors = entry.getValue().getClass().getDeclaredConstructors();

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
                    createBean(entry, constructor, parametersObject);
                }
            }
        }
    }

    /**
     * Must be only one constructor in class with annotation @Autowired
     */
    public boolean validateCountConstructor(Constructor<?>[] constructors) {
        int autowiredConstructorsCount = 0;
        for (Constructor<?> constructor : constructors) {
            if (constructor.isAnnotationPresent(Autowired.class)) {
                autowiredConstructorsCount++;
            }
        }
        if (autowiredConstructorsCount == 1) {
            return true;
        } else {
            String errorMessage = "The class must have only one constructor annotated with @Autowired, found " + autowiredConstructorsCount + ".";
            LOGGER.error(errorMessage);
            throw new CountConstructorException(errorMessage);
        }
    }

    private String createBeanId(String classObject) {
        return classObject.contains(".") ? StringParsUtil.getClassNameFromPath(classObject).toLowerCase() :
                classObject.toLowerCase();
    }

    private void createBean(Map.Entry<String, Object> entry, Constructor<?> constructor, Object[] parametersObject) {
        try {
            entry.setValue(constructor.newInstance(parametersObject));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("Can't inject constructor");
            throw new RuntimeException("Can't inject constructor", e);
        }
    }

    private Object getObjectType(Map<String, Object> beans, Type type) {
        for (Object object : beans.values()) {
            if (object.getClass().getTypeName().equals(type.getTypeName())) {
                return object;
            }
        }
        return null;
    }

    private void initClass(Map<String, Object> beans, String path) {
        String className = StringParsUtil.getClassNameFromPath(path);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            String objectKey = entry.getKey();
            if (!objectKey.equalsIgnoreCase(path)) {
                Class<?> classObject = getClassObject(path);
                Object bean = createBean(classObject);
                String beanId = createBeanId(className);
                beans.put(beanId, bean);
            }
        }
    }

    private Class<?> getClassObject(String fileName) {
        try {
            return Class.forName(fileName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Object createBean(Class<?> classObject) {
        try {
            return classObject.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}

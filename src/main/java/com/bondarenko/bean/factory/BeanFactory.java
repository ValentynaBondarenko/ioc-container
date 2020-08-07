package com.bondarenko.bean.factory;

import com.bondarenko.bean.factory.annotation.Autowired;
import com.bondarenko.bean.factory.annotation.PreDestroy;
import com.bondarenko.bean.factory.annotation.stereotype.*;
import com.bondarenko.bean.factory.config.BeanPostProcessor;
import com.bondarenko.bean.factory.exception.CountConstructorException;
import com.bondarenko.bean.factory.util.PackageScanner;
import lombok.SneakyThrows;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.lang.reflect.*;
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

    public int beansSize() {
        return beans.size();
    }

    @SneakyThrows
    public void init(String directory) {
        log.info("Start init method");
        List<? extends String> classNames = PackageScanner.getPackageContent(directory);
        for (String fileName : classNames) {
            String className = fileName.substring(fileName.lastIndexOf("/") + 1);
            Class<?> classObject = Class.forName(fileName.replace("/", "."));

            if (classObject.isAnnotationPresent(Component.class) || classObject.isAnnotationPresent(Service.class)) {
                Object bean = classObject.getDeclaredConstructor().newInstance();
                String beanId = className.substring(0, 1).toLowerCase() + className.substring(1);
                beans.put(beanId, bean);
            }
        }
    }

    public void setterInjector() {
        log.info("Start setter inject");

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
                                log.error("Can't found method to inject setter");
                                throw new RuntimeException("Can't found method", e);
                            }
                        }
                    }
                }
            }
        }
    }

    @SneakyThrows
    private void initClass(String fileName) {

        String className = fileName.substring(fileName.lastIndexOf(".") + 1);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            String objectKey = entry.getKey();
            if (!objectKey.equalsIgnoreCase(fileName)) {
                Class<?> classObject = Class.forName(fileName);
                Object bean = classObject.getDeclaredConstructor().newInstance();
                String beanId = className.substring(0, 1).toLowerCase() + className.substring(1);
                beans.put(beanId, bean);
            }
        }
    }

    @SneakyThrows
    public void constructorInjection() {
        log.info("Start constructor inject");
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Object object = entry.getValue();
            Constructor<?>[] constructors = object.getClass().getDeclaredConstructors();

            for (Constructor<?> constructor : constructors) {
                constructor.setAccessible(true);
                if (constructor.isAnnotationPresent(Autowired.class) && validateCountConstructor(constructors)) {
                    log.info("Check constructor : {} annotation @Autowired", constructor.getName());

                    Type[] typesParametersConstructor = constructor.getParameterTypes();
                    for (Type param : typesParametersConstructor) {
                        String paramName = param.getTypeName();
                        log.info("Get constructor parameter : {} ", paramName);
//                        if (searchPrimitiveType(param)) {
//                        }
                        initClass(paramName);
                    }

                    Object[] parametersObject = new Object[typesParametersConstructor.length];
                    for (int i = 0; i < typesParametersConstructor.length; i++) {
                        parametersObject[i] = getObjectType(typesParametersConstructor[i]);
                    }
                    try {
                        entry.setValue(constructor.newInstance(parametersObject));

                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        log.error("Can't inject constructor");
                        throw new RuntimeException("Can't inject constructor", e);
                    }
                }
            }
        }
    }

    @SneakyThrows
    private Object getObjectType(Type type) {
        for (Object object : beans.values()) {
            if (object.getClass().getTypeName().equals(type.getTypeName())) {
                return object;
            }
        }
        return null;
    }

    private boolean searchPrimitiveType(Type type) throws ClassNotFoundException {
        Constant constant = new Constant();
        for (Map.Entry<String, Class<?>> mapEntry : constant.CLASS_MAP.entrySet()) {
            Object primitiveClass = mapEntry.getValue();
            String paramName = type.getTypeName();
            if (primitiveClass.equals(Class.forName(paramName))) {
                break;
            }
        }
        return true;
    }

    public void injectBeanNames() {
        for (String name : beans.keySet()) {
            Object bean = beans.get(name);

            if (bean instanceof BeanNameAware) {
                ((BeanNameAware) bean).setBeanName(name);
            }
        }
    }

    public void initializeBeans() {
        for (String name : beans.keySet()) {
            Object bean = beans.get(name);

            for (BeanPostProcessor postProcessor : postProcessors) {
                postProcessor.postProcessBeforeInitialization(bean, name);
            }

            for (BeanPostProcessor postProcessor : postProcessors) {
                postProcessor.postProcessAfterInitialization(bean, name);
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
            log.error("More than two constructors in class with annotation @Autowired");
            throw new CountConstructorException("More than two constructors in class with annotation @Autowired");
        }

    }
}
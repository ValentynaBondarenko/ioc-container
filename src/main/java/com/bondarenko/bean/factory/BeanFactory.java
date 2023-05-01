package com.bondarenko.bean.factory;

import com.bondarenko.bean.factory.annotation.stereotype.Component;
import com.bondarenko.bean.factory.annotation.stereotype.Service;
import com.bondarenko.bean.factory.config.BeanPostProcessor;
import com.bondarenko.bean.factory.destroy.DestroyBean;
import com.bondarenko.bean.factory.destroy.DestroyBeanImpl;
import com.bondarenko.bean.factory.injection.ConstructorInjection;
import com.bondarenko.bean.factory.injection.FieldInjection;
import com.bondarenko.bean.factory.injection.Injection;
import com.bondarenko.bean.factory.util.PackageScanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Create new bean. Bean equals object.
 */
public class BeanFactory {
    private static final Logger LOGGER = LogManager.getLogger(BeanFactory.class);
    private final Map<String, Object> beans = new ConcurrentHashMap<>();
    private List<BeanPostProcessor> postProcessors = new ArrayList<>();
    private Injection injection;
    private DestroyBean destroyBean;

    public void addPostProcessor(BeanPostProcessor postProcessor) {
        postProcessors.add(postProcessor);
    }

    public Object getBean(String beanName) {
        return beans.get(beanName);
    }

    public int beansSize() {
        return beans.size();
    }

    /**
     * Create new instance
     *
     * @param directory- the directory in which potential beans will be searched
     */
    public void init(String directory) {
        LOGGER.info("Start init method");
        List<? extends String> classNames = PackageScanner.getPackageContent(directory);
        for (String fileName : classNames) {
            String className = fileName.substring(fileName.lastIndexOf("/") + 1);
            Class<?> classObject = getClassObject(fileName);
            if (classObject.isAnnotationPresent(Component.class) || classObject.isAnnotationPresent(Service.class)) {
                Object bean = createNewBean(classObject);
                String beanId = createBeanId(className);
                beans.put(beanId, bean);
            }
        }
    }

    /**
     * Field injection to the bean
     */
    public void fieldInjector() {
        injection = new FieldInjection();
        injection.inject(beans);
    }

    /**
     * Constructor injection to the bean
     */
    public void constructorInjection() {
        LOGGER.info("Start constructor inject");
        injection = new ConstructorInjection();
        injection.inject(beans);
    }

    public void injectBeanNames() {
        for (String name : beans.keySet()) {
            Object bean = beans.get(name);

            if (bean instanceof BeanNameAware) {
                ((BeanNameAware) bean).setBeanName(name);
            }
        }
    }

    /**
     * Customize the created bean
     */
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
        destroyBean = new DestroyBeanImpl();
        destroyBean.close(beans);
    }

    private Class<?> getClassObject(String fileName) {
        try {
            return Class.forName(fileName.replace("/", "."));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Object createNewBean(Class<?> classObject) {
        try {
            return classObject.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private String createBeanId(String className) {
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }
}
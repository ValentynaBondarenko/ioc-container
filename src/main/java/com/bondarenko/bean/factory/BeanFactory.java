package com.bondarenko.bean.factory;

import com.bondarenko.bean.factory.annotation.stereotype.Component;
import com.bondarenko.bean.factory.config.BeanPostProcessor;
import com.bondarenko.bean.factory.destroy.DestroyBean;
import com.bondarenko.bean.factory.destroy.DestroyBeanImpl;
import com.bondarenko.bean.factory.injection.ConstructorInjector;
import com.bondarenko.bean.factory.injection.FieldInjector;
import com.bondarenko.bean.factory.util.PackageScanner;
import com.bondarenko.bean.factory.util.StringParsUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Create new bean. Bean equals object.
 */
@Slf4j
public class BeanFactory {
    private final Map<String, Object> beans = new ConcurrentHashMap<>();
    private List<BeanPostProcessor> postProcessors = new ArrayList<>();
    private ConstructorInjector constructorInjector;
    private FieldInjector fieldInjector;

    private DestroyBean destroyBean;

    public BeanFactory() {
        constructorInjector = new ConstructorInjector();
        fieldInjector = new FieldInjector();
        destroyBean = new DestroyBeanImpl();
    }

    public BeanFactory(String directory) {
        this();
        initialize(directory);
    }

    private void initialize(String directory) {
        validateInjectors();
        createBeans(directory);
        injectFieldDependencies();
        constructorInjection();
        injectBeanNames();
        initializeBeans();
    }

    private void injectFieldDependencies() {
        fieldInjector.inject(beans);
    }

    private void constructorInjection() {
        constructorInjector.inject(beans);
    }

    private void validateInjectors() {

    }

    /**
     * Get bean from context
     *
     * @param beanName
     * @return bean
     */
    public Object getBean(String beanName) {
        return beans.get(beanName);
    }

    /**
     * Count beans in context
     *
     * @return Count beans
     */
    public int beansSize() {
        return beans.size();
    }

    /**
     * Create new instance
     *
     * @param directory- the directory in which potential beans will be searched
     */
    private void createBeans(String directory) {
        log.info("Start init method");
        List<? extends String> classNames = PackageScanner.getClassFullNames(directory);
        for (String fileName : classNames) {
            String className = StringParsUtil.getClassNameFromPath(fileName);
            Class<?> classObject = getClassObject(fileName);
            createBeanIfComponent(className, classObject);
        }
    }

    private void injectBeanNames() {
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
    private void initializeBeans() {
        for (String name : beans.keySet()) {
            Object bean = beans.get(name);

            for (BeanPostProcessor postProcessor : postProcessors) {
                Object newBeanValue = postProcessor.postProcessBeforeInitialization(bean, name);
                beans.put(name, newBeanValue);
            }

            initMethod();

            for (BeanPostProcessor postProcessor : postProcessors) {
                Object newBean = postProcessor.postProcessAfterInitialization(bean, name);
                beans.put(name, newBean);
            }
        }
    }

    /**
     * Initialization bean. If method in bead contains annotation @PostConstruct this method will be run
     */

    private void initMethod() {
        //TODO : [vb] add logic
    }

    /**
     * Destroy bean.
     */
    public void close() {
        destroyBean = new DestroyBeanImpl();
        destroyBean.destroy(beans);
    }

    private Class<?> getClassObject(String fileName) {
        try {
            return Class.forName(fileName);
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

    private void createBeanIfComponent(String className, Class<?> classObject) {
        if (classObject.isAnnotationPresent(Component.class)) {
            Object bean = createNewBean(classObject);
            String beanId = createBeanId(className);
            beans.put(beanId, bean);
        }
    }
}
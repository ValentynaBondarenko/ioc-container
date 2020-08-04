package com.bondarenko.context;

import com.bondarenko.bean.factory.BeanFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;

public class ApplicationContext {
    private static Logger log = LogManager.getLogger(ApplicationContext.class);
    private BeanFactory beanFactory = new BeanFactory();

    public ApplicationContext(String directory) throws InvocationTargetException, NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        log.info("Context in constructor start");
        beanFactory.init(directory);
        beanFactory.setterInjector();
        beanFactory.constructorInjection();
        beanFactory.injectionBeanName();

    }

    public void close() {
        beanFactory.close();
        log.info("Close application context");
    }


}

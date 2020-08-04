package com.bondarenko.context;

import com.bondarenko.bean.factory.BeanFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ApplicationContext {
    private static final Logger log = LogManager.getLogger(ApplicationContext.class);
    private final BeanFactory beanFactory = new BeanFactory();

    public ApplicationContext(String directory) {
        log.info("Context in constructor start");
        beanFactory.init(directory);
        beanFactory.setterInjector();
        beanFactory.constructorInjection();
        beanFactory.injectBeanNames();
        beanFactory.initializeBeans();

    }

    public void close() {
        beanFactory.close();
        log.info("Close application context");
    }
}

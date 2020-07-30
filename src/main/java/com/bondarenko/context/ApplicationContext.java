package com.bondarenko.context;

import com.bondarenko.bean.factory.BeanProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ApplicationContext {
    private static Logger log = LogManager.getLogger(ApplicationContext.class);
    private BeanProcessor beanProcessor = new BeanProcessor();

    public ApplicationContext(String directory) {
        log.info("Context in constructor start");
        beanProcessor.instantiate(directory);
        beanProcessor.beanProperties();
        beanProcessor.injectionBeanName();

    }
    public void close(){
        beanProcessor.close();
        log.info("Close application context");
    }
}

package com.bondarenko.context;

import com.bondarenko.bean.factory.BeanFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Create new bean. Bean equals object.
 */
public class ApplicationContext {
    private static final Logger LOGGER = LogManager.getLogger(ApplicationContext.class);
    private final BeanFactory beanFactory;

    public ApplicationContext(String directory) {
        LOGGER.info("Start application context");
        beanFactory = new BeanFactory();

        beanFactory.init(directory);
        beanFactory.fieldInjector();
        beanFactory.constructorInjection();
        beanFactory.injectBeanNames();
        beanFactory.initializeBeans();
    }

    public void close() {
        beanFactory.close();
        LOGGER.info("Close application context");
    }
}

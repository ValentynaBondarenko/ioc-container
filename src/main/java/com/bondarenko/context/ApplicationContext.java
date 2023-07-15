package com.bondarenko.context;

import com.bondarenko.bean.factory.BeanFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * Create new bean. Bean equals object.
 */
@Slf4j
public class ApplicationContext {
    private final BeanFactory beanFactory;

    public ApplicationContext(String directory) {
        log.info("Start application context");
        beanFactory = new BeanFactory(directory);
    }

    public Object getBean(String name) {
        return beanFactory.getBean(name);
    }

    public void close() {
        beanFactory.close();
        log.info("Close application context");
    }


}

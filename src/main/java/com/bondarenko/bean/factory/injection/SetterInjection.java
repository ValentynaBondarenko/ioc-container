package com.bondarenko.bean.factory.injection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class SetterInjection implements Injection {
    private static final Logger LOGGER = LogManager.getLogger(SetterInjection.class);


    @Override
    public void inject(Map<String, Object> beans) {
    }
}

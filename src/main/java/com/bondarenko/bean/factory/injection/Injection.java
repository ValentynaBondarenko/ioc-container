package com.bondarenko.bean.factory.injection;
import java.util.Map;

public interface  Injection {
    /**
     *  Inject bean into another bean
     * @param beans depends on beanId
     */
    void inject(Map<String, Object> beans);
}

package com.bondarenko.bean.factory.destroy;

import java.util.Map;

public interface DestroyBean {
    void destroy(Map<String, Object> beans);
}

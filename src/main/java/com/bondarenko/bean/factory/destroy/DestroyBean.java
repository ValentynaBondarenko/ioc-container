package com.bondarenko.bean.factory.destroy;

import java.util.Map;

public interface DestroyBean {
    void close(Map<String, Object> beans);
}

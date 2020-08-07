package com.bondarenko.bean.factory;

import java.util.HashMap;
import java.util.Map;

public class Constant {
    Map<String, Class<?>> CLASS_MAP = new HashMap<>() {{
        put("String", String.class);
        put("int", int.class);
        put("double", double.class);
        put("char", char.class);
        put("short", short.class);
        put("long", long.class);
        put("float", float.class);
        put("Integer", Integer.class);
        put("Double", Double.class);
        put("Short", Short.class);
        put("Long", Long.class);
        put("Float", Float.class);
    }};
}

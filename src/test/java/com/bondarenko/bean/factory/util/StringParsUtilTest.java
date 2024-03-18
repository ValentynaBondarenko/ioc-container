package com.bondarenko.bean.factory.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StringParsUtilTest {

    @Test
    public void testGetClassName() {
        String path = "testclasses.WithNonParameterizedConstructor";

        String classNameFromPath = StringParsUtil.getClassNameFromPath(path);

        assertEquals("WithNonParameterizedConstructor", classNameFromPath);
    }

    @Test
    public void testGetClassNameFromPath_ValidPath() {
        String path = "com.example.package.ClassName";
        String className = StringParsUtil.getClassNameFromPath(path);
        assertEquals("ClassName", className);
    }

    @Test
    public void testGetClassNameFromPath_RootPackage() {
        String path = "ClassName";
        String className = StringParsUtil.getClassNameFromPath(path);
        assertEquals("ClassName", className);
    }

    @Test
    public void testGetClassNameFromPath_ClassWithDots() {
        String path = "com.example.package.MyClass.WithDots";
        String className = StringParsUtil.getClassNameFromPath(path);
        assertEquals("WithDots", className);
    }

    @Test
    public void testGetClassNameFromPath_NullPath() {
        assertThrows(NullPointerException.class, () -> StringParsUtil.getClassNameFromPath(null));
    }

    @Test
    public void testGetClassNameFromPath_EmptyPath() {
        String path = "";
        assertThrows(StringIndexOutOfBoundsException.class, () -> StringParsUtil.getClassNameFromPath(path));
    }

    @Test
    public void testGetClassNameFromPath_NoDotInPath() {
        String path = "com/example/package/ClassName";
        assertThrows(StringIndexOutOfBoundsException.class, () -> StringParsUtil.getClassNameFromPath(path));
    }

    @Test
    public void testGetClassNameFromPath_OnlyOneDot() {
        String path = "com.example.package.";
        assertThrows(StringIndexOutOfBoundsException.class, () -> StringParsUtil.getClassNameFromPath(path));
    }
}
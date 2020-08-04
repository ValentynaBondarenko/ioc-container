package com.bondarenko.bean.factory.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScanPackageTest {

    @Test
    void foundAllClassesInDirectory() {
        String path = "testclasses/classes";
        List<? extends String> list = ScanPackage.getPackageContent(path);
        assertEquals(5, list.size());
    }

    @Test
    void foundAllInnerClassInDirectory() {
        String path = "testclasses/packegeFirst";

        List<? extends String> list = ScanPackage.getPackageContent(path);
        assertEquals(1, list.size());
    }

    @Test
    void foundAllClassesInPackage() {
        String path = "testclasses";

        List<? extends String> list = ScanPackage.getPackageContent(path);

        assertEquals(7, list.size());
        assertTrue(list.contains("testclasses/WithNonParameterizedConstructor"));
        assertTrue(list.contains("testclasses/classes/Main"));
        assertTrue(list.contains("testclasses/classes/MoodService"));
        assertTrue(list.contains("testclasses/classes/SongRepository"));
        assertTrue(list.contains("testclasses/classes/SongService"));
    }
}
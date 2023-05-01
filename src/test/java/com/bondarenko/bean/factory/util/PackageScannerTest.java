package com.bondarenko.bean.factory.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PackageScannerTest {

    @Test
    void foundAllClassesInDirectory() {
        String path = "testclasses/classes";
        List<? extends String> list = PackageScanner.getPackageContent(path);
        assertEquals(7, list.size());
    }

    @Test
    void foundAllInnerClassInDirectory() {
        String path = "testclasses/packegeFirst";

        List<? extends String> list = PackageScanner.getPackageContent(path);
        assertEquals(1, list.size());
    }

    @Test
    void foundAllClassesInPackage() {
        String path = "testclasses";

        List<? extends String> list = PackageScanner.getPackageContent(path);

        assertEquals(9, list.size());
        assertTrue(list.contains("testclasses.WithNonParameterizedConstructor"));
        assertTrue(list.contains("testclasses.classes.Main"));
        assertTrue(list.contains("testclasses.classes.MoodService"));
        assertTrue(list.contains("testclasses.classes.SongRepository"));
        assertTrue(list.contains("testclasses.classes.SongService"));
        assertTrue(list.contains("testclasses.packegeFirst.MetaInfoService"));
        assertTrue(list.contains("testclasses.classes.RepositoryContainer"));
        assertTrue(list.contains("testclasses.classes.CurrentPostProcessor"));
        assertTrue(list.contains("testclasses.classes.RepositoryContainer$1"));
    }
}
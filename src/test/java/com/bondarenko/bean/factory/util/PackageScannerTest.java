package com.bondarenko.bean.factory.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PackageScannerTest {
    @Test
    void foundAllClassesInPackage() {
        String path = "testclasses";

        List<? extends String> list = PackageScanner.getClassFullNames(path);

        assertEquals(11, list.size());
        assertTrue(list.contains("testclasses.WithNonParameterizedConstructor"));
        assertTrue(list.contains("testclasses.classes.MoodService"));
        assertTrue(list.contains("testclasses.classes.SongRepository"));
        assertTrue(list.contains("testclasses.classes.SongService"));
        assertTrue(list.contains("testclasses.packegeFirst.MetaInfoService"));
        assertTrue(list.contains("testclasses.classes.RepositoryContainer"));
        assertTrue(list.contains("testclasses.classes.processor.CurrentPostProcessor"));
        assertTrue(list.contains("testclasses.classes.RepositoryContainer$1"));
        assertTrue(list.contains("testclasses.classes.destroy.DestroyService"));
        assertTrue(list.contains("testclasses.classes.destroy.RemoveService"));
        assertTrue(list.contains("testclasses.classes.processor.Order"));
    }
}
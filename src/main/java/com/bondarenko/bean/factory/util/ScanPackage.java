package com.bondarenko.bean.factory.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ScanPackage {

    public static ClassLoader getCurrentClassLoader() {
        ClassLoader classLoader;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        } catch (SecurityException ex) {
            throw new RuntimeException("Can't read files");
        }
        return classLoader;
    }

    public static List<? extends String> getPackageContent(String scanPackages) {
        final List<String> list = new ArrayList<>();

        final URL baseDirectory = getCurrentClassLoader().getResource(scanPackages);
        try {
            Enumeration<URL> resources = getCurrentClassLoader().getResources(scanPackages);
            String basePackage = baseDirectory.getPath();

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                Path directory = Paths.get(resource.getFile());
                Files.walk(directory)
                        .filter(fileName -> fileName.toString().endsWith(".class"))
                        .map(fileName -> {
                            File file = fileName.toFile();

                            String filePath = file.getPath();
                            filePath = filePath.replace(basePackage, "");
                            String className = filePath.replace(".class", "");

                            return scanPackages + className.replace(".", File.separator);

                        }).distinct()
                        .forEach(fileName -> {
                            list.add(fileName);
                        });
            }
            return list;
        } catch (IOException e) {
            throw new RuntimeException("Can't found files and classes", e);
        }
    }
}

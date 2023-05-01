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
import java.util.Objects;
import java.util.stream.Stream;

/**
 * PackageScanner to search for classes in packages
 */
public final class PackageScanner {
    private static final String SLASH = "/";
    private static final String DOT = ".";
    private static final String EXPANSION_CLASS = ".class";

    private PackageScanner() {
    }

    /**
     * find all ".class" in a packages
     *
     * @param scanPackages main path which searches inside all packages for classes
     * @return List<String> with class paths
     */
    public static List<? extends String> getPackageContent(String scanPackages) {
        List<String> classPaths = new ArrayList<>();

        Enumeration<URL> resources;
        try {
            resources = getCurrentClassLoader().getResources(scanPackages);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            Path directory = Paths.get(resource.getFile());
            try (Stream<Path> walk = Files.walk(directory)) {
                walk.filter(clasName -> clasName.toString().endsWith(EXPANSION_CLASS))
                        .map(fileName -> getClassPath(scanPackages, fileName))
                        .distinct()
                        .forEach(classPaths::add);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return classPaths;
    }

    private static String getClassPath(String scanPackages, Path fileName) {
        URL baseDirectory = getCurrentClassLoader().getResource(scanPackages);
        String basePackage = Objects.requireNonNull(baseDirectory).getPath();
        File file = fileName.toFile();

        String filePath = file.getPath();
        filePath = filePath.replace(basePackage, "");
        String classNameWithoutExpansion = filePath.replace(EXPANSION_CLASS, "");
        String path = scanPackages + classNameWithoutExpansion;
        return path.replace(SLASH, DOT);
    }

    private static ClassLoader getCurrentClassLoader() {
        ClassLoader classLoader;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        } catch (SecurityException ex) {
            throw new RuntimeException("Can't read files");
        }
        return classLoader;
    }
}
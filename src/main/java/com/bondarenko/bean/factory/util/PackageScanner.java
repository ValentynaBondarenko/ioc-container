package com.bondarenko.bean.factory.util;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Stream;


/**
 * PackageScanner to search for classes in packages
 */
public class PackageScanner {
    private static final String SLASH = "/";
    private static final String DOT = ".";
    private static final String EXPANSION_CLASS = ".class";

    /**
     * find all ".class" in a packages
     *
     * @param scanPackages main path which searches inside all packages for classes
     * @return List<String> with class paths
     */
    public static List<String> getPackageContent(String scanPackages) {
        Enumeration<URL> resources = getUrlResurces(scanPackages);
        List<String> list = new ArrayList<>();

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            Path directory = Paths.get(resource.getFile());

            try (Stream<Path> walk = Files.walk(directory);) {
                walk.filter(classPath -> classPath.toString().endsWith(EXPANSION_CLASS))
                        .map(classPath -> getClassPath(classPath.toString(), scanPackages))
                        .forEach(list::add);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return list;
    }

    private static Enumeration<URL> getUrlResurces(String scanPackages) {
        Enumeration<URL> resources = null;
        try {
            resources = getCurrentClassLoader().getResources(scanPackages);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resources;
    }

    private static String getClassPath(String clasPath, String scanPackages) {
        int index = clasPath.indexOf(scanPackages);
        String path = clasPath.substring(index);
        String pathClass = path.replace(EXPANSION_CLASS, "");
        return pathClass.replace(SLASH, DOT);

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

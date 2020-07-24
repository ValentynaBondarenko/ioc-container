package com.bondarenko.bean.factory;

import com.bondarenko.bean.factory.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

//singletons
public class BeanFactory {
   // private final Logger log = LoggerFactory.getLogger(getClass());
    private Map<String, Object> singletons = new HashMap<String, Object>();

    public Object getBean(String beanName) {
        return singletons.get(beanName);
    }

    public void searchClass(String directory) {
        try {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();

            String path = directory.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);//???
           // log.error("Can't read");//?????


            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File file = new File(resource.toURI());

                for (File classFile : file.listFiles()) {
                    String fileName = classFile.getName();//ProductService.class
                 //   log.debug("can't search file name class " + fileName);
                    System.out.println(fileName);
                    if (fileName.endsWith(".class")) {
                        String className = fileName.substring(0, fileName.indexOf("."));
                        Class classObject = null;//for full name get instance Class

                        classObject = Class.forName(directory + "." + className);
                      //  log.error("Can't found file " + className);

                        if (classObject.isAnnotationPresent(Component.class)) {
                            System.out.println("Component: " + classObject);
                     //       log.debug("All class with annotation Component:  " + fileName);
                        }

                    }


                }
            }
        } catch (IOException | ClassNotFoundException | URISyntaxException e) {
            throw new RuntimeException("Can't read file");//!!!
        }

    }
}

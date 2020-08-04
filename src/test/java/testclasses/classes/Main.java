package testclasses.classes;

import com.bondarenko.bean.factory.BeanFactory;
import com.bondarenko.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) {

        BeanFactory beanFactory = new BeanFactory();

        beanFactory.init("testclasses/classes");

    }

//        beanFactory.setterInjector();
//        SongService songService = (SongService) beanFactory.getBean("songService");
//
//        beanFactory.injectionBeanName();
//        System.out.println("Bean name = " + songService.getBeanName());
//
//        songService.getMoodService();
//        System.out.println(songService);
//        System.out.println(songService.getMoodService());
//
//        songService.getSongRepository();
//        System.out.println(songService.getSongRepository());
//
//        ApplicationContext applicationContext = new ApplicationContext("testclasses.classes");
//        applicationContext.close();


}

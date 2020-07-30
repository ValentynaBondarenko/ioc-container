package testclasses;

import com.bondarenko.bean.factory.BeanProcessor;
import com.bondarenko.context.ApplicationContext;

public class Main {

    public static void main(String[] args) {

        BeanProcessor beanProcessor = new BeanProcessor();
        beanProcessor.instantiate("testclasses");
        beanProcessor.beanProperties();
        SongService songService = (SongService) beanProcessor.getBean("songService");
        beanProcessor.injectionBeanName();
        System.out.println("Bean name = " + songService.getBeanName());

        songService.getMoodService();
        System.out.println(songService);
       System.out.println(songService.getMoodService());

         beanProcessor.beanProperties();

        ApplicationContext applicationContext=new ApplicationContext("testclasses");

    }
}

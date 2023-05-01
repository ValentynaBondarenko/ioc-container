package testclasses.classes;

import com.bondarenko.bean.factory.BeanFactory;
import com.bondarenko.context.ApplicationContext;

public class Main {

    public static void main(String[] args) {

        BeanFactory beanFactory = new BeanFactory();
        beanFactory.addPostProcessor(new CurrentPostProcessor());

        beanFactory.init("testclasses");

        beanFactory.fieldInjector();
        SongService songService = (SongService) beanFactory.getBean("songService");


        songService.getMoodService();
        System.out.println(songService);
        System.out.println(songService.getMoodService());

        songService.getSongRepository();
        System.out.println(songService.getSongRepository());

        ApplicationContext applicationContext = new ApplicationContext("testclasses/classes");
        applicationContext.close();
    }
}

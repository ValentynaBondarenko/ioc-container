package com.bondarenko.bean.factory;

import com.bondarenko.bean.factory.annotation.Autowired;
import com.bondarenko.bean.factory.annotation.stereotype.Component;
import com.bondarenko.bean.factory.annotation.stereotype.Service;
import org.junit.jupiter.api.Test;
import testclasses.classes.MoodService;
import testclasses.classes.SongService;

import static org.junit.Assert.*;


class BeanFactoryTest {
    private final BeanFactory beanFactory = new BeanFactory();
    private final String pathTest = "testclasses/classes";

    @Test
    void beanShouldBeInstance() {
        //when
        beanFactory.init(pathTest);

        //then
        MoodService moodService = (MoodService) beanFactory.getBean("moodService");
        assertTrue(moodService.getClass().isInstance(moodService));
    }

    @Test
    void beanBeInstanceIfClassHaveAnnotationComponent() {
        //when
        beanFactory.init(pathTest);

        //then
        MoodService moodService = (MoodService) beanFactory.getBean("moodService");
        SongService songService = (SongService) beanFactory.getBean("songService");

        assertTrue(moodService.getClass().isInstance(moodService));
        assertTrue(songService.getClass().isInstance(songService));
        assertTrue(moodService.getClass().isAnnotationPresent(Component.class));
        assertTrue(songService.getClass().isAnnotationPresent(Component.class));

    }
    @Test
    void beanBeInstanceIfClassHaveAnnotationService() {
        //when
        beanFactory.init(pathTest);

        //then
        MoodService moodService = (MoodService) beanFactory.getBean("moodService");
        SongService songService = (SongService) beanFactory.getBean("songService");

        assertTrue(moodService.getClass().isAnnotationPresent(Service.class));
        assertTrue(songService.getClass().isAnnotationPresent(Service.class));

    }

// Why I can't check class without annotation?
//    @Test
//    void testInstanceBeanIfClassNotHaveAnnotationComponent() {
//        //when
//        beanFactory.init(pathTest);
//        SongService songService = (SongService) beanFactory.getBean("songService");
//
//        //then
//        assertFalse(songService.getSongRepository().getClass().isAnnotationPresent(Component.class));
//    }


}
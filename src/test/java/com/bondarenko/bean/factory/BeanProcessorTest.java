package com.bondarenko.bean.factory;

import org.junit.jupiter.api.Test;
import testclasses.MoodService;
import testclasses.SongService;

import static org.junit.Assert.*;


class BeanProcessorTest {
    private final BeanProcessor beanProcessor = new BeanProcessor();
    private final String pathTest = "testclasses";

    @Test
    void testCheckInstanceBean() {
        //when
        beanProcessor.instanceBean("MoodService.class", pathTest);

        //then
        MoodService moodService = (MoodService) beanProcessor.getBean("moodService");
        assertTrue(moodService.getClass().isInstance(moodService));
    }

    @Test
    void testInstanceBeanIfClassHaveAnnotationComponentAndAutowired() {
        //when
        beanProcessor.instantiate(pathTest);
        beanProcessor.beanProperties();

        //then
        SongService songService = (SongService) beanProcessor.getBean("songService");
        assertTrue(songService.getClass().isInstance(songService));
        assertNotNull(songService.getMoodService());
    }

    @Test
    void testInstanceBeanIfClassNotHaveAnnotationComponent() {
        //when
        beanProcessor.instantiate(pathTest);
        beanProcessor.beanProperties();
        SongService songService = (SongService) beanProcessor.getBean("songService");

        //then
        assertFalse(songService.getClass().isInstance(songService.getSongRepository()));
        assertNull(songService.getSongRepository());
    }
    @Test
    void testInstanceBeanIfClassNotHaveAnnotationComponentAndAutowired() {
        //when
        beanProcessor.instantiate(pathTest);
        beanProcessor.beanProperties();
        SongService songService = (SongService) beanProcessor.getBean("songService");

        //then
        assertFalse(songService.getClass().isInstance(songService.getMetaInfoService()));
        assertNull(songService.getMetaInfoService());
    }

}
package com.bondarenko.bean.factory;


import org.junit.jupiter.api.Test;
import test.MoodService;
import test.SongService;

import static org.junit.Assert.*;


class BeanProcessorTest {
    private final BeanProcessor beanProcessor = new BeanProcessor();
    private final String pathTest = "test";
    //private final String pathTest = "org.test";


    @Test
    void createBeanIfFileIsClass() {
        //when
        beanProcessor.createBean("MoodService.class", pathTest);

        //then
        MoodService moodService = (MoodService) beanProcessor.getBean("moodService");
        assertTrue(moodService.getClass().isInstance(moodService));

    }
    @Test
    void createBeanIfClassHaveAnnotation() {
        //when
        beanProcessor.createBean("SongService.class", pathTest);

        //then
        SongService songService = (SongService) beanProcessor.getBean("songService");
        assertTrue(songService.getClass().isInstance(songService));

    }

}
package com.bondarenko.bean.factory;


import org.junit.jupiter.api.Test;
import testclasses.MoodService;
import testclasses.SongService;

import static org.junit.Assert.*;


class BeanProcessorTest {
    private final BeanProcessor beanProcessor = new BeanProcessor();
    private final String pathTest = "testclasses";

    @Test
    void instanceBeanIfFileIsClass() {
        //when
        beanProcessor.instanceBean("MoodService.class", pathTest);

        //then
        MoodService moodService = (MoodService) beanProcessor.getBean("moodService");
        assertTrue(moodService.getClass().isInstance(moodService));

    }
    @Test
    void instanceBeanIfClassHaveAnnotation() {
        //when
        beanProcessor.instanceBean("SongService.class", pathTest);

        //then
        SongService songService = (SongService) beanProcessor.getBean("songService");
        assertTrue(songService.getClass().isInstance(songService));

    }

}
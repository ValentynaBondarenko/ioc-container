package com.bondarenko.bean.factory;

import org.junit.jupiter.api.Test;
import testclasses.classes.MoodService;
import testclasses.classes.SongService;
import testclasses.WithNonParameterizedConstructor;

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
    void testInstanceBeanIfClassHaveAnnotationComponentAndAutowired() {
        //when
        beanFactory.init(pathTest);
        beanFactory.setterInjector();

        //then
        SongService songService = (SongService) beanFactory.getBean("songService");
        assertTrue(songService.getClass().isInstance(songService));
        assertNotNull(songService.getMoodService());
    }

    @Test
    void testInstanceBeanIfClassNotHaveAnnotationComponent() {
        //when
        beanFactory.init(pathTest);
        beanFactory.setterInjector();
        SongService songService = (SongService) beanFactory.getBean("songService");

        //then
        assertFalse(songService.getClass().isInstance(songService.getSongRepository()));
        assertNull(songService.getSongRepository());
    }

    @Test
    void testInstanceBeanIfClassNotHaveAnnotationComponentAndAutowired() {
        //when
        beanFactory.init(pathTest);
        beanFactory.setterInjector();
        SongService songService = (SongService) beanFactory.getBean("songService");

        //then
        assertFalse(songService.getClass().isInstance(songService.getMetaInfoService()));
        assertNull(songService.getMetaInfoService());
    }

    @Test
    void shouldBeCreateBeanWithDefaultConstructor() {
        //when
        beanFactory.init( pathTest);
        beanFactory.constructorInjection();
        SongService songService = (SongService) beanFactory.getBean(SongService.class);

        //then
        assertEquals("songService", songService.toString());
    }

    @Test
    void shouldBeCreateBeanWithNoArgumentConstructor() {
        //when
        beanFactory.init( pathTest);
        beanFactory.constructorInjection();
        WithNonParameterizedConstructor bean = (WithNonParameterizedConstructor) beanFactory.getBean(WithNonParameterizedConstructor.class);

        //then
        assertEquals("WithNonParameterizedConstructor", bean.toString());

    }


}
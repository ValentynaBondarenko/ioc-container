package com.bondarenko.bean.factory;

import com.bondarenko.bean.factory.annotation.stereotype.Component;
import com.bondarenko.bean.factory.annotation.stereotype.Service;
import org.junit.jupiter.api.Test;
import testclasses.classes.MoodService;
import testclasses.classes.RepositoryContainer;
import testclasses.classes.SongService;
import testclasses.packegeFirst.MetaInfoService;

import static org.junit.jupiter.api.Assertions.*;

class BeanFactoryTest {
    private BeanFactory beanFactory;

    @Test
    void beanShouldBeInstance() {
        //when
        beanFactory = new BeanFactory("testclasses");

        //then
        MoodService moodService = (MoodService) beanFactory.getBean("moodService");
        assertTrue(moodService.getClass().isInstance(moodService));
    }

    @Test
    void beanBeInstanceIfClassHaveAnnotationComponent() {
        //when
        beanFactory = new BeanFactory("testclasses");

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
        beanFactory = new BeanFactory("testclasses");

        //then
        MoodService moodService = (MoodService) beanFactory.getBean("moodService");
        SongService songService = (SongService) beanFactory.getBean("songService");

        assertTrue(moodService.getClass().isAnnotationPresent(Service.class));
        assertFalse(songService.getClass().isAnnotationPresent(Service.class));

    }

    @Test
    void beanBeInstanceIfFieldsClassHasAnnotationAutowired() {
        //when
        //when
        beanFactory = new BeanFactory("testclasses");

        SongService songService = (SongService) beanFactory.getBean("songService");
        MoodService moodService = (MoodService) beanFactory.getBean("moodService");
        MetaInfoService metaInfoService = (MetaInfoService) beanFactory.getBean("metaInfoService");

        //then
        assertTrue(songService.getMoodService().getClass().isInstance(moodService));
        assertTrue(songService.getMetaInfoService().getClass().isInstance(metaInfoService));

        assertNull(songService.getSongRepository());
        assertEquals(5, beanFactory.beansSize());

    }

    @Test
    void instanceBeanIfClassHaveNotAnnotationComponent() {
        //when
        beanFactory = new BeanFactory("testclasses");
        SongService songService = (SongService) beanFactory.getBean("songService");

        //then
        assertNull(songService.getSongRepository());
    }

    @Test
    void beanBeInstanceIfClassHasAnnotationAutowiredInConstructor() {
        //when
        beanFactory = new BeanFactory("testclasses");

        //then
        SongService songService = (SongService) beanFactory.getBean("songService");
        MoodService moodService = (MoodService) beanFactory.getBean("moodService");
        RepositoryContainer repositoryContainer = (RepositoryContainer) beanFactory.getBean("repositoryContainer");

        assertNotNull(songService);
        assertNotNull(repositoryContainer);
        assertNotNull(songService.getSongRepository());
        assertNotNull(songService.getMetaInfoService());
        assertTrue(songService.getMoodService().getClass().isInstance(moodService));
        assertEquals(8, beanFactory.beansSize());
    }

}
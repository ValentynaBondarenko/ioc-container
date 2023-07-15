package com.bondarenko.bean.factory.injection;

import org.junit.jupiter.api.Test;
import testclasses.classes.RepositoryContainer;
import testclasses.classes.SongRepository;
import testclasses.classes.SongService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConstructorInjectorTest {
    @Test
    void testInject_SingleAutowiredConstructor() {
        // prepare
        ConstructorInjector constructorInjector = new ConstructorInjector();
        Map<String, Object> beans = new HashMap<>();
        beans.put("songService", new SongService());
        beans.put("songRepository", new SongRepository());

        // when
        constructorInjector.inject(beans);

        // then
        SongService songService = (SongService) beans.get("songService");
        assertNotNull(songService.getSongRepository());
    }

    @Test
    void testInject_CannotInstantiateConstructor() {
        // prepare
        ConstructorInjector constructorInjector = new ConstructorInjector();
        Map<String, Object> beans = new HashMap<>();
        RepositoryContainer repository = (RepositoryContainer) beans.put("repositoryContainer", new RepositoryContainer());

        // when & then
        assertThrows(RuntimeException.class, () -> constructorInjector.inject(beans));
    }

}
package com.bondarenko.context;

import org.junit.jupiter.api.Test;
import testclasses.classes.destroy.DestroyService;
import testclasses.classes.destroy.RemoveService;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ApplicationContextTest {
    private ApplicationContext context;

    @Test
    public void testPreDestroyMethodCalled() {
        //prepare
        context = new ApplicationContext("testclasses/classes/destroy");

        //when
        context.close();

        //then
        DestroyService destroyService = (DestroyService) context.getBean("DestroyService");
        assertTrue(destroyService.isMethodCalled());
    }

    @Test
    public void testPreDestroyMethodWasNotCall() {
        //prepare
        context = new ApplicationContext("testclasses/classes/destroy");

        //then
        DestroyService destroyService = (DestroyService) context.getBean("DestroyService");
        assertFalse(destroyService.isMethodCalled());
    }


    @Test
    public void destroyMethodCalledForAllBeansWhichHasThisMethod() {
        //prepare
        context = new ApplicationContext("testclasses/classes/destroy");

        //when
        context.close();

        //then
        DestroyService destroyService = (DestroyService) context.getBean("DestroyService");
        RemoveService removeService = (RemoveService) context.getBean("RemoveService");

        assertTrue(destroyService.isMethodCalled());
        assertTrue(removeService.isMethodCalled());
    }

    @Test
    public void destroyMethodThrowException() {
        //prepare
        context = new ApplicationContext("testclasses/classes/destroy");
        context.close();
        DestroyService destroyMockBean = mock((DestroyService) context.getBean("DestroyService"));
        when(destroyMockBean.destroyMethod()).thenThrow(RuntimeException.class);

        //when
        destroyMockBean.destroyMethod();

        //then
        verify(destroyMockBean).destroyMethod();
    }
}
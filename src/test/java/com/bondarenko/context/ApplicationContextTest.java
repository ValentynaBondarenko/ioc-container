package com.bondarenko.context;

import org.junit.jupiter.api.Test;
import testclasses.classes.destroy.DestroyService;
import testclasses.classes.destroy.RemoveService;
import testclasses.classes.processor.CurrentPostProcessor;
import testclasses.classes.processor.Order;

import static org.junit.jupiter.api.Assertions.*;

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
    public void testBeanPostProcessorSetChangesInTheBeanAndInitializesBean() {
        //prepare
        context = new ApplicationContext("testclasses/classes/processor");

        //then
        Order order = (Order) context.getBean("Order");
        CurrentPostProcessor processor = (CurrentPostProcessor) context.getBean("CurrentPostProcessor");

        assertTrue(processor.isMethodBeforeInitializationCalled());
        assertTrue(processor.isMethodAfterInitializationCalled());
        assertTrue(order.isMethodCalled());
        assertEquals("Order is done", order.getOrderDetails());
    }
}
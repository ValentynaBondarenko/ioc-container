package testclasses.classes.processor;

import com.bondarenko.bean.factory.annotation.stereotype.Component;
import com.bondarenko.bean.factory.config.BeanPostProcessor;

@Component
public class CurrentPostProcessor implements BeanPostProcessor {
    private boolean postProcessBeforeInitializationCalled;
    private boolean postProcessAfterInitializationCalled;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        postProcessBeforeInitializationCalled = true;
        if (bean instanceof Order) {
            Order order = (Order) bean;
            order.setOrderDetails("Created new order");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        postProcessAfterInitializationCalled = true;
        if (bean instanceof Order) {
            Order order = (Order) bean;
            modifyOrder(order);
        }
        return bean;
    }

    private void modifyOrder(Order order) {
        order.setOrderDetails("Order is done");
    }

    public boolean isMethodAfterInitializationCalled() {
        return postProcessAfterInitializationCalled;
    }

    public boolean isMethodBeforeInitializationCalled() {
        return postProcessBeforeInitializationCalled;
    }
}

package testclasses.classes.processor;

import com.bondarenko.bean.factory.annotation.stereotype.Component;
import com.bondarenko.bean.factory.config.BeanPostProcessor;

@Component
public class CurrentPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof Order) {
            Order order = (Order) bean;
            order.setOrderDetails("Created new order");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof Order) {
            Order order = (Order) bean;
            modifyOrder(order);
        }
        return bean;
    }

    private void modifyOrder(Order order) {
        order.setOrderDetails("Order is done");
    }
}

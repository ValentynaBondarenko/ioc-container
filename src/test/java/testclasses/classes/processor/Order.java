package testclasses.classes.processor;

import com.bondarenko.bean.factory.annotation.PostConstruct;
import com.bondarenko.bean.factory.annotation.stereotype.Component;

@Component
public class Order {
    private String orderDetails;
    private boolean methodCalled;

    @PostConstruct
    public void init() {
        methodCalled = true;
        setOrderDetails("Order in progress");
    }

    public void setOrderDetails(String orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getOrderDetails() {
        return orderDetails;
    }

    public boolean isMethodCalled() {
        return methodCalled;
    }
}

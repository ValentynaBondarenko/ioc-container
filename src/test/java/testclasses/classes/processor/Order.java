package testclasses.classes.processor;

import com.bondarenko.bean.factory.annotation.PostConstruct;
import com.bondarenko.bean.factory.annotation.stereotype.Component;

@Component
public class Order {
    private String orderDetails;

    @PostConstruct
    public void init() {
        setOrderDetails("Order in progress");
    }

    public void setOrderDetails(String orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getOrderDetails() {
        return orderDetails;
    }
}

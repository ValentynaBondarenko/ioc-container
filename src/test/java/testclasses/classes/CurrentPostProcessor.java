package testclasses.classes;

import com.bondarenko.bean.factory.config.BeanPostProcessor;

public class CurrentPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.out.println("PostProcessor Before " + beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("PostProcessor After " + beanName);
        return bean;
    }
}

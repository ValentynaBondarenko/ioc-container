package testclasses.classes.destroy;

import com.bondarenko.bean.factory.annotation.PreDestroy;
import com.bondarenko.bean.factory.annotation.stereotype.Component;

@Component
public class RemoveService {
    private boolean methodCalled;

    @PreDestroy
    public void destroyMethod() {
        methodCalled = true;
    }

    public boolean isMethodCalled() {
        return methodCalled;
    }
}

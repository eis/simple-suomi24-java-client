package fi.eis.applications.chatapp.di;

/**
 * Creation Date: 30.11.2014
 * Creation Time: 21:21
 *
 * @author eis
 */


import org.junit.Assert;
import org.junit.Test;


interface ConstDependencyInterface {
    void sayHello();
}

class ConstDependency implements ConstDependencyInterface {
    public void sayHello() {
        System.out.println("hello");
    }
}

class ConstClassToInit {
    private final ConstDependencyInterface dependency;

    @Inject
    public ConstClassToInit(ConstDependencyInterface dependency) {
        this.dependency = dependency;
    }


    @Override
    public String toString() {
        return "ConstClassToInit{" +
                "dependency=" + dependency +
                '}';
    }

    public ConstDependencyInterface getDependency() {
        return this.dependency;
    }
}

abstract class ConstAbstractParentForDependency implements ConstDependencyInterface {
    public void sayHello() {
        System.out.println("hello");
    }
}

class AnotherConstDependency extends ConstAbstractParentForDependency {}

public class DIConstructorTest {

    @Test
    public void testDi() {
        Module mSuppliers = DependencyInjection.classes(
            ConstDependency.class,
            ConstClassToInit.class
        );
        Module mClasses = DependencyInjection.classes(ConstClassToInit.class);
        Context diContext = DependencyInjection.context(mClasses, mSuppliers);
        ConstClassToInit instance = diContext.get(ConstClassToInit.class);
        Assert.assertNotNull("was not initialized: " + instance, instance.getDependency());
    }

    @Test
    public void testDiWithInheritance() {
        Module mSuppliers = DependencyInjection.classes(
            AnotherConstDependency.class,
            ConstClassToInit.class
        );
        Module mClasses = DependencyInjection.classes(ConstClassToInit.class);
        Context diContext = DependencyInjection.context(mClasses, mSuppliers);
        ConstClassToInit instance = diContext.get(ConstClassToInit.class);
        Assert.assertNotNull("was not initialized: " + instance, instance.getDependency());
    }
}
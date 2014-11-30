package fi.eis.applications.chatapp.di;

/**
 * Creation Date: 30.11.2014
 * Creation Time: 21:21
 *
 * @author eis
 */


import org.junit.Assert;
import org.junit.Test;


interface DependencyInterface {
    void sayHello();
}
class Dependency implements DependencyInterface {
    public void sayHello() {
        System.out.println("hello");
    }
}
class ClassToInit {
    @Inject
    DependencyInterface dependency;

    @Override
    public String toString() {
        return "ClassToInit{" +
                "dependency=" + dependency +
                '}';
    }

    public DependencyInterface getDependency() {
        return this.dependency;
    }
}

abstract class AbstractParentForDependency implements DependencyInterface {
    public void sayHello() {
        System.out.println("hello");
    }
}

class AnotherDependency extends AbstractParentForDependency {}

public class MyDITest {

    @Test
    public void testDi() {
        Module mSuppliers = DependencyInjection.suppliers(
            Dependency.class
        );
        Module mClasses = DependencyInjection.classes(ClassToInit.class);
        Context diContext = DependencyInjection.context(mClasses, mSuppliers);
        ClassToInit instance = diContext.get(ClassToInit.class);
        Assert.assertNotNull("was not initialized: " + instance, instance.getDependency());
    }

    @Test
    public void testDiWithInheritance() {
        Module mSuppliers = DependencyInjection.suppliers(
            AnotherDependency.class
        );
        Module mClasses = DependencyInjection.classes(ClassToInit.class);
        Context diContext = DependencyInjection.context(mClasses, mSuppliers);
        ClassToInit instance = diContext.get(ClassToInit.class);
        Assert.assertNotNull("was not initialized: " + instance, instance.getDependency());
    }
}
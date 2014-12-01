package fi.eis.applications.chatapp.di.testhelpers;

/**
 * Creation Date: 1.12.2014
 * Creation Time: 21:45
 *
 * @author eis
 */
public class DependencyMock implements DependencyMockInterface {
    @Override
    public void sayHello() {
        System.out.println("hello");
    }
}

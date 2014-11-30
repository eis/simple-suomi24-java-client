package fi.eis.applications.chatapp;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * Creation Date: 30.11.2014
 * Creation Time: 16:16
 *
 * @author eis
 */
public class DependencyInjectionIT {
    @Test
    public void testDependencyInjection() throws IllegalAccessException {
        App app = App.createInjectedApp();
        Field fields[] = App.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Assert.assertNotNull("should not be null: " + field.getName(), field.get(app));
        }
    }
}

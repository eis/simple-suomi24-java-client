package fi.eis.applications.chatapp.di;

import fi.eis.applications.chatapp.di.testhelpers.MockClassInNeedOfDependency;
import org.junit.Assert;
import org.junit.Test;

/**
 * Creation Date: 1.12.2014
 * Creation Time: 21:43
 *
 * @author eis
 */
public class DIClassScanningTest {

    @Test
    public void testDi() {
        Context diContext = DependencyInjection.classScanningContext(this.getClass());
        MockClassInNeedOfDependency instance = diContext.get(MockClassInNeedOfDependency.class);
        Assert.assertNotNull("was not initialized: " + instance, instance.dependency);
    }
}

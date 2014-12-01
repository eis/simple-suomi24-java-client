package fi.eis.applications.chatapp.di.testhelpers;

import fi.eis.applications.chatapp.di.Inject;

/**
 * Creation Date: 1.12.2014
 * Creation Time: 21:47
 *
 * @author eis
 */
public class MockClassInNeedOfDependency {
    @Inject
    public DependencyMockInterface dependency;

}

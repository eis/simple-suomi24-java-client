package fi.eis.applications.chatapp.di;

import java.util.ArrayList;
import java.util.List;

/**
 * Creation Date: 30.11.2014
 * Creation Time: 22:54
 *
 * @author eis
 */
public class DependencyInjection {
    public static Module suppliers(Class... providers) {
        return new Module(providers);
    }
    public static Module classes(Class... classes) {
        List<Class> providers = new ArrayList<>();
        for(Class clazz: classes) {
            providers.add(clazz);
        }
        return suppliers(providers.toArray(new Class[providers.size()]));
    }
    public static Context context(Module... modules) {
        return new Context(modules);
    }
}

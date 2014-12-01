package fi.eis.applications.chatapp.di;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Creation Date: 30.11.2014
 * Creation Time: 22:54
 *
 * @author eis
 */
public class DependencyInjection {
    public static Module classes(Class... classes) {
        return new Module(classes);
    }
    public static Module classes(List<Class> classes) {
        return new Module(classes);
    }
    public static Context context(Module... modules) {
        return new Context(modules);
    }

    public static Context classScanningContext(Class sourceClass) {
        return new ClassScanningContext(sourceClass);
    }
}

package fi.eis.applications.chatapp.di;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Creation Date: 30.11.2014
 * Creation Time: 22:52
 *
 * @author eis
 */
public class Context extends Module {
    private static final boolean debug = false;
    public void debugPrint(String message) {
        if (debug) {
            System.out.println(message);
        }
    }
    public void debugPrint(String message, Object... parameters) {
        if (debug) {
            System.out.printf(message, parameters);
        }
    }

    final List<Module> modules = new ArrayList<Module>();
    public Context(Module... modules) {
        Collections.addAll(this.modules, modules);
    }
    public <T> T get(Class<T> type) {
        debugPrint("context.get=" + type);
        T object = null;
        for (Module module : modules) {

            if (module.has(type)) {
                debugPrint("has type " + type);
                object = module.get(type);
                break;
            }
        }
        if (object == null) {
            throw new IllegalArgumentException("Type was not found but should: " + type);
        }
        resolveProperties(object);
        return object;
    }

    private <T> void resolveProperties(T object) {
        debugPrint("resolveProperties=" + object);
        debugPrint("class=" + object.getClass());

        // @Injected fields
        Field[] allFields = object.getClass().getDeclaredFields();

        for (Field field : allFields) {
            if(field.isAnnotationPresent(Inject.class)) {
                try {
                    boolean originallyAccessible = field.isAccessible();
                    field.setAccessible(true);
                    field.set(object, get(field.getType()));
                    if (!originallyAccessible) {
                        field.setAccessible(false);
                    }
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    }
}

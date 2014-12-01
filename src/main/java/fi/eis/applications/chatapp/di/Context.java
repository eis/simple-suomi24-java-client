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
    final List<Module> modules = new ArrayList<Module>();
    public Context(Module... modules) {
        Collections.addAll(this.modules, modules);
    }
    public <T> T get(Class<T> type) {
        System.out.println("context.get=" + type);
        T object = null;
        for (Module module : modules) {

            if (module.has(type)) {
                System.out.println("has type " + type);
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
        System.out.println("resolveProperties=" + object);
        System.out.println("class=" + object.getClass());
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

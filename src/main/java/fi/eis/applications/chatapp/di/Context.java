package fi.eis.applications.chatapp.di;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * Creation Date: 30.11.2014
 * Creation Time: 22:52
 *
 * @author eis
 */
public class Context extends Module {
    final List<Module> modules;
    public Context(Module... modules) {
        this.modules = Arrays.asList(modules);
    }
    public <T> T get(Class<T> type) {
        T object = null;
        for (Module module : modules) {

            if (module.has(type)) {
                object = module.get(type);
                break;
            }
        }
        resolveProperties(object);
        return object;
    }

    private <T> void resolveProperties(T object) {
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

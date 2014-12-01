package fi.eis.applications.chatapp.di;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Creation Date: 30.11.2014
 * Creation Time: 22:49
 *
 * @author eis
 */
public class Module {
    private static final Object NO_INSTANCE = new Object();
    private Map<Class, Object> providers = new HashMap<>();
    public Module(Class... providers) {
        for (Class clazz: providers) {
            this.providers.put(clazz, NO_INSTANCE );
        }
    }
    public Module(List<Class> providers) {
        for (Class clazz: providers) {
            this.providers.put(clazz, NO_INSTANCE );
        }
    }
    public boolean has(Class type) {
        for (Class clazz: providers.keySet()) {
            if (type.isAssignableFrom(clazz)) {
                return true;
            }
        }
        return false;
    }
    private Class getImplClassFor(Class type) {
        for (Class clazz: providers.keySet()) {
            if (type.isAssignableFrom(clazz)) {
                return clazz;
            }
        }
        throw new IllegalArgumentException("not allowed: " + type);
    }
    private <T> T getInstance(Class <T> type) {
        for (Class clazz: providers.keySet()) {
            if (type.isAssignableFrom(clazz)) {
                return (T)providers.get(clazz);
            }
        }
        throw new IllegalArgumentException("not allowed: " + type);

    }
    public <T>T get(Class<T> type) {
        if (!has(type)) {
            throw new IllegalArgumentException("not supported: " + type);
        }
        Object storedValue = getInstance(type);
        if (storedValue != NO_INSTANCE) {
            return (T)storedValue;
        } else {
            try {
                Class implClass = getImplClassFor(type);
                storedValue = implClass.newInstance();
                providers.put(implClass, storedValue);
                return (T)storedValue;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}

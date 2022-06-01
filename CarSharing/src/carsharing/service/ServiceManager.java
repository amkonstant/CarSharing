package carsharing.service;

import java.util.HashMap;
import java.util.Map;

public class ServiceManager {

    private static final Map<String, Service> services = new HashMap<>();

    private ServiceManager() {
    }

    public static <T extends Service> void load(Class<T> cls, T service) {
        services.put(cls.getSimpleName(), service);
    }

    public static <T extends Service> T getService(Class<T> cls) {
        //noinspection unchecked
        return (T) services.get(cls.getSimpleName());
    }

}

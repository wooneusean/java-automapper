package io.github.wooneusean.automapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

public class AutoMapper {
    protected static final Logger log = LogManager.getLogger();

    private static AutoMapper instance;
    private AutoMapperConfiguration config;

    private AutoMapper(AutoMapperConfiguration config) {
        setConfig(config);
    }

    private AutoMapper() {
    }

    public static void initialize(AutoMapperConfiguration config) {
        if (instance == null) {
            instance = new AutoMapper(config);
        }
    }

    public static AutoMapper getInstance() {
        if (instance == null) {
            throw new NullPointerException("AutoMapper has not been initialized yet.");
        }
        return instance;
    }

    private static Object getFieldData(Field field, Object obj) {
        boolean isAccessible = field.isAccessible();
        field.setAccessible(true);
        Object holder = null;
        try {
            holder = field.get(obj);
        } catch (IllegalAccessException e) {
            log.info("Cannot get value of field {}, maybe doesn't exist.", field.getName());
        }
        field.setAccessible(isAccessible);
        return holder;
    }

    /**
     * Attempts to map object to class. If mapping configuration is not
     * declared, default mapping will be used.
     *
     * @param victim Object to map from.
     * @param target Class to map to.
     * @param <T>    Type of object to map from.
     * @param <U>    Type of class to map to.
     * @return <code>null</code> when mapping fails.
     */
    public static <T, U> U map(T victim, Class<U> target) throws InstantiationException {
        Field[] targetFields = target.getDeclaredFields();
        U u;
        try {
            u = target.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("There is no default no-args constructor for this class. {}()", target.getName(), e);
            return null;
        }

        for (Field targetField : targetFields) {
            Field victimField = null;
            try {
                victimField = victim.getClass().getDeclaredField(targetField.getName());
            } catch (NoSuchFieldException ignored) {
                continue;
            }
            Object victimValue = getFieldData(victimField, victim);
            setFieldData(targetField, u, victimValue);
        }


        return AutoMapper.getInstance()
                         .getConfig()
                         .getDirective(new AutoMapperPair<>((Class<T>) victim.getClass(), target))
                         .getTransformer()
                         .apply(victim, u);
    }

    private static void setFieldData(Field field, Object target, Object value) {
        boolean isAccessible = field.isAccessible();
        field.setAccessible(true);

        try {
            field.set(target, value);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            log.warn("Cannot set value of field {} on {}, maybe type mismatch.",
                    field.getName(),
                    target.getClass().getName());
        }

        field.setAccessible(isAccessible);
    }

    public static <T, U> AutoMapperDirective<T, U> addMapping(Class<T> from, Class<U> to) {
        return getInstance().getConfig().addMapping(from, to);
    }

    public AutoMapperConfiguration getConfig() {
        return this.config;
    }

    public void setConfig(AutoMapperConfiguration config) {
        this.config = config;
    }
}

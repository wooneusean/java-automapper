package io.github.wooneusean.automapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

@Slf4j
@Component
public class AutoMapper {
    private AutoMapperConfiguration config = new AutoMapperConfiguration();

    public <T, U> AutoMapperDirective<T, U> addMapping(Class<T> from, Class<U> to) {
        return this.getConfig().addMapping(from, to);
    }

    private void setFieldData(Field field, Object target, Object value) {
        boolean isAccessible = field.isAccessible();
        field.setAccessible(true);

        try {
            field.set(target, value);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            log.warn("Cannot set value of field [{}] on [{}], maybe type mismatch.",
                    field.getName(),
                    target.getClass().getName());
        }

        field.setAccessible(isAccessible);
    }

    private Object getFieldData(Field field, Object obj) {
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
    public <T, U> U map(T victim, Class<U> target) {
        Field[] targetFields = target.getDeclaredFields();
        U u;
        try {
            u = target.getDeclaredConstructor().newInstance();
        } catch (InstantiationException |
                 IllegalAccessException |
                 NoSuchMethodException |
                 InvocationTargetException e) {
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


        return this.getConfig()
                   .getDirective(new AutoMapperPair<>((Class<T>) victim.getClass(), target))
                   .getTransformer()
                   .apply(victim, u);
    }

    public AutoMapperConfiguration getConfig() {
        return this.config;
    }

    public void setConfig(AutoMapperConfiguration config) {
        this.config = config;
    }
}

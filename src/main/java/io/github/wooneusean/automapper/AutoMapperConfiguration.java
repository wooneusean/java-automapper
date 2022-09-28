package io.github.wooneusean.automapper;

import java.util.HashMap;

public class AutoMapperConfiguration {
    private HashMap<AutoMapperPair<?, ?>, AutoMapperDirective<?, ?>> directiveMap = new HashMap<>();

    public <T, U> AutoMapperDirective<T, U> getDirective(AutoMapperPair<T, U> pair) {
        return (AutoMapperDirective<T, U>) directiveMap.get(pair);
    }

    /**
     * Adds mapping between two classes.
     * @param from Input class
     * @param to Output class
     * @return {@link AutoMapperDirective} instance.
     * @param <T> Type of input class
     * @param <U> Type of output class
     */
    public <T, U> AutoMapperDirective<T, U> addMapping(Class<T> from, Class<U> to) {
        AutoMapperDirective<T, U> directive = new AutoMapperDirective<>(from, to);
        directiveMap.put(new AutoMapperPair<>(from, to), directive);
        return directive;
    }
}

package io.github.wooneusean.automapper;

import java.util.HashMap;

public class AutoMapperConfiguration {
    private HashMap<AutoMapperPair<?, ?>, AutoMapperDirective<?, ?>> directiveMap = new HashMap<>();

    <T, U> AutoMapperDirective<T, U> getDirective(AutoMapperPair<T, U> pair) {
        AutoMapperDirective<T, U> directive = (AutoMapperDirective<T, U>) directiveMap.get(pair);
        if (directive == null) {
            directive = new AutoMapperDirective<>(pair).withTransformer((t, u) -> u);
        }
        return directive;
    }

    /**
     * Adds mapping between two classes.
     *
     * @param from Input class
     * @param to   Output class
     * @param <T>  Type of input class
     * @param <U>  Type of output class
     * @return {@link AutoMapperDirective} instance.
     */
    public <T, U> AutoMapperDirective<T, U> addMapping(Class<T> from, Class<U> to) {
        AutoMapperDirective<T, U> directive = new AutoMapperDirective<>(from, to);
        directiveMap.put(new AutoMapperPair<>(from, to), directive);
        return directive;
    }
}

package io.github.wooneusean.automapper;

import java.util.function.BiFunction;

public class AutoMapperDirective<T, U> {
    private Class<T> from;
    private Class<U> to;
    private BiFunction<T, U, U> transformer;

    public AutoMapperDirective(Class<T> from, Class<U> to) {
        this.from = from;
        this.to = to;
    }

    public AutoMapperDirective(AutoMapperPair<T, U> pair) {
        this.from = pair.getFirst();
        this.to = pair.getSecond();
    }

    @Override
    public int hashCode() {
        int result = 17;
        if (to != null) {
            result = 31 * result + to.hashCode();
        }
        if (from != null) {
            result = 31 * result + from.hashCode();
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof AutoMapperDirective))
            return false;
        AutoMapperDirective<?, ?> other = (AutoMapperDirective<?, ?>) obj;
        return (this.to == other.to) && (this.from == other.from);
    }

    BiFunction<T, U, U> getTransformer() {
        return transformer;
    }

    void setTransformer(BiFunction<T, U, U> transformer) {
        this.transformer = transformer;
    }

    /**
     * Adds transformer function that will mutate the object
     * after it has been automatically mapped by default mapper.
     *
     * @param transformer Lambda function that will describe mutations to be done on mapped object.
     * @return {@link AutoMapperDirective} instance.
     */
    public AutoMapperDirective<T, U> withTransformer(BiFunction<T, U, U> transformer) {
        this.transformer = transformer;
        return this;
    }

    Class<T> getFrom() {
        return from;
    }

    void setFrom(Class<T> from) {
        this.from = from;
    }

    Class<U> getTo() {
        return to;
    }

    void setTo(Class<U> to) {
        this.to = to;
    }
}

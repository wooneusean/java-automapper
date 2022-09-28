package io.github.wooneusean.automapper;

public class AutoMapperPair<T, U> {
    private Class<T> first;
    private Class<U> second;

    public AutoMapperPair(Class<T> first, Class<U> second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int hashCode() {
        int result = 17;
        if (first != null) {
            result = 31 * result + first.hashCode();
        }
        if (second != null) {
            result = 31 * result + second.hashCode();
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof AutoMapperPair))
            return false;
        AutoMapperPair<?, ?> other = (AutoMapperPair<?, ?>) obj;
        return (this.first == other.first) && (this.second == other.second);
    }

    public Class<T> getFirst() {
        return first;
    }

    public void setFirst(Class<T> first) {
        this.first = first;
    }

    public Class<U> getSecond() {
        return second;
    }

    public void setSecond(Class<U> second) {
        this.second = second;
    }
}

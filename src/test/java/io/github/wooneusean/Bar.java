package io.github.wooneusean;

public class Bar {
    Integer a;
    String b;
    Integer c;

    public Bar() {
        // For AutoMapper to work.
    }

    @Override
    public String toString() {
        return "Bar{" +
               "a=" + a +
               ", b='" + b + '\'' +
               ", c=" + c +
               '}';
    }

}

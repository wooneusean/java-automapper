package io.github.wooneusean.demo;

public class Bar {
    int a;
    String b;
    int c;

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

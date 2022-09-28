package io.github.wooneusean;

public class Foo {
    String a;
    boolean b;
    int c;

    public Foo(String a, boolean b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Foo() {
        // For AutoMapper to work
    }

    @Override
    public String toString() {
        return "Foo{" +
               "a='" + a + '\'' +
               ", b=" + b +
               ", c=" + c +
               '}';
    }

}

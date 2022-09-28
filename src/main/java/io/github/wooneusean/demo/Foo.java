package io.github.wooneusean.demo;

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

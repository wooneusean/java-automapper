package io.github.wooneusean.demo;

import io.github.wooneusean.automapper.AutoMapper;
import io.github.wooneusean.automapper.AutoMapperConfiguration;

public class Main {
    public static void main(String[] args) {
        AutoMapperConfiguration mapperConfig = new AutoMapperConfiguration();
        // addMapping() creates an AutoMapperDirective, optional to add,
        // by default will run default mapper
        mapperConfig.addMapping(Foo.class, Bar.class)
                    .withTransformer((foo, bar) -> {        // foo will be automatically mapped to bar
                        bar.a = Integer.parseInt(foo.a);    // then this transformer will be run.
                        bar.b = foo.b ? "True" : "False";
                        return bar;
                    });
        mapperConfig.addMapping(Bar.class, Foo.class)
                    .withTransformer((bar, foo) -> {
                        foo.a = bar.a + " mapped";
                        foo.b = bar.b.equalsIgnoreCase("True");
                        foo.c = bar.c - (bar.c - 420);
                        return foo;
                    });

        AutoMapper autoMapper = new AutoMapper(mapperConfig);
        Foo foo = new Foo("12", true, 1337);
        Bar bar = autoMapper.map(foo, Bar.class);
        System.out.println(bar);
        Foo newFoo = autoMapper.map(bar, Foo.class);
        System.out.println(newFoo);
    }
}

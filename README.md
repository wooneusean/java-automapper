# Java AutoMapper
AutoMapper for Java inspired by [AutoMapper for C#](https://github.com/AutoMapper/AutoMapper)

## Demo
View full demo [here](https://github.com/wooneusean/java-automapper/blob/main/src/main/java/io/github/wooneusean/demo/Main.java)

```java
 public class Main {
    public static void main(String[] args) {
        AutoMapperConfiguration mapperConfig = new AutoMapperConfiguration();
        // addMapping() creates an AutoMapperDirective, 
        // optional because by default will run default 
        // mapper which maps by similar field name
        mapperConfig.addMapping(Foo.class, Bar.class)
                    // foo will be automatically mapped to bar
                    // with default mapper, then this 
                    // transformer will be run.
                    .withTransformer((foo, bar) -> {
                        bar.a = Integer.parseInt(foo.a);
                        bar.b = foo.b ? "True" : "False";
                        
                        // No need explicit mapping for fields of same type
                        // bar.c = foo.c
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
        // Class can have constructor with args but in order for
        // the automapper to work, the class also needs a no-args
        // constructor.
        Foo foo = new Foo("12", true, 1337);
        Bar bar = autoMapper.map(foo, Bar.class);
        System.out.println(bar); 
        // Bar{a=12, b='True', c=1337}
        Foo newFoo = autoMapper.map(bar, Foo.class);
        System.out.println(newFoo); 
        // Foo{a='12 mapped', b=true, c=420}
    }
}
```
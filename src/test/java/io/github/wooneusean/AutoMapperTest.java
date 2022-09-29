package io.github.wooneusean;

import io.github.wooneusean.automapper.AutoMapper;
import io.github.wooneusean.automapper.AutoMapperConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AutoMapperTest {
    private static final Logger log = LogManager.getLogger();

    @BeforeAll
    static void initialize_automapper() {
        AutoMapperConfiguration config = new AutoMapperConfiguration();
        AutoMapper.initialize(config);
    }

    @Test
    void can_map_fields_with_same_type_and_name() {
        Foo foo = new Foo("12", false, 1337);
        try {
            Bar bar = AutoMapper.map(foo, Bar.class);
            Assertions.assertNotNull(bar);
            assertEquals(foo.c, bar.c);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void only_defined_transformer_works() {
        Foo foo = new Foo("12", false, 1337);
        AutoMapper.addMapping(Foo.class, Bar.class)
                  .withTransformer((foof, bar) -> {
                      bar.a = Integer.parseInt(foof.a);
                      return bar;
                  });
        try {
            Bar bar = AutoMapper.map(foo, Bar.class);
            assertNotNull(bar);
            assertEquals(Integer.parseInt(foo.a), bar.a);
            assertNull(bar.b);
            assertEquals(foo.c, bar.c);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void can_map_back_and_forth() {
        Foo foo = new Foo("12", false, 1337);
        AutoMapper.addMapping(Foo.class, Bar.class)
                  .withTransformer((foof, bar) -> {
                      bar.a = Integer.parseInt(foof.a);
                      bar.b = foof.b ? "True" : "False";
                      return bar;
                  });

        AutoMapper.addMapping(Bar.class, Foo.class)
                  .withTransformer((bar, foof) -> {
                      foof.a = bar.a + "";
                      foof.b = bar.b.equalsIgnoreCase("True");
                      return foof;
                  });

        try {
            Bar bar = AutoMapper.map(foo, Bar.class);
            Foo fooBar = AutoMapper.map(bar, Foo.class);
            assertNotNull(fooBar);
            assertEquals(fooBar.a, foo.a);
            assertEquals(fooBar.b, foo.b);
            assertEquals(fooBar.c, foo.c);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}

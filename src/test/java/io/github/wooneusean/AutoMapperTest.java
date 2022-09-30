package io.github.wooneusean;

import io.github.wooneusean.automapper.AutoMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class AutoMapperTest {
    @Autowired
    AutoMapper autoMapper;

    @Test
    void can_map_fields_with_same_type_and_name() {
        Foo foo = new Foo("12", false, 1337);
        try {
            Bar bar = autoMapper.map(foo, Bar.class);
            assertNotNull(bar);
            assertEquals(foo.c, bar.c);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void only_defined_transformer_works() {
        Foo foo = new Foo("12", false, 1337);
        autoMapper.addMapping(Foo.class, Bar.class)
                  .withTransformer((foof, bar) -> {
                      bar.a = Integer.parseInt(foof.a);
                      return bar;
                  });
        try {
            Bar bar = autoMapper.map(foo, Bar.class);
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
        autoMapper.addMapping(Foo.class, Bar.class)
                  .withTransformer((foof, bar) -> {
                      bar.a = Integer.parseInt(foof.a);
                      bar.b = foof.b ? "True" : "False";
                      return bar;
                  });

        autoMapper.addMapping(Bar.class, Foo.class)
                  .withTransformer((bar, foof) -> {
                      foof.a = bar.a + "";
                      foof.b = bar.b.equalsIgnoreCase("True");
                      return foof;
                  });

        try {
            Bar bar = autoMapper.map(foo, Bar.class);
            Foo fooBar = autoMapper.map(bar, Foo.class);
            assertNotNull(fooBar);
            assertEquals(fooBar.a, foo.a);
            assertEquals(fooBar.b, foo.b);
            assertEquals(fooBar.c, foo.c);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}

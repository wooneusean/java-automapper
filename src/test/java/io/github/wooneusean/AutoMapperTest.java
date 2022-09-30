package io.github.wooneusean;

import io.github.wooneusean.automapper.AutoMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
class AutoMapperTest {
    @Autowired
    AutoMapper autoMapper;

    @Test
    void can_map_fields_with_same_type_and_name() {
        Foo foo = new Foo("12", false, 1337);
        Bar bar = autoMapper.map(foo, Bar.class);
        assertThat(bar).isNotNull();
        assertThat(foo.c).isEqualTo(bar.c);
    }

    @Test
    void only_defined_transformer_works() {
        Foo foo = new Foo("12", false, 1337);
        autoMapper.addMapping(Foo.class, Bar.class)
                  .withTransformer((foof, bar) -> {
                      bar.a = Integer.parseInt(foof.a);
                      return bar;
                  });
        Bar bar = autoMapper.map(foo, Bar.class);
        assertThat(bar).isNotNull();
        assertThat(Integer.parseInt(foo.a)).isEqualTo(bar.a);
        assertThat(bar.b).isNull();
        assertThat(foo.c).isEqualTo(bar.c);
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

        Bar bar = autoMapper.map(foo, Bar.class);
        Foo fooBar = autoMapper.map(bar, Foo.class);
        assertThat(fooBar).isNotNull();
        assertThat(fooBar)
                .usingRecursiveComparison()
                .isEqualTo(foo);
    }
}

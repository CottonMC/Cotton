package io.github.cottonmc.cotton.config.annotations;

import java.lang.annotation.*;

@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
public @interface ConfigFile {
    String name();
}

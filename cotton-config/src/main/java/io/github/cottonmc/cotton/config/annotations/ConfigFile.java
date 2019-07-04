package io.github.cottonmc.cotton.config.annotations;

import io.github.cottonmc.cotton.config.ConfigManager;

import java.lang.annotation.*;

@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
public @interface ConfigFile {
    String name();
    String extension() default ConfigManager.DEFAULT_EXTENSION;
}

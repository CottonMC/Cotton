package io.github.cottonmc.cotton.util.config;

import blue.endless.jankson.Comment;
import io.github.cottonmc.cotton.util.config.annotations.ConfigFile;

@ConfigFile(name="CottonConfig")
public class CottonConfig {


    public String someString = "sada";

    @Comment(value="A comment describing number1.")
    public int number1 = 8; //this is new 2


}

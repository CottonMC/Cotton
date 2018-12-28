package io.github.cottonmc.cotton.config;

import blue.endless.jankson.Comment;
import io.github.cottonmc.cotton.config.annotations.ConfigFile;

import java.util.ArrayList;
import java.util.List;

@ConfigFile(name="CottonConfig")
public class CottonConfig {

    public int number1 = 8;

    @Comment(value="A list of mod ids, in order of preference for resource loading.")
    public List<String> namespacePreferenceOrder = new ArrayList<>();

}

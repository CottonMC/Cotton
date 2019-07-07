package io.github.cottonmc.cotton.datapack.config;

import blue.endless.jankson.Comment;
import io.github.cottonmc.cotton.config.annotations.ConfigFile;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

@ConfigFile(name="CottonDatapack")
public class CottonDatapackConfig {
    @Comment("Specifies specific recipes to remove from the game.")
    public ArrayList<String> removeRecipesByIdentifier = new ArrayList<>();

    @Comment("Item identifiers specified here will prevent any recipe for one of that item.")
    public ArrayList<ItemStack> removeRecipesByItem = new ArrayList<>();
}
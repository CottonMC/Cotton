package io.github.cottonmc.cotton.mixins;

import com.google.common.collect.BiMap;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.SimpleRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SimpleRegistry.class)
public interface MixinSimpleRegistry<T> {

    @Accessor
    public BiMap<Identifier, T> getEntries();

}

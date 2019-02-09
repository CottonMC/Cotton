package io.github.cottonmc.cotton.mixin;

import io.github.cottonmc.cotton.tweaks.PlaceBlockBehavior;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.DispenserBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(DispenserBlock.class)
public abstract class DispenserBlockMixin {

    /**
     * This is used to replace the default behavior with block placing.
     *
     * @author Juuz
     */
    // method_10008 is the lambda in the static initializer
    @Overwrite
    private static void method_10008(Object2ObjectOpenHashMap map) {
        map.defaultReturnValue(new PlaceBlockBehavior());
    }
}

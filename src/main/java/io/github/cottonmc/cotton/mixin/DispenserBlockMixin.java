package io.github.cottonmc.cotton.mixin;

import io.github.cottonmc.cotton.tweaks.PlaceBlockBehavior;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.item.Item;
import net.minecraft.util.SystemUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(DispenserBlock.class)
public abstract class DispenserBlockMixin {

    @Mutable
    @Shadow @Final
    private static Map<Item, DispenserBehavior> BEHAVIORS = SystemUtil.consume(new Object2ObjectOpenHashMap<>(), (object2ObjectOpenHashMap_1) -> {
        object2ObjectOpenHashMap_1.defaultReturnValue(new PlaceBlockBehavior());
    });
}

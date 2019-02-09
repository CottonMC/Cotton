package io.github.cottonmc.cotton.mixin;

import io.github.cottonmc.cotton.tweaks.PlaceBlockBehavior;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(DispenserBlock.class)
public abstract class DispenserBlockMixin {

    @Shadow @Final private static Map<Item, DispenserBehavior> BEHAVIORS;

    /**
     *
     * This is used to place a block if no other behavior is found.
     *
     * @author Pannoniae
     */
    @Overwrite
    public DispenserBehavior getBehaviorForItem(ItemStack itemStack) {

        if (BEHAVIORS.containsKey(itemStack.getItem())) {
            return BEHAVIORS.get(itemStack.getItem());
        }
        else {
            return new PlaceBlockBehavior();
        }

    }
}

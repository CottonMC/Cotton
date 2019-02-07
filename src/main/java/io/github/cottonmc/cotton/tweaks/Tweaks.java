package io.github.cottonmc.cotton.tweaks;

import io.github.cottonmc.cotton.Cotton;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Tweaks {

    public static void initialize() {

        Cotton.logger.info("Tweaks initialized.");
        DispenserBehavior behavior = (blockPointer, itemStack) -> {
            World world = blockPointer.getWorld();
            BlockPos pos = blockPointer.getBlockPos();
            BlockPos target = pos.offset(blockPointer.getBlockState().get(DispenserBlock.FACING));
            if (world.isAir(target) && Blocks.WHEAT.getDefaultState().canPlaceAt(world, target)) {
                world.setBlockState(target, Blocks.WHEAT.getDefaultState(), 11);
                world.playSound(null, target, SoundEvents.ITEM_CROP_PLANT, SoundCategory.BLOCK, 1.0F, 1.0F);
            }
            itemStack.subtractAmount(1);
            return itemStack;
        };
        DispenserBlock.registerBehavior(() -> Items.WHEAT_SEEDS, behavior);

    }

}

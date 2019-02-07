package io.github.cottonmc.cotton.tweaks;

import io.github.cottonmc.cotton.Cotton;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Tweaks {
    public static DispenserBehavior PLANTING_BEHAVIOR = (blockPointer, itemStack) -> {
        World world = blockPointer.getWorld();
        BlockPos pos = blockPointer.getBlockPos();
        BlockPos target = pos.offset(blockPointer.getBlockState().get(DispenserBlock.FACING)).offset(Direction.DOWN);
        itemStack.useOnBlock(new DispenserUsageContext(world, null, itemStack, new BlockHitResult(new Vec3d(0.5, 0.5, 0.5), Direction.UP, target, true)));
        itemStack.subtractAmount(1);
        return itemStack;
    };

    public static void initialize() {

        Cotton.logger.info("Tweaks initialized.");

        if (Cotton.config.includeTweaks) {
            DispenserBlock.registerBehavior(() -> Items.WHEAT_SEEDS, PLANTING_BEHAVIOR);
            DispenserBlock.registerBehavior(() -> Items.MELON_SEEDS, PLANTING_BEHAVIOR);
            DispenserBlock.registerBehavior(() -> Items.PUMPKIN_SEEDS, PLANTING_BEHAVIOR);
            DispenserBlock.registerBehavior(() -> Items.CARROT, PLANTING_BEHAVIOR);
            DispenserBlock.registerBehavior(() -> Items.POTATO, PLANTING_BEHAVIOR);
            DispenserBlock.registerBehavior(() -> Items.NETHER_WART, PLANTING_BEHAVIOR);
            DispenserBlock.registerBehavior(() -> Items.BEETROOT_SEEDS, PLANTING_BEHAVIOR);
            DispenserBlock.registerBehavior(() -> Items.SWEET_BERRIES, PLANTING_BEHAVIOR);
        }

    }

}

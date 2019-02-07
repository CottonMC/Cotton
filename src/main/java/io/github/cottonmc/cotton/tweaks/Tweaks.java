package io.github.cottonmc.cotton.tweaks;

import io.github.cottonmc.cotton.Cotton;
import io.github.cottonmc.cotton.config.CottonConfig;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.item.FoodCropItem;
import net.minecraft.item.Item;
import net.minecraft.item.SeedsItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class Tweaks {

    private static void registerDispenserSeedPlanting() {
        DispenserBehavior PLANTING_BEHAVIOR = (blockPointer, itemStack) -> {
            World world = blockPointer.getWorld();
            BlockPos pos = blockPointer.getBlockPos();
            BlockPos target = pos.offset(blockPointer.getBlockState().get(DispenserBlock.FACING)).offset(Direction.DOWN);
            itemStack.useOnBlock(new DispenserUsageContext(world, null, itemStack, new BlockHitResult(new Vec3d(0.5, 0.5, 0.5), Direction.UP, target, true)));
            itemStack.subtractAmount(1);
            return itemStack;
        };
        for (Item item : Registry.ITEM) {
            if (item instanceof SeedsItem || item instanceof FoodCropItem) {
                DispenserBlock.registerBehavior(item, PLANTING_BEHAVIOR);
            }
        }
        }

    public static void initialize() {
        if (Cotton.config.include_tweaks) {
            if (CottonConfig.enable_dispenser_tweaks) {
                registerDispenserSeedPlanting();
            }
            Cotton.logger.info("Tweaks initialized.");
        }
    }

}

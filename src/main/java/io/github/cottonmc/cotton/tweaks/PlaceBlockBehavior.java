package io.github.cottonmc.cotton.tweaks;

import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.item.block.BlockItem;
import net.minecraft.item.block.WallStandingBlockItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PlaceBlockBehavior implements DispenserBehavior {
    private static final ItemDispenserBehavior DEFAULT_BEHAVIOR = new ItemDispenserBehavior();

    @Override
    public ItemStack dispense(BlockPointer blockPointer, ItemStack itemStack) {
        if (itemStack.getItem() instanceof BlockItem) {
            BlockItem item = (BlockItem)itemStack.getItem();
            World world = blockPointer.getWorld();
            Direction facing = blockPointer.getBlockState().get(DispenserBlock.FACING);
            BlockPos target = blockPointer.getBlockPos().offset(facing);

            // Swap direction for WallStandingBlockItems (like banners),
            // so they can be placed on the dispenser
            if (item instanceof WallStandingBlockItem && facing == Direction.UP) {
                facing = Direction.DOWN;
            }

            DispenserPlacementContext placementContext = new DispenserPlacementContext(
                world, itemStack, new BlockHitResult(new Vec3d(0.5, 0.5, 0.5), facing, target, true)
            );
            BlockState placementState = item.getBlock().getPlacementState(placementContext);
            boolean replaceable = world.getBlockState(target).getMaterial().isReplaceable();

            if (placementState != null && replaceable && placementState.canPlaceAt(world, target)) {
                // Calling breakBlock separately here to drop the broken block,
                // even though the block would be broken by BlockItem.place.
                // Note: this does nothing to air and will only be done to replaceable blocks
                world.breakBlock(target, true);
                if (item.place(placementContext) == ActionResult.SUCCESS) {
                    return itemStack;
                }
            }
        }

        return DEFAULT_BEHAVIOR.dispense(blockPointer, itemStack);
    }
}

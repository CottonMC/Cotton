package io.github.cottonmc.cotton.tweaks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.item.block.BlockItem;
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
            BlockState placementState = item.getBlock().getPlacementState(new DispenserPlacementContext(
                world, itemStack, new BlockHitResult(new Vec3d(0.5, 0.5, 0.5), facing, target, true)
            ));

            boolean replaceable = world.getBlockState(target).getMaterial().isReplaceable();

            if (placementState != null && replaceable && placementState.canPlaceAt(world, target)) {
                if (!world.isAir(target)) {
                    world.breakBlock(target, true);
                }

                world.setBlockState(target, placementState);
                itemStack.subtractAmount(1);

                // This uses the breaking world event. Placing sounds shouldn't be too different?
                world.fireWorldEvent(2001, target, Block.getRawIdFromState(placementState));
                return itemStack;
            }
        }

        return DEFAULT_BEHAVIOR.dispense(blockPointer, itemStack);
    }
}

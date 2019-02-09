package io.github.cottonmc.cotton.tweaks;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.block.BlockItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PlaceBlockBehavior implements DispenserBehavior {

    @Override
    public ItemStack dispense(BlockPointer blockPointer, ItemStack itemStack) {
        if (itemStack.getItem() instanceof BlockItem) {
            BlockItem item = (BlockItem)itemStack.getItem();
            World world = blockPointer.getWorld();
            Direction facing = blockPointer.getBlockState().get(DispenserBlock.FACING);
            BlockPos target = blockPointer.getBlockPos().offset(facing);
            world.setBlockState(target, item.getBlock().getPlacementState(new ItemPlacementContext(new DispenserUsageContext(
                world, itemStack, new BlockHitResult(new Vec3d(0.5, 0.5, 0.5), facing.getOpposite(), target, true)
            ))));
            itemStack.subtractAmount(1);
        }
        return itemStack;
    }
}
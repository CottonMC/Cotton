package io.github.cottonmc.cotton.tweaks;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.item.block.BlockItem;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlaceBlockBehavior implements DispenserBehavior {

    @Override
    public ItemStack dispense(BlockPointer blockPointer, ItemStack itemStack) {
        if (itemStack.getItem() instanceof BlockItem) {
            BlockItem item = (BlockItem)itemStack.getItem();
            World world = blockPointer.getWorld();
            BlockPos target = blockPointer.getBlockPos().offset(blockPointer.getBlockState().get(DispenserBlock.FACING));
            world.setBlockState(target, item.getBlock().getDefaultState());
            itemStack.subtractAmount(1);
        }
        return itemStack;
    }
}

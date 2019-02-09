package io.github.cottonmc.cotton.tweaks;

import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class DispenserPlacementContext extends ItemPlacementContext {
    protected DispenserPlacementContext(World world, ItemStack stack, BlockHitResult hitResult) {
        super(world, null, stack, hitResult);
    }

    @Override
    public Direction getPlayerFacing() {
        return hitResult.getSide();
    }

    @Override
    public Direction[] getPlacementFacings() {
        return new Direction[] { getPlayerFacing() };
    }
}

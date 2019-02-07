package io.github.cottonmc.cotton.tweaks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class DispenserUsageContext extends ItemUsageContext {

	protected DispenserUsageContext(World world_1, PlayerEntity playerEntity_1, ItemStack itemStack_1, BlockHitResult blockHitResult_1) {
		super(world_1, playerEntity_1, itemStack_1, blockHitResult_1);
	}
}

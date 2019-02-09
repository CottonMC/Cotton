package io.github.cottonmc.cotton.tweaks;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class DispenserUsageContext extends ItemUsageContext {

	protected DispenserUsageContext(World world, ItemStack itemStack, BlockHitResult blockHitResult) {
		super(world, null, itemStack, blockHitResult);
	}
}

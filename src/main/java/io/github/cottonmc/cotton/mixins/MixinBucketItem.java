package io.github.cottonmc.cotton.mixins;

import io.github.cottonmc.cotton.impl.BucketFluidAccessor;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Used for cauldrons.
 */
@Mixin(BucketItem.class)
public class MixinBucketItem implements BucketFluidAccessor {
	@Shadow @Final
	private Fluid fluid;

	@Override
	public Fluid cotton_getFluid() {
		return fluid;
	}
}

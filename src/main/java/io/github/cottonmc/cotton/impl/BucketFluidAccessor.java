package io.github.cottonmc.cotton.impl;

import net.minecraft.fluid.Fluid;

/**
 * internal interface to access the fluid of a bucket
 */
public interface BucketFluidAccessor {
	Fluid cotton_getFluid();
}

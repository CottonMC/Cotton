package io.github.cottonmc.cotton.cauldron.tweaker;

import io.github.cottonmc.cotton.cauldron.CauldronContext;
import io.github.cottonmc.libcd.api.CDLogger;
import io.github.cottonmc.libcd.api.util.StackInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;


public class WrappedCauldronContext {
	private CauldronContext context;
	private CDLogger logger;

	public WrappedCauldronContext(CDLogger logger, CauldronContext context) {
		this.context = context;
		this.logger = logger;
	}

	public int getLevel() {
		return context.getLevel();
	}

	public StackInfo getStack() {
		StackInfo info = new StackInfo(context.getStack());
		return new StackInfo(context.getStack());
	}

	public String getHand() {
		if (context.getHand().equals(Hand.MAIN_HAND)) return "main";
		else return "off";
	}

	public String getFluid() {
		return Registry.FLUID.getId(context.getFluid()).toString();
	}

	public StackInfo[] getPreviousItems() {
		List<StackInfo> prev = new ArrayList<>();
		for (ItemStack stack : context.getPreviousItems()) {
			if (!stack.isEmpty()) prev.add(new StackInfo(stack));
		}
		return prev.toArray(new StackInfo[0]);
	}

	public boolean hasFireUnder() {
		return context.hasFireUnder();
	}

	public CauldronContext getContext() {
		return context;
	}

	public CDLogger getLogger() {
		return logger;
	}
}

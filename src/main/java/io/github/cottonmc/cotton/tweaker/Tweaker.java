package io.github.cottonmc.cotton.tweaker;

import net.minecraft.resource.ResourceManager;

import java.util.ArrayList;
import java.util.List;

public interface Tweaker {
	List<Tweaker> TWEAKERS = new ArrayList<>();

	/**
	 * Add a new tweaker to store data in.
	 * @param tweaker an instanceof Tweaker to call whenever reloading.
	 */
	static void addTweaker(Tweaker tweaker) {
		TWEAKERS.add(tweaker);
	}

	/**
	 * Called whenever the /reload command is run, before scripts are applied.
	 * Use this time to empty out any lists or maps you need to.
	 * @param manager The ResourceManager reloading tweakers.
	 */
	void prepareReload(ResourceManager manager);
}

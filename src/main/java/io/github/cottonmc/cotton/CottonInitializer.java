package io.github.cottonmc.cotton;

/**
 * Called after standard entrypoints are loaded and vanilla registries are populated,
 * so you can register any additional blocks or items based on what common materials exist.
 * This interface will be removed with changes coming to Fabric for Minecraft 1.15.
 * Add entrypoints under the category "cotton".
 */
public interface CottonInitializer {
	void initialize();
}

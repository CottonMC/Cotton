package io.github.cottonmc.cotton.cauldron.tweaker;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.github.cottonmc.cotton.cauldron.CauldronBehavior;
import io.github.cottonmc.cotton.cauldron.CauldronContext;
import io.github.cottonmc.libdp.api.Diskette;
import io.github.cottonmc.libdp.api.driver.Driver;
import net.minecraft.fluid.Fluid;
import net.minecraft.resource.ResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.Predicate;

//TODO: improve system
public class CauldronTweaker implements Driver {
	public final Map<Predicate<CauldronContext>, CauldronBehavior> behaviors = new HashMap<>();
	public static final CauldronTweaker INSTANCE = new CauldronTweaker();
	private Logger logger = LogManager.getLogger();
	private JsonObject debug;
	private Diskette currentBridge;

	/**
	 * Used during data pack loading to clear the behavior list.
	 * DO NOT CALL THIS YOURSELF, EVER. IT WILL WIPE ALL DATA PACK BEHAVIORS.
	 */
	public void prepareReload(ResourceManager manager) {
		INSTANCE.behaviors.clear();
		debug = new JsonObject();
		currentBridge = null;
	}

	@Override
	public void applyReload(ResourceManager resourceManager, Executor executor) {
	}

	@Override
	public String getApplyMessage() {
		return behaviors.size() + " cauldron " + (behaviors.size() == 1 ? "behavior" : "behaviors");
	}

	@Override
	public void prepareFor(Diskette bridge) {
		this.logger = LogManager.getLogger(bridge.getId().getNamespace());
		this.currentBridge = bridge;
	}

	@Override
	public JsonObject getDebugInfo() {
		return debug;
	}

	/**
	 * Register a new cauldron behavior.
	 * References functions defined in the same script as this is called.
	 *
	 * @param testName The name of the function used to test whether this behavior should run. Passed one argument of type {@link WrappedCauldronContext}.
	 * @param runName  The name of the function used to perform this behavior. Passed one argument of type {@link WrappedCauldronContext}.
	 */
	public void registerBehavior(String testName, String runName) {
		registerBehavior(currentBridge, testName, runName);
	}

	/**
	 * Register a new cauldron behavior.
	 * References functions defined in the passed ScriptBridge instead of the one for the current script.
	 *
	 * @param bridge   The ScriptBridge passed in the `libcd` variable, or the ScriptBridge of another script if the functions are in another script bridge.
	 * @param testName The name of the function used to test whether this behavior should run. Passed one argument of type {@link WrappedCauldronContext}.
	 * @param runName  The name of the function used to perform this behavior. Passed one argument of type {@link WrappedCauldronContext}.
	 */
	public void registerBehavior(Diskette bridge, String testName, String runName) {
		ScriptEngine engine = bridge.getEngine();
		if (engine instanceof Invocable) {
			String key = bridge.getId().toString();
			if (!debug.has(key)) {
				debug.add(key, new JsonArray());
			}
			JsonArray array = (JsonArray) debug.get(key);
			JsonObject toAdd = new JsonObject();
			toAdd.add("test_func", new JsonPrimitive(testName));
			toAdd.add("run_func", new JsonPrimitive(runName));
			array.add(toAdd);
			INSTANCE.behaviors.put(new ScriptedPredicate(bridge.getId(), (Invocable) engine, testName), new ScriptedBehavior(bridge.getId(), (Invocable) engine, runName));
		} else {
			logger.error("Cannot register cauldron behaviors using this language, the engine must be able to invoke functions in scripts");
		}
	}


	/**
	 * Register a new cauldron behavior. Deprecated; use {@link CauldronTweaker#registerBehavior(Diskette, String, String)} instead.
	 * Construct new classes and pass them functions for these. Each are passed a {@link CauldronContext}.
	 *
	 * @param context  A predicate for under what conditions the behavior should be performed.
	 * @param behavior The behavior to perform when the conditions are met.
	 */
	@Deprecated
	public void registerBehavior(Predicate<CauldronContext> context, CauldronBehavior behavior) {
		int deprecated = debug.get("deprecated_behaviors").getAsInt();
		debug.add("deprecated_behaviors", new JsonPrimitive(deprecated + 1));
		INSTANCE.behaviors.put(context, behavior);
	}

	/**
	 * Drain a cauldron of its fluid (for ease of calling from scripts). Depreacated; use {@link WrappedCauldronContext#drain} instead.
	 *
	 * @param context The {@link CauldronContext} being used, wrapped for use outside of obf.
	 * @param bottles How many bottles of fluid to drain.
	 * @return Whether the drain was successful.
	 */
	@Deprecated
	public boolean drainCauldron(WrappedCauldronContext context, int bottles) {
		return context.drain(bottles);
	}

	/**
	 * Fill a cauldron with a given fluid (for ease of calling from scripts). Deprecated; use {@link WrappedCauldronContext#fill} instead.
	 *
	 * @param context The {@link CauldronContext} being used, wrapped for use outside of obf.
	 * @param fluid   What fluid to fill with.
	 * @param bottles How many bottles of fluid to fill.
	 * @return Whether the fill was successful.
	 */
	@Deprecated
	public boolean fillCaudron(WrappedCauldronContext context, Fluid fluid, int bottles) {
		return context.fill(fluid, bottles);
	}

	/**
	 * Take items from the stack used in a cauldron behavior. Deprecated; use {@link WrappedCauldronContext#takeItem} instead.
	 *
	 * @param context The {@link CauldronContext} being used, wrapped for use outside of obf.
	 * @param amount  How many items to take.
	 * @return Whether the items could be taken.
	 */
	@Deprecated
	public boolean takeItem(WrappedCauldronContext context, int amount) {
		return context.takeItem(amount);
	}

	/**
	 * Give items to a player using a cauldron behavior, or drop them on the ground if there's no player. Deprecated; use {@link WrappedCauldronContext#giveItem} instead.
	 * Items dropped will have the "NoCauldronCollect" scoreboard tag, which prevents in-spec cauldrons from picking them up hopper-style.
	 *
	 * @param context The {@link CauldronContext} being used.
	 * @param stack   The item stack to give.
	 * @return Whether the items could be given.
	 */
	@Deprecated
	public boolean giveItem(WrappedCauldronContext context, Object stack) {
		return context.giveItem(stack);
	}

	/**
	 * Play a sound effect from the cauldron. Deprecated; use {@link WrappedCauldronContext#playSound} instead.
	 *
	 * @param context The {@link CauldronContext} being used, wrapped for use outside of obf.
	 * @param sound   The ID of the sound to play.
	 * @param volume  The volume to play at.
	 * @param pitch   The pitch to play at.
	 */
	@Deprecated
	public void playSound(WrappedCauldronContext context, String sound, float volume, float pitch) {
		context.playSound(sound, volume, pitch);
	}

	/**
	 * Spawn an entity above the cauldron. Deprecated; use {@link WrappedCauldronContext#spawnEntity} instead.
	 *
	 * @param context  The {@link CauldronContext} being used, wrapped for use outside of obf.
	 * @param typeName The ID of the type of entity to spawn.
	 */
	@Deprecated
	public void spawnEntity(WrappedCauldronContext context, String typeName) {
		context.spawnEntity(typeName);
	}

}

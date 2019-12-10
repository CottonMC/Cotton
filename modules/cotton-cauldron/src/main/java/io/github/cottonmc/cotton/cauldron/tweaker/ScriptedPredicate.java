package io.github.cottonmc.cotton.cauldron.tweaker;

import io.github.cottonmc.cotton.cauldron.CauldronContext;
import io.github.cottonmc.libcd.api.CDLogger;
import net.minecraft.util.Identifier;

import javax.script.Invocable;
import javax.script.ScriptException;
import java.util.function.Predicate;

public class ScriptedPredicate implements Predicate<CauldronContext> {
	private Invocable engine;
	private String funcName;
	private CDLogger logger;

	public ScriptedPredicate(Identifier scriptName, Invocable engine, String funcName) {
		this.engine = engine;
		this.funcName = funcName;
		this.logger = new CDLogger(scriptName.getNamespace());
	}

	@Override
	public boolean test(CauldronContext ctx) {
		try {
			return (Boolean) engine.invokeFunction(funcName, new WrappedCauldronContext(logger, ctx));
		} catch (NoSuchMethodException | ScriptException e) {
			logger.error("Error processing cauldron tweaker predicate: " + e.getMessage());
			return false;
		}
	}
}

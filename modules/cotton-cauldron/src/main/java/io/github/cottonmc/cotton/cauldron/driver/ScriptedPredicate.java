package io.github.cottonmc.cotton.cauldron.driver;

import io.github.cottonmc.cotton.cauldron.CauldronContext;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.script.Invocable;
import javax.script.ScriptException;
import java.util.function.Predicate;

public class ScriptedPredicate implements Predicate<CauldronContext> {
	private Invocable engine;
	private String funcName;
	private Logger logger;

	public ScriptedPredicate(Identifier scriptName, Invocable engine, String funcName) {
		this.engine = engine;
		this.funcName = funcName;
		this.logger = LogManager.getLogger(scriptName.getNamespace());
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

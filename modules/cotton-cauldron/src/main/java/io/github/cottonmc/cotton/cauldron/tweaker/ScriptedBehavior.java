package io.github.cottonmc.cotton.cauldron.tweaker;

import io.github.cottonmc.cotton.cauldron.CauldronBehavior;
import io.github.cottonmc.cotton.cauldron.CauldronContext;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.script.Invocable;
import javax.script.ScriptException;

public class ScriptedBehavior implements CauldronBehavior {
	private Invocable engine;
	private String funcName;
	private Logger logger;

	public ScriptedBehavior(Identifier scriptName, Invocable engine, String funcName) {
		this.engine = engine;
		this.funcName = funcName;
		this.logger = LogManager.getLogger(scriptName.getNamespace());
	}

	@Override
	public void react(CauldronContext ctx) {
		try {
			engine.invokeFunction(funcName, new WrappedCauldronContext(logger, ctx));
		} catch (NoSuchMethodException | ScriptException e) {
			logger.error("Error processing cauldron tweaker predicate: " + e.getMessage());
		}
	}
}

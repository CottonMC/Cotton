package io.github.cottonmc.cotton.tweaker;

import io.github.cottonmc.cotton.Cotton;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.apache.commons.io.IOUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class TweakerLoader implements SimpleResourceReloadListener {
	public static Map<Identifier, String> TWEAKERS = new HashMap<>();
	public static final ScriptEngineManager SCRIPT_MANAGER = new ScriptEngineManager();

	@Override
	public CompletableFuture load(ResourceManager manager, Profiler profiler, Executor executor) {
		return CompletableFuture.supplyAsync(() -> {
			TWEAKERS.clear();
			Collection<Identifier> resources = manager.findResources("tweakers", name -> true);
			for (Identifier fileId : resources) {
				try {
					Resource res = manager.getResource(fileId);
					String script = IOUtils.toString(res.getInputStream());
					int localPath = fileId.getPath().indexOf('/')+1;
					Identifier scriptId = new Identifier(fileId.getNamespace(), fileId.getPath().substring(localPath));
					TWEAKERS.put(scriptId, script);
				} catch (IOException e) {
					Cotton.logger.error("Error when accessing tweaker script %s: %s", fileId.toString(), e.getMessage());
				}
			}
			return TWEAKERS;
		});
	}

	@Override
	public CompletableFuture<Void> apply(Object o, ResourceManager manager, Profiler profiler, Executor executor) {
		return CompletableFuture.runAsync(() -> {
			for (Tweaker tweaker : Tweaker.TWEAKERS) {
				tweaker.prepareReload(manager);
			}
			int loaded = 0;
			for (Identifier tweaker : TWEAKERS.keySet()) {
				String extension = tweaker.getPath().substring(tweaker.getPath().lastIndexOf('.') + 1);
				String script = TWEAKERS.get(tweaker);
				if (script == null) {
					Cotton.logger.error("Tweaker script not found: " + tweaker.toString());
					continue;
				}
				ScriptEngine engine = SCRIPT_MANAGER.getEngineByExtension(extension);
				if (engine == null) {
					Cotton.logger.error("Engine for tweaker script not found: " + tweaker.toString());
					continue;
				}
				try {
					engine.eval(script);
				} catch (ScriptException e) {
					Cotton.logger.error("Error executing tweaker script %s: %s", tweaker.toString(), e.getMessage());
					continue;
				}
				loaded++;
			}
			List<String> applied = new ArrayList<>();
			for (Tweaker tweaker : Tweaker.TWEAKERS) {
				applied.add(tweaker.getApplyMessage());
			}
			String confirm = formatApplied(applied);
			Cotton.logger.info("Applied %s tweaker %s, including %s", loaded, (loaded == 1? "script" : "scripts"), confirm);
		});
	}

	public String formatApplied(List<String> messages) {
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < messages.size(); i++) {
			String message = messages.get(i);
			ret.append(message);
			if (i < messages.size() - 1) {
				if (messages.size() <= 2) ret.append(" ");
				else ret.append(", ");
			}
			if (i == messages.size() - 2) ret.append("and ");
		}
		return ret.toString();
	}

	@Override
	public Identifier getFabricId() {
		return new Identifier(Cotton.MODID, "tweak_loader");
	}
}

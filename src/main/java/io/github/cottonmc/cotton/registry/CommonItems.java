package io.github.cottonmc.cotton.registry;

import io.github.cottonmc.cotton.Cotton;
import net.fabricmc.loader.FabricLoader;
import net.fabricmc.loader.ModContainer;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Iterator;

public class CommonItems {

	public static final String SHARED_NAMESPACE = "cotton";

	/**Attempts to get a common item by name. If no item with this name was found register the given item and return it.
	 * @param name The item name to look for. This is the path and does not include the namespace.
	 * @param item A item that will be registered if no item with this name was found.
	 * @return Returns either an already existing item with the specified name or a new one that was register under the given name.
	 */
	public static Item register(String name, Item item) {
		Identifier id = new Identifier("cotton", name);

		//log some stuff
		Iterator<ModContainer> iterator = FabricLoader.INSTANCE.getModContainers().iterator();
		ModContainer lastLoadedMod = iterator.next();
		while(iterator.hasNext()){
			lastLoadedMod = iterator.next();
		}
		if(item.getItemGroup() != null) {
			Cotton.logger.info("Mod \"" + lastLoadedMod.getInfo().getName() + "\" is adding common item \"" + name + "\" to tab \"" + item.getItemGroup().getUntranslatedName()+"\".");
		} else {
			Cotton.logger.warn("Mod \"" + lastLoadedMod.getInfo().getName() + "\" is adding common item \"" + name + "\" to no tab.");
		}

		if (!Registry.ITEM.contains(id)) {
			Registry.register(Registry.ITEM, id, item);
			return item;
		}
		else {
			return Registry.ITEM.get(id);
		}
	}

	/**Checks if a Common Item with the given name exists and returns it.
	 *
	 * @param name The name to look for.
	 * @return Either the item if it is found or null if no such Common Item exists
	 */
	public static Item getItem(String name){
		Identifier id = new Identifier(SHARED_NAMESPACE, name);

		if (Registry.ITEM.contains(id)) {
			return Registry.ITEM.get(id);
		}
		return null;
	}


}

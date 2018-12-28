package io.github.cottonmc.cotton.registry;

import io.github.cottonmc.cotton.Cotton;
import io.github.cottonmc.cotton.interfaces.IChangeItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CommonItems {

	public static final String SHARED_NAMESPACE = "cotton";

	/**Attempts to get a common item by name. If no item with this name was found register the given item, add it to the common item tab, and return it.
	 * @param name The item name to look for. This is the path and does not include the namespace.
	 * @param item A item that will be registered if no item with this namespace was found.
	 * @return Returns either an already existing item with the specified name or a new one that was register und the given name.
	 */
	public static Item register(String name, Item item) {
		return register(name, item, Cotton.commonGroup);
	}

	/**Attempts to get a common item by name. If no item with this name was found register the given item, add it to the specified tab,
	 * and return it.
	 * Only use this method if you are sure that you want to add the new block to a specific tab.
	 *
	 * @param name The item name to look for. This is the path and does not include the namespace.
	 * @param item A item that will be registered if no item with this namespace was found.
	 * @param tab The tab the new item will be added to.
	 * @return Returns either an already existing item with the specified name or a new one that was register und the given name.
	 */
	public static Item register(String name, Item item, ItemGroup tab) {
		//We want to make sure that all items are in the common tab, unless whoever registered it explicitly requests another tab.
		//One mod accidentally changing the tab of e.g. copper_ore would make it a pain to find this block in the creative inventory.
		((IChangeItemGroup) item).setItemGroup(tab);

		Identifier id = new Identifier("cotton", name);
		if (!Registry.ITEM.contains(id)) {
			Registry.register(Registry.ITEM, id, item);
			return item;
		}
		else {
			return Registry.ITEM.get(id);
		}
	}


}

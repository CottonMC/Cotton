package io.github.cottonmc.cotton.registry;

import io.github.cottonmc.cotton.Cotton;
import io.github.cottonmc.cotton.tags.TagEntryManager;
import io.github.cottonmc.cotton.tags.TagType;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
public class CommonItems {

	/**Attempts to get a common item by name. If no item with this name was found register the given item and return it.
	 * @param name The item name to look for. This is the path and does not include the namespace.
	 * @param item A item that will be registered if no item with this name was found.
	 * @return Returns either an already existing item with the specified name or a new one that was register under the given name.
	 */
	public static Item register(String name, Item item) {
		Identifier id = new Identifier(Cotton.SHARED_NAMESPACE, name);

		if (!Registry.ITEM.containsId(id)) {
			Registry.register(Registry.ITEM, id, item);
			TagEntryManager.registerToTag(TagType.ITEM, new Identifier(Cotton.SHARED_NAMESPACE, name), id.toString());
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
		Identifier id = new Identifier(Cotton.SHARED_NAMESPACE, name);

		if (Registry.ITEM.containsId(id)) {
			return Registry.ITEM.get(id);
		}
		return null;
	}


}

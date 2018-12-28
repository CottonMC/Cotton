package io.github.cottonmc.cotton.registry;

import io.github.cottonmc.cotton.Cotton;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.block.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CommonBlocks {
	public static final String SHARED_NAMESPACE = "cotton";

	/**Attempts to get a common block by name. If no block with this name was found register the given block and return it.
	 * @param name The block name to look for. This is the path and does not include the namespace.
	 * @param block A block that will be registered if no block with this name was found.
	 * @return Returns either an already existing block with the specified name or a new one that was register under the given name.
	 */
	public static Block register(String name, Block block) {
		return register(name, block, Cotton.commonGroup);
	}

	/**Attempts to get a common block by name. If no block with this name was found register the given block, add it to the specified tab
	 * and return it.
	 * Only use this method if you are sure that you want to add the new block to a specific tab.
	 *
	 * @param name The block name to look for. This is the path and does not include the namespace.
	 * @param block A block that will be registered if no block with this name was found.
	 * @param tab The tab the new block will be added to.
	 * @return Returns either an already existing block with the specified name or a new one that was register under the given name.
	 */
	public static Block register(String name, Block block, ItemGroup tab) {
		return register(name, block, new Item.Settings(), tab);
	}

	/**Attempts to get a common block by name. If no block with this name was found register the given block.
	 *
	 * @param name The block name to look for. This is the path and does not include the namespace.
	 * @param block A block that will be registered if no block with this name was found.
	 * @param settings The settings of the new Blocks BlockItem. The tab will be changed to the cotton default tab.
	 * @return Returns either an already existing block with the specified name or a new one that was register under the given name.
	 */
	public static Block register(String name, Block block, Item.Settings settings) {
		return register(name, block, settings, Cotton.commonGroup);
	}

	/**Attempts to get a common block by name. If no block with this name was found register the given block, add it to the specified tab
	 * and return it.
	 * Only use this method if you are sure that you want to add the new block to a tag.
	 *
	 * @param name The block name to look for. This is the path and does not include the namespace.
	 * @param block A block that will be registered if no block with this name was found.
 	 * @param settings The settings of the blocks BlockItem.
	 * @param tab The tab the new block will be added to.
	 * @return Returns either an already existing block with the specified name or a new one that was register under the given name.
	 */
	public static Block register(String name, Block block, Item.Settings settings, ItemGroup tab) {
		Identifier id = new Identifier(SHARED_NAMESPACE, name);
		if (!Registry.BLOCK.contains(id)) {
			Registry.register(Registry.BLOCK, id, block);
			BlockItem item = new BlockItem(block, settings.itemGroup(tab));
			CommonItems.register(id.getPath(), item);
			return block;
		}
		else {
			return Registry.BLOCK.get(id);
		}
	}



}

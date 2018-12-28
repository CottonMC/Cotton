package io.github.cottonmc.cotton.registry;

import io.github.cottonmc.cotton.Cotton;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.block.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CommonBlocks {
	public static final String SHARED_NAMESPACE = "cotton";

	/**Attempts to get a common block by name. If no block with this name was found register the given block and create a BlockItem for it.
	 * @param name The block name to look for. This is the path and does not include the namespace.
	 * @param block A block that will be registered if no block with this name was found.
	 * @return Returns either an already existing block with the specified name or a new one that was register under the given name.
	 */
	public static Block register(String name, Block block) {
		BlockItem item = new BlockItem(block, (new Item.Settings()).itemGroup(Cotton.commonGroup));
		return register(name, block, item);
	}

	/**Attempts to get a common block by name. If no block with this name was found register the given block and the provided BlockItem.
	 *
	 * @param name The block name to look for. This is the path and does not include the namespace.
	 * @param block A block that will be registered if no block with this name was found.
	 * @param item The settings of the new Blocks BlockItem. The tab will be changed to the cotton default tab.
	 * @return Returns either an already existing block with the specified name or a new one that was register under the given name.
	 */
	public static Block register(String name, Block block, BlockItem item) {
		return register(name, block, item, Cotton.commonGroup);
	}

	/**Attempts to get a common block by name. If no block with this name was found register the given block and BlockItem,
	 * add it to the specified tab and return the Block.
	 * Only use this method if you are sure that you want to add the new block to a tag.
	 *
	 * @param name The block name to look for. This is the path and does not include the namespace.
	 * @param block A block that will be registered if no block with this name was found.
 	 * @param item The BlockItem that will be added should no Block with that name exist.
	 * @param tab The tab the new BlockItem will be added to.
	 * @return Returns either an already existing block with the specified name or a new one that was register under the given name.
	 */
	public static Block register(String name, Block block, BlockItem item, ItemGroup tab) {
		Identifier id = new Identifier(SHARED_NAMESPACE, name);
		if (!Registry.BLOCK.contains(id)) {
			Registry.register(Registry.BLOCK, id, block);
			CommonItems.register(id.getPath(), item, tab);
			return block;
		}
		else {
			return Registry.BLOCK.get(id);
		}
	}



}

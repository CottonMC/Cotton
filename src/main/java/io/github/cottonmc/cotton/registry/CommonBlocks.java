package io.github.cottonmc.cotton.registry;

import io.github.cottonmc.cotton.Cotton;
import io.github.cottonmc.cotton.datapack.tags.TagEntryManager;
import io.github.cottonmc.cotton.datapack.tags.TagType;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.block.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CommonBlocks {

	/**Attempts to get a common block by name. If no block with this name was found register the given block and create a BlockItem for it.
	 * @param name The block name to look for. This is the path and does not include the namespace.
	 * @param block A block that will be registered if no block with this name was found.
	 * @return Returns either an already existing block with the specified name or a new one that was register under the given name.
	 */
	public static Block register(String name, Block block) {
		BlockItem item = new BlockItem(block, new Item.Settings().itemGroup(Cotton.commonGroup));
		return register(name, block, item);
	}

	/**Attempts to get a common block by name. If no block with this name was found register the given Block and BlockItem.
	 *
	 * @param name The block name to look for. This is the path and does not include the namespace.
	 * @param block A block that will be registered if no block with this name was found.
	 * @param item The settings of the new Blocks BlockItem.
	 * @return Returns either an already existing block with the specified name or a new one that was register under the given name.
	 */
	public static Block register(String name, Block block, BlockItem item) {
		Identifier id = new Identifier(Cotton.SHARED_NAMESPACE, name);

		if (!Registry.BLOCK.containsId(id)) {
			Registry.register(Registry.BLOCK, id, block);
			CommonItems.register(id.getPath(), item);
			TagEntryManager.registerToTag(TagType.BLOCK, new Identifier(Cotton.SHARED_NAMESPACE, name), id.toString());
			return block;
		}
		else {
			return Registry.BLOCK.get(id);
		}
	}

	/**Checks if a Common Block with the given name exists and returns it.
	 *
	 * @param name The name to look for.
	 * @return Either the block if it is found or null if no such Common Block exists
	 */
	public static Block getBlock(String name){
		Identifier id = new Identifier(Cotton.SHARED_NAMESPACE, name);

		if (Registry.BLOCK.containsId(id)) {
			return Registry.BLOCK.get(id);
		}
		return null;
	}


}

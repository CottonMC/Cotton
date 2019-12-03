package io.github.cottonmc.cotton.commons;

import io.github.cottonmc.cotton.datapack.CottonDatapack;
import io.github.cottonmc.cotton.datapack.tags.TagEntryManager;
import io.github.cottonmc.cotton.datapack.tags.TagType;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/** @deprecated The functionality from this class is moving to Cotton Resources */
@Deprecated
public class CommonBlocks {

	/**Attempts to get a common block by name. If no block with this name was found register the given block and create a BlockItem for it.
	 * @param name The block name to look for. This is the path and does not include the namespace.
	 * @param block A block that will be registered if no block with this name was found.
	 * @return Returns either an already existing block with the specified name or a new one that was register under the given name.
	 * @deprecated This function is moving to Cotton Resources
	 */
	public static Block register(String name, Block block) {
		BlockItem item = new BlockItem(block, new Item.Settings()/*.group(CottonCommons.ITEM_GROUP)*/);
		return register(name, block, item);
	}

	/**Attempts to get a common block by name. If no block with this name was found register the given Block and BlockItem.
	 *
	 * @param name The block name to look for. This is the path and does not include the namespace.
	 * @param block A block that will be registered if no block with this name was found.
	 * @param item The settings of the new Blocks BlockItem.
	 * @return Returns either an already existing block with the specified name or a new one that was register under the given name.
	 * @deprecated This function is moving to Cotton Resources
	 */
	public static Block register(String name, Block block, BlockItem item) {
		Identifier id = new Identifier(CottonDatapack.SHARED_NAMESPACE, name);

		if (!Registry.BLOCK.getOrEmpty(id).isPresent()) {
			Registry.register(Registry.BLOCK, id, block);
			CommonItems.register(id.getPath(), item);
			TagEntryManager.registerToTag(TagType.BLOCK, new Identifier(CottonDatapack.SHARED_NAMESPACE, name), id.toString());
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
	 * @deprecated This function is moving to Cotton Resources
	 */
	public static Block getBlock(String name){
		Identifier id = new Identifier(CottonDatapack.SHARED_NAMESPACE, name);

		if (!Registry.BLOCK.getOrEmpty(id).isPresent()) {
			return Registry.BLOCK.get(id);
		}
		return null;
	}


}

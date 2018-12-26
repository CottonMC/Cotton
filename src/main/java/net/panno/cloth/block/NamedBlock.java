package net.panno.cloth.block;

import net.minecraft.block.Block;

public interface NamedBlock {

	//exiss so we can easily register non-BE blocks and BE blocks with the same register()

	String getName();
	Block getBlock();
}

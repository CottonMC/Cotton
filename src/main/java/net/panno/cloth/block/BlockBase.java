package net.panno.cloth.block;

import net.minecraft.block.Block;

import java.util.List;

public class BlockBase extends Block implements NamedBlock {

	public String name;

	public BlockBase(String name, Settings settings) {
		super(settings);
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Block getBlock() {
		return this;
	}

	//add any other helpful stuff here, like a default Settings
}

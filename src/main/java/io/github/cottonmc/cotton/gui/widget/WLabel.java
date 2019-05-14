package io.github.cottonmc.cotton.gui.widget;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
public class WLabel extends WWidget {
	protected final Component text;
	protected final int color;

	public static final int DEFAULT_TEXT_COLOR = 0x404040;

	public WLabel(String text, int color) {
		this(new TextComponent(text), color);
	}
	
	public WLabel(Component text, int color) {
		this.text = text;
		this.color = color;
	}

	public WLabel(String text) {
		this(text, DEFAULT_TEXT_COLOR);
	}

	@Override
	public void paintBackground(int x, int y) {
		String translated = text.getFormattedText();
		ScreenDrawing.drawString(translated, x, y, color);
	}

	@Override
	public boolean canResize() {
		return false;
	}
	
	@Override
	public int getWidth() {
		return 8; //We don't actually clip to our boundaries so return a dummy value.
	}
	
	@Override
	public int getHeight() {
		return 8;
	}
}
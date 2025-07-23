package com.davenonymous.pipegoggles.lib.gui;

import com.davenonymous.pipegoggles.lib.gui.event.UpdateScreenEvent;
import com.davenonymous.pipegoggles.lib.gui.event.WidgetEventResult;
import com.davenonymous.pipegoggles.lib.gui.widgets.WidgetCloseButton;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public abstract class WidgetContainerFullScreen<T extends WidgetContainer> extends WidgetContainerScreen<T> {
	private Window window;
	private int lastWindowWidth;
	private int lastWindowHeight;

	WidgetCloseButton closeButton;

	public WidgetContainerFullScreen(T container, Inventory inv, Component name) {
		super(container, inv, name);
	}

	protected abstract void updateWidgetSizes();

	@Override
	protected GUI createGUI() {
		this.window = Minecraft.getInstance().getWindow();

		this.width = window.getGuiScaledWidth();
		this.height = window.getGuiScaledHeight();
		this.lastWindowWidth = this.width;
		this.lastWindowHeight = this.height;

		GUI gui = new GUI(0, 0, this.width, this.height);

		// Always scale to full screen size
		gui.addListener(
			UpdateScreenEvent.class, (event, widget) -> {
				if(this.lastWindowWidth != this.window.getGuiScaledWidth() || this.lastWindowHeight != this.window.getGuiScaledHeight()) {
					this.lastWindowWidth = this.window.getGuiScaledWidth();
					this.lastWindowHeight = this.window.getGuiScaledHeight();
					this.width = this.lastWindowWidth;
					this.height = this.lastWindowHeight;
					gui.setSize(this.width, this.height);
					this.closeButton.setPosition(this.width - 14, 4);
					updateWidgetSizes();
				}
				return WidgetEventResult.CONTINUE_PROCESSING;
			}
		);

		this.closeButton = new WidgetCloseButton();
		this.closeButton.setPosition(this.width - 14, 4);
		gui.add(this.closeButton);

		return gui;
	}
}

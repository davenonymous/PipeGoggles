package com.davenonymous.pipegoggles.lib.gui;


import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.lib.gui.widgets.IValueProvider;
import com.davenonymous.pipegoggles.lib.gui.widgets.Widget;
import com.davenonymous.pipegoggles.lib.gui.widgets.WidgetPanel;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GUI extends WidgetPanel {
	public static ResourceLocation tabIcons = PipeGoggles.resource("textures/gui/tabicons.png");
	public static ResourceLocation windowBackground = PipeGoggles.resource("textures/gui/window.png");
	public static ResourceLocation defaultButtonTexture = PipeGoggles.resource("textures/gui/button_background.png");

	public static final Logger LOGGER = LogUtils.getLogger();

	public boolean hasTabs = false;
	private final Map<ResourceLocation, IValueProvider> valueMap = new HashMap<>();
	private WidgetContainer container;
	private boolean isDragging = false;
	private final Set<Integer> pressedKeys = new HashSet<>();

	public GUI(int x, int y, int width, int height) {
		this.setX(x);
		this.setY(y);
		this.setWidth(width);
		this.setHeight(height);
	}


	public void findValueWidgets() {
		this.findValueWidgets(this);
	}

	public void registerValueWidget(ResourceLocation id, IValueProvider widget) {
		this.valueMap.put(id, widget);
	}

	public Object getValue(ResourceLocation id) {
		if(id == null || !valueMap.containsKey(id)) {
			return null;
		}

		return valueMap.get(id).getValue();
	}

	public void drawGUI(GuiGraphics pGuiGraphics, Screen screen) {
		this.setX((screen.width - this.width) / 2);
		this.setY((screen.height - this.height) / 2);

		this.shiftAndDraw(pGuiGraphics, screen);
	}

	@Override
	public void drawBeforeShift(GuiGraphics pGuiGraphics, Screen screen) {
		//screen.drawDefaultBackground();

		super.drawBeforeShift(pGuiGraphics, screen);
	}

	@Override
	public void draw(GuiGraphics pGuiGraphics, Screen screen) {
		GUIHelper.drawWindow(pGuiGraphics, this.width, this.height, this.hasTabs);
		super.draw(pGuiGraphics, screen);
	}

	public void drawTooltips(GuiGraphics pGuiGraphics, Screen screen, int mouseX, int mouseY) {
		Widget hoveredWidget = getHoveredWidget(mouseX, mouseY);
		if(hoveredWidget == null || hoveredWidget.getTooltip() == null) {
			return;
		}
		if(hoveredWidget.getTooltip().isEmpty()) {
			return;
		}
		if(!hoveredWidget.shouldShowTooltip()) {
			return;
		}

		Font font = screen.getMinecraft().font;
		var formattedTooltip = hoveredWidget.getTooltipFormatted();
		var maxWidth = Integer.MIN_VALUE;
		for(Either<FormattedText, TooltipComponent> line : formattedTooltip) {
			if(line.left().isPresent()) {
				FormattedText text = line.left().get();
				maxWidth = Math.max(maxWidth, font.width(text));
			} else if(line.right().isPresent()) {
				if(line.right().get() instanceof ClientTooltipComponent clientTooltipComponent) {
					maxWidth = Math.max(maxWidth, clientTooltipComponent.getWidth(font));
				}
			}
		}

		hoveredWidget.setActualTooltipWidth(maxWidth);
		pGuiGraphics.renderComponentTooltipFromElements(font, hoveredWidget.getTooltipFormatted(), mouseX, mouseY, ItemStack.EMPTY);
	}

	public void drawSlot(GuiGraphics pGuiGraphics, Screen screen, Slot slot, int guiLeft, int guiTop) {
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

		if(slot instanceof WidgetSlot) {
			if(!slot.allowModification(screen.getMinecraft().player)) {
				RenderSystem.setShaderColor(1.0f, 0.3f, 0.3f, 1.0f);
			}
		}

		float offsetX = guiLeft - 1;
		float offsetY = guiTop - 1;

		pGuiGraphics.pose().pushPose();
		pGuiGraphics.pose().translate(offsetX, offsetY, 0.0f);
		int texOffsetY = 84;
		int texOffsetX = 84;

		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, GUIHelper.tabIcons);
		pGuiGraphics.blit(GUIHelper.tabIcons, slot.x, slot.y, texOffsetX, texOffsetY, 18, 18);

		//RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		pGuiGraphics.pose().popPose();
	}

	public void setContainer(WidgetContainer container) {
		this.container = container;
	}

	public WidgetContainer getContainer() {
		return container;
	}

	public void setDragging(boolean dragging) {
		this.isDragging = dragging;
	}

	public boolean isDragging() {
		return isDragging;
	}

	public void keyDown(int scanCode) {
		this.pressedKeys.add(scanCode);
	}

	public void keyUp(int scanCode) {
		this.pressedKeys.remove(scanCode);
	}

	public boolean isKeyPressed(int scanCode) {
		return this.pressedKeys.contains(scanCode);
	}

	private static final int SCAN_CODE_CTRL = 29;
	private static final int SCAN_CODE_CTRL_LEFT = 157;
	private static final int SCAN_CODE_CTRL_RIGHT = 197;

	private static final int SCAN_CODE_SHIFT = 42;
	private static final int SCAN_CODE_SHIFT_LEFT = 54;
	private static final int SCAN_CODE_SHIFT_RIGHT = 55;

	public boolean isCtrlDown() {
		return this.isKeyPressed(SCAN_CODE_CTRL) || this.isKeyPressed(SCAN_CODE_CTRL_LEFT) || this.isKeyPressed(SCAN_CODE_CTRL_RIGHT);
	}

	public boolean isShiftDown() {
		return this.isKeyPressed(SCAN_CODE_SHIFT) || this.isKeyPressed(SCAN_CODE_SHIFT_LEFT) || this.isKeyPressed(SCAN_CODE_SHIFT_RIGHT);
	}

}

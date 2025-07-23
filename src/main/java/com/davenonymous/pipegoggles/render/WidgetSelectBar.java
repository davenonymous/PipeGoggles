package com.davenonymous.pipegoggles.render;

import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.lib.gui.GUIHelper;
import com.davenonymous.pipegoggles.lib.gui.Icons;
import com.davenonymous.pipegoggles.lib.gui.event.MouseDraggedEvent;
import com.davenonymous.pipegoggles.lib.gui.event.MouseReleasedEvent;
import com.davenonymous.pipegoggles.lib.gui.event.WidgetEventResult;
import com.davenonymous.pipegoggles.lib.gui.widgets.Widget;
import com.davenonymous.pipegoggles.lib.gui.widgets.WidgetLabel;
import com.davenonymous.pipegoggles.lib.gui.widgets.WidgetWithChoiceValue;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

import java.util.function.Function;

public class WidgetSelectBar<T> extends WidgetWithChoiceValue<T> {
	float sliderPosition = 0.0f; // Position of the slider paddle, between 0.0 and 1.0
	float lineWidth = 2.0f;
	int lineOffset = 4;
	int borderLineHeight = 8;
	int slotLineHeight = 5;
	int lineColor = 0xFF505050;

	Function<T, Widget> labelProvider = t -> {
		var widget = new WidgetLabel(t.toString());
		widget.autoWidth(50);
		widget.setTextColor(lineColor);
		return widget;
	};
	Function<T, Float> positionProvider = t -> {
		float regularSpacing = 1 / (float) (this.choices.size()-1);
		int choiceNum = this.choices.getPointerIndex();
		return regularSpacing * choiceNum;
	};


	public WidgetSelectBar() {
		super();

		this.addListener(MouseDraggedEvent.class, (event, widget) -> {
			if(event.button() != 0) {
				return WidgetEventResult.CONTINUE_PROCESSING; // Only handle left mouse button drags
			}

			float mouseX = getActualX() - (float)event.mouseX();
			float ratio = Math.abs(mouseX / (float)(width - 1));

			this.sliderPosition = Math.max(0.0f, Math.min(1.0f, ratio)); // Clamp between 0.0 and 1.0
			return WidgetEventResult.CONTINUE_PROCESSING;
		});

		this.addListener(MouseReleasedEvent.class, (event, widget) -> {
			if(event.button != 0) {
				return WidgetEventResult.CONTINUE_PROCESSING; // Only handle left mouse button releases
			}

			T closestChoice = getClosestChoice(sliderPosition);
			if(closestChoice != null) {
				this.setValue(closestChoice, true);
			}
			return WidgetEventResult.CONTINUE_PROCESSING;
		});
	}

	private T getClosestChoice(float position) {
		if(this.choices.isEmpty()) {
			return null;
		}

		float closestDistance = Float.MAX_VALUE;
		T closestChoice = null;

		for(T choice : this.choices) {
			float choicePosition = this.positionProvider.apply(choice);
			float distance = Math.abs(choicePosition - position);
			if(distance < closestDistance) {
				closestDistance = distance;
				closestChoice = choice;
			}
		}

		return closestChoice;
	}

	public WidgetSelectBar<T> setLabelProvider(Function<T, Widget> labelProvider) {
		this.labelProvider = labelProvider;
		return this;
	}

	public WidgetSelectBar<T> setPositionProvider(Function<T, Float> positionProvider) {
		this.positionProvider = positionProvider;
		return this;
	}

	@Override
	public void setValue(T choice, boolean fireEvent) {
		super.setValue(choice, fireEvent);
		this.sliderPosition = this.positionProvider.apply(choice);
	}

	private void drawSlider(GuiGraphics pGuiGraphics, Screen screen) {
		pGuiGraphics.pose().pushPose();
		pGuiGraphics.pose().translate((sliderPosition * (width-1)) - 4f, 0, 0);
		pGuiGraphics.blit(Icons.sliderPaddle, 0, 0, 0, 0, 7, 12, 7, 12);
		pGuiGraphics.pose().popPose();
	}

	private void drawBackgroundLine(GuiGraphics pGuiGraphics, Screen screen) {


		float centerY = lineOffset + 4;
		GUIHelper.drawFatLine(pGuiGraphics, lineWidth / 2, centerY, width-1, centerY, lineWidth, lineColor);

		GUIHelper.drawFatLine(pGuiGraphics, 0, centerY - (borderLineHeight / 2.0f), 0, centerY + (borderLineHeight / 2.0f), lineWidth, lineColor);
		GUIHelper.drawFatLine(pGuiGraphics, width - 1, centerY - (borderLineHeight / 2.0f), width - 1, centerY + (borderLineHeight / 2.0f), lineWidth, lineColor);

		int choiceNum = 0;
		float regularSpacing = 1 / (float) (this.choices.size()-1);
		for(T choice : this.choices) {
			float position = this.positionProvider != null ? this.positionProvider.apply(choice) : regularSpacing * choiceNum;
			float x = position * (width - 1);
			GUIHelper.drawFatLine(pGuiGraphics, x, centerY - (slotLineHeight / 2.0f), x, centerY + (slotLineHeight / 2.0f), lineWidth, lineColor);
			choiceNum++;

			Widget label = this.labelProvider.apply(choice);
			float xShift = (label.width / 2.0f) - 1;
			label.setPosition(this.getActualX() + (int)x - (int)xShift, this.getActualY());
			label.setHeight(this.height);

			//PipeGoggles.LOGGER.info("Drawing label for choice: " + choice + " at position: " + x + ", " + this.getActualY() + ", width: " + label.width);
			pGuiGraphics.pose().pushPose();
			pGuiGraphics.pose().translate(x - xShift, lineOffset + 10, 100);
			label.draw(pGuiGraphics, screen);
			pGuiGraphics.pose().popPose();

		}
	}

	@Override
	public void draw(GuiGraphics pGuiGraphics, Screen screen) {
		int availableWidth = this.width;

		drawBackgroundLine(pGuiGraphics, screen);
		drawSlider(pGuiGraphics, screen);

		if(this.positionProvider != null) {

		}
	}
}

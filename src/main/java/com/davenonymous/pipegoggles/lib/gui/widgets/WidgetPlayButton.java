package com.davenonymous.pipegoggles.lib.gui.widgets;

import com.davenonymous.pipegoggles.lib.gui.ColorHelper;
import com.davenonymous.pipegoggles.lib.gui.Icons;
import com.davenonymous.pipegoggles.lib.gui.event.*;
import net.minecraft.client.Minecraft;

public class WidgetPlayButton extends WidgetImage {
	private long lastClickTime = 0;
	private boolean isPlaying;

	public interface OnPlay {
		void onPlay();
	}
	public interface OnStop {
		void onStop();
	}

	public WidgetPlayButton(boolean isInitiallyPlaying, OnPlay onClick, OnStop onStop) {
		super(isInitiallyPlaying ? Icons.pauseButton : Icons.playButton);
		this.isPlaying = isInitiallyPlaying;
		this.setColor(isPlaying ? ColorHelper.COLOR_ORANGE : ColorHelper.COLOR_GREEN);
		this.setSize(11, 10);

		this.addListener(MouseClickEvent.class, (event, widget) -> {
			if (event.button != 0) {
				return WidgetEventResult.CONTINUE_PROCESSING;
			}

			if (this.isPlaying) {
				// If the button is already playing, we stop it
				this.isPlaying = false;
				onStop.onStop();
				this.setImage(Icons.playButton);
				this.setColor(ColorHelper.COLOR_GREEN);
			} else {
				// If the button is not playing, we start it
				this.isPlaying = true;
				onClick.onPlay();
				this.setImage(Icons.pauseButton);
				this.setColor(ColorHelper.COLOR_ORANGE);
			}

			lastClickTime = Minecraft.getInstance().level.getGameTime();
			return WidgetEventResult.HANDLED;
		});

		this.addListener(MouseReleasedEvent.class, (event, widget) -> {
			if (event.button != 0) {
				return WidgetEventResult.CONTINUE_PROCESSING;
			}
			if(!widget.isPosInside(event.x, event.y)) {
				// If the mouse was released outside the button, we ignore it
				return WidgetEventResult.CONTINUE_PROCESSING;
			}

			long currentTime = Minecraft.getInstance().level.getGameTime();
			long timeSinceLastClick = currentTime - lastClickTime;
			if (timeSinceLastClick < 5) { // this is about 250ms
				// If the button was released again within n ticks, then we register the click as a play-without-hold action
				return WidgetEventResult.CONTINUE_PROCESSING;
			}

			// If the button was released after a hold, we register it as a stop action
			this.isPlaying = false;
			onStop.onStop();
			this.setImage(Icons.playButton);
			this.setColor(ColorHelper.COLOR_GREEN);
			return WidgetEventResult.HANDLED;
		});

		this.addListener(
			MouseEnterEvent.class, (event, widget) -> {
				//this.setImage(IMAGE_HIGHLIGHT);
				return WidgetEventResult.HANDLED;
			}
		);

		this.addListener(
			MouseExitEvent.class, (event, widget) -> {
				//this.setImage(IMAGE);
				return WidgetEventResult.HANDLED;
			}
		);
	}
}

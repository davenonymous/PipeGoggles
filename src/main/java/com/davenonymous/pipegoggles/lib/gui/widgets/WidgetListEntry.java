package com.davenonymous.pipegoggles.lib.gui.widgets;


import com.davenonymous.pipegoggles.lib.gui.ISelectable;
import com.davenonymous.pipegoggles.lib.gui.event.ListEntrySelectionEvent;
import com.davenonymous.pipegoggles.lib.gui.event.WidgetEventResult;

public class WidgetListEntry extends WidgetPanel implements ISelectable {
	boolean isSelected = false;

	@Override
	public boolean isSelected() {
		return this.isSelected;
	}

	@Override
	public void setSelected(boolean state) {
		this.isSelected = state;
		this.fireEvent(new ListEntrySelectionEvent(state));
	}

	public void bindTo(Widget boundWidget) {
		this.addListener(
			ListEntrySelectionEvent.class, (event, widget) -> {
				boundWidget.setVisible(event.selected);
				return WidgetEventResult.CONTINUE_PROCESSING;
			}
		);
	}
}

package com.davenonymous.pipegoggles.lib.gui.event;


import com.davenonymous.pipegoggles.lib.gui.widgets.Widget;

public interface IWidgetListener<T extends IEvent> {
	WidgetEventResult call(T event, Widget widget);
}

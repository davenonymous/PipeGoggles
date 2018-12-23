package org.dave.pipemaster.gui.event;

import org.dave.pipemaster.gui.widgets.Widget;

public interface IWidgetListener<T extends IEvent> {
    WidgetEventResult call(T event, Widget widget);
}

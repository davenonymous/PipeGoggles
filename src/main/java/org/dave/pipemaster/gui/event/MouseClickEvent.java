package org.dave.pipemaster.gui.event;

public class MouseClickEvent implements IEvent {
    public int button;
    public int x;
    public int y;

    public MouseClickEvent(int mouseX, int mouseY, int button) {
        this.x = mouseX;
        this.y = mouseY;
        this.button = button;
    }

    public boolean isLeftClick() { return button == 0; };
}

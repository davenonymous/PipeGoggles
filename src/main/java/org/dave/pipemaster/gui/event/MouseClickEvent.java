package org.dave.pipemaster.gui.event;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class MouseClickEvent implements IEvent {
    public int button;
    public int x;
    public int y;
    public ItemStack carriedStack = ItemStack.EMPTY;

    public MouseClickEvent(int mouseX, int mouseY, int button) {
        this.x = mouseX;
        this.y = mouseY;
        this.button = button;
        this.carriedStack = Minecraft.getMinecraft().player.inventory.getItemStack().copy();
    }

    public boolean isLeftClick() { return button == 0; }

    @Override
    public String toString() {
        return String.format("MouseClick[x=%d,y=%d,button=%d]", this.x, this.y, this.button);
    }
}

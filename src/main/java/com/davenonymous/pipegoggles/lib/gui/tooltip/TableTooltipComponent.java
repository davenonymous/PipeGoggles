package com.davenonymous.pipegoggles.lib.gui.tooltip;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableTooltipComponent implements TooltipComponent, ClientTooltipComponent {
	private record CellData(TooltipComponent component, BoxAlignment vertical, BoxAlignment horizontal) {
		public CellData(TooltipComponent component) {
			this(component, BoxAlignment.CENTER, BoxAlignment.START);
		}

		public CellData withComponent(TooltipComponent component) {
			return new CellData(component, this.vertical, this.horizontal);
		}

		public CellData withAlignment(BoxAlignment vertical, BoxAlignment horizontal) {
			return new CellData(this.component, vertical, horizontal);
		}
	}

	private final Table<Integer, Integer, CellData> components = HashBasedTable.create();
	private final Map<Integer, Integer> rowHeights = new HashMap<>();
	private final Map<Integer, Integer> colWidths = new HashMap<>();
	private int highestColumn = 0;
	private int longestRow = 0;

	private int verticalPadding = 2;
	private int horizontalPadding = 4;

	public TableTooltipComponent() {

	}

	private void putComponent(int row, int col, TooltipComponent component) {
		putComponent(row, col, new CellData(component));
	}

	private void putComponent(int row, int col, CellData cell) {
		this.components.put(row, col, cell);

		updateTableRowColsToComponent(row, col, cell.component());
	}

	private void updateTableRowColsToComponent(int row, int col, TooltipComponent component) {
		if(!(component instanceof ClientTooltipComponent clientTooltipComponent)) {
			return;
		}
		int height = clientTooltipComponent.getHeight();
		int width = clientTooltipComponent.getWidth(Minecraft.getInstance().font);

		if(height > rowHeights.getOrDefault(row, 0)) {
			rowHeights.put(row, height);
		}
		if(width > colWidths.getOrDefault(col, 0)) {
			colWidths.put(col, width);
		}

		updateTableSize();
	}

	private void updateTableSize() {
		int totalHeight = rowHeights.values().stream().mapToInt(Integer::intValue).sum() + (rowHeights.size() * verticalPadding);
		int totalWidth = colWidths.values().stream().mapToInt(Integer::intValue).sum() + (colWidths.size() * verticalPadding);

		this.highestColumn = totalHeight;
		this.longestRow = totalWidth;
	}

	public interface CellConsumer {
		void accept(int row, int col, TooltipComponent component, BoxAlignment vertical, BoxAlignment horizontal);
	}

	public TableTooltipComponent forCellInRow(int row, CellConsumer consumer) {
		for (int col : components.columnKeySet()) {
			if (components.contains(row, col)) {
				CellData cell = components.get(row, col);
				if (cell != null) {
					consumer.accept(row, col, cell.component(), cell.vertical, cell.horizontal);
				}
			}
		}
		return this;
	}

	public TableTooltipComponent forCellInColumn(int col, CellConsumer consumer) {
		for (int row : components.rowKeySet()) {
			if (components.contains(row, col)) {
				CellData cell = components.get(row, col);
				if (cell != null) {
					consumer.accept(row, col, cell.component(), cell.vertical, cell.horizontal);
				}
			}
		}
		return this;
	}

	public TableTooltipComponent forAllCells(CellConsumer consumer) {
		for (int row : components.rowKeySet()) {
			for (int col : components.columnKeySet()) {
				if (components.contains(row, col)) {
					CellData cell = components.get(row, col);
					if (cell != null) {
						consumer.accept(row, col, cell.component(), cell.vertical, cell.horizontal);
					}
				}
			}
		}
		return this;
	}

	public TableTooltipComponent updateCellSizes() {
		rowHeights.clear();
		colWidths.clear();

		forAllCells((row, col, component, vertical, horizontal) -> {
			updateTableRowColsToComponent(row, col, component);
		});

		return this;
	}

	public TableTooltipComponent addRow(TooltipComponent... component) {
		int rows = this.components.rowKeySet().size();
		for(int i = 0; i < component.length; i++) {
			putComponent(rows, i, component[i]);
		}
		return this;
	}

	public TableTooltipComponent addRow(List<TooltipComponent> components) {
		return this.addRow(components.toArray(new TooltipComponent[0]));
	}

	public TableTooltipComponent addColumn(TooltipComponent... component) {
		int cols = this.components.columnKeySet().size();
		for(int i = 0; i < component.length; i++) {
			putComponent(i, cols, component[i]);
		}
		return this;
	}

	public TableTooltipComponent setCellAlignment(int row, int col, BoxAlignment vertical, BoxAlignment horizontal) {
		if(!this.components.contains(row, col) || this.components.get(row, col) == null) {
			putComponent(row, col, new CellData(null, vertical, horizontal));
		} else {
			//noinspection DataFlowIssue
			putComponent(row, col, this.components.get(row, col).withAlignment(vertical, horizontal));
		}
		return this;
	}

	public TableTooltipComponent setCellData(int row, int col, TooltipComponent component) {
		if(!this.components.contains(row, col) || this.components.get(row, col) == null) {
			putComponent(row, col, new CellData(component));
		} else {
			//noinspection DataFlowIssue
			putComponent(row, col, this.components.get(row, col).withComponent(component));
		}
		return this;
	}

	public TooltipComponent getCellData(int row, int col) {
		if(!this.components.contains(row, col) || this.components.get(row, col) == null) {
			return null;
		}

		//noinspection DataFlowIssue
		return this.components.get(row, col).component;
	}

	public TableTooltipComponent setVerticalPadding(int verticalPadding) {
		this.verticalPadding = verticalPadding;
		return this;
	}

	public TableTooltipComponent setHorizontalPadding(int horizontalPadding) {
		this.horizontalPadding = horizontalPadding;
		return this;
	}

	public int count() {
		return components.size();
	}

	public int rows() {
		return components.rowKeySet().size();
	}

	public int cols() {
		return components.columnKeySet().size();
	}

	public TableTooltipComponent clear() {
		components.clear();
		rowHeights.clear();
		colWidths.clear();
		return this;
	}

	@Override
	public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
		int yPos = y;
		for (int row : components.rowKeySet()) {
			if (row > 0) {
				yPos += rowHeights.getOrDefault(row - 1, 0) + verticalPadding; // Add padding between rows
			}

			int xPos = x;
			for(int col : components.columnKeySet()) {
				if(col > 0) {
					xPos += colWidths.getOrDefault(col - 1, 0) + horizontalPadding; // Add padding between columns
				}

				if(!components.contains(row, col)) {
					continue;
				}

				CellData cell = components.get(row, col);
				if(cell == null) {
					continue;
				}

				TooltipComponent component = cell.component();
				if(!(component instanceof ClientTooltipComponent clientComponent)) {
					continue;
				}

				int xOffset = 0;
				int yOffset = 0;
				if(cell.horizontal == BoxAlignment.CENTER) {
					xOffset = (colWidths.getOrDefault(col, 0) - clientComponent.getWidth(font)) / 2;
				} else if(cell.horizontal == BoxAlignment.END) {
					xOffset = colWidths.getOrDefault(col, 0) - clientComponent.getWidth(font);
				}
				if(cell.vertical == BoxAlignment.CENTER) {
					yOffset = (rowHeights.getOrDefault(row, 0) - clientComponent.getHeight()) / 2;
				} else if(cell.vertical == BoxAlignment.END) {
					yOffset = rowHeights.getOrDefault(row, 0) - clientComponent.getHeight();
				}

				clientComponent.renderImage(font, xPos + xOffset, yPos + yOffset, guiGraphics);
			}
		}

	}

	@Override
	public int getHeight() {
		return highestColumn;
	}

	@Override
	public int getWidth(Font font) {
		return longestRow;
	}
}

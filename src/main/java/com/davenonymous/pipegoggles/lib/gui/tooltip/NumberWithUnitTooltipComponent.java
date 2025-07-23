package com.davenonymous.pipegoggles.lib.gui.tooltip;

import java.util.List;

public class NumberWithUnitTooltipComponent extends StringTooltipComponent {

	public NumberWithUnitTooltipComponent(long number, int color) {
		super(formatNumber(number), color);
	}

	private static String formatNumber(long number) {
		List<String> units = List.of("", "k", "M", "G", "T", "P", "E", "Z", "Y");
		for(var unit : units) {
			if (number < 1000) {
				return number + unit;
			}
			number /= 1000;
		}
		if (number >= 1000) {
			return number + "Y"; // If it exceeds Yotta, just return the number with Y
		}
		return number + ""; // Fallback to just the number if it doesn't fit any unit
	}
}

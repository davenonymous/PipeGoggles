package com.davenonymous.pipegoggles.data;

import com.davenonymous.pipegoggles.config.Rules;
import com.davenonymous.pipegoggles.datacomponents.PipeGoggleDataComponent;

public class EnergyCosts {
	public static long getCost(PipeGoggleDataComponent data) {
		if(!Rules.requireEnergy) {
			return 0;
		}

		if(data.isDisabled()) {
			return 0;
		}

		if(data.enabledColors().isEmpty()) {
			return 0;
		}

		long rangeCost = ((long)data.range()) * Rules.energyPerRange;
		long colorCost = data.enabledColors().size() * (long)Rules.energyPerActiveColor;
		if(data.goggleMode() == EnumGoggleMode.ON) {
			colorCost = (long)(colorCost * 1.5f);
			rangeCost = (long)(rangeCost * 1.5f);
		}
		return rangeCost + colorCost;
	}
}

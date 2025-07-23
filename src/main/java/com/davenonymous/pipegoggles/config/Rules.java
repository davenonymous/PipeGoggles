package com.davenonymous.pipegoggles.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class Rules {
	public static ModConfigSpec.BooleanValue REQUIRE_ENERGY;
	public static ModConfigSpec.IntValue MAX_ENERGY;
	public static ModConfigSpec.IntValue MAX_ENERGY_RECEIVE;
	public static ModConfigSpec.IntValue ENERGY_PER_RANGE;
	public static ModConfigSpec.IntValue ENERGY_PER_ACTIVE_COLOR;

	public static ModConfigSpec.ConfigValue<List<? extends Integer>> RANGE_OPTIONS;

	public static boolean requireEnergy;
	public static int maxEnergy;
	public static int maxEnergyReceive;

	public static int energyPerRange;
	public static int energyPerActiveColor;

	public static List<Integer> rangeOptions;

	public Rules(ModConfigSpec.Builder builder) {
		builder.push("rules");

		REQUIRE_ENERGY = builder
				.comment("If true, Pipe Goggles require energy to operate. If false, they will not consume any energy.")
				.translation("pipegoggles.configuration.rules.require_energy")
				.define("requireEnergy", true);

		MAX_ENERGY = builder
				.comment("The maximum amount of energy a Pipe Goggle can store.")
				.translation("pipegoggles.configuration.rules.max_energy")
				.defineInRange("maxEnergy", 400_000, 0, Integer.MAX_VALUE);

		MAX_ENERGY_RECEIVE = builder
				.comment("The maximum amount of energy a Pipe Goggle can receive per tick.")
				.translation("pipegoggles.configuration.rules.max_energy_receive")
				.defineInRange("maxEnergyReceive", 5_000, 1, Integer.MAX_VALUE);

		ENERGY_PER_RANGE = builder
				.comment("The amount of energy consumed per range unit.")
				.translation("pipegoggles.configuration.rules.energy_per_range")
				.defineInRange("energyPerRange", 2, 0, Integer.MAX_VALUE);

		ENERGY_PER_ACTIVE_COLOR = builder
				.comment("The amount of energy consumed per active color.")
				.translation("pipegoggles.configuration.rules.energy_per_active_color")
				.defineInRange("energyPerActiveColor", 2, 0, Integer.MAX_VALUE);

		RANGE_OPTIONS = builder
				.comment("The available range options for Pipe Goggles. The first value is the minimum range and the last value is the maximum range.")
				.translation("pipegoggles.configuration.rules.range_options")
				.defineList("rangeOptions", List.of(4, 6, 8, 12, 16), () -> 0, i -> i instanceof Integer in && in >= 4 && in <= 64);

		builder.pop();
	}

	public void load() {
		requireEnergy = REQUIRE_ENERGY.get();
		maxEnergy = MAX_ENERGY.get();
		maxEnergyReceive = MAX_ENERGY_RECEIVE.get();

		energyPerRange = ENERGY_PER_RANGE.get();
		energyPerActiveColor = ENERGY_PER_ACTIVE_COLOR.get();

		rangeOptions = new ArrayList<>(new HashSet<>(RANGE_OPTIONS.get()));

		// Ensure we have at least two range options (min and max)
		if(rangeOptions.isEmpty()) {
			rangeOptions.add(4);
			rangeOptions.add(16); // Default minimum and maximum if none are provided
		}

		if(rangeOptions.size() == 1) {
			var existing = rangeOptions.getFirst();
			if(existing * 2 > 64) {
				rangeOptions.add(existing / 2);
			} else {
				rangeOptions.add(existing * 2);
			}
		}
		rangeOptions.sort(Comparator.naturalOrder());

		minRange = rangeOptions.getFirst();
		maxRange = rangeOptions.getLast();
	}

	// These two are calculated from the rangeOptions and are not directly configurable
	public static int minRange = 4;
	public static int maxRange = 16;
}

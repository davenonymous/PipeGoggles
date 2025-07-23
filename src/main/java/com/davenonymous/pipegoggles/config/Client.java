package com.davenonymous.pipegoggles.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Client {
	public static ModConfigSpec.BooleanValue SHOW_HELP_TOOLTIPS;
	public static ModConfigSpec.BooleanValue SCAN_RADIALLY;
	public static ModConfigSpec.BooleanValue AUTO_MODE_REQUIRES_CROUCHING;
	public static ModConfigSpec.IntValue SCAN_REFRESH_RATE;


	public static boolean showHelpTooltips = true;
	public static boolean scanRadially = true;
	public static boolean autoModeRequiresCrouching = true;
	public static int scanRefreshRate = 10;

	public Client(ModConfigSpec.Builder builder) {
		builder.push("client");

		SCAN_RADIALLY = builder
				.comment("If true, the scan will be performed radially around the player. If false, it will scan rectangular.")
				.translation("pipegoggles.configuration.client.scan_radially")
				.define("scanRadially", true);

		SHOW_HELP_TOOLTIPS = builder
				.comment("Show help tooltips in the GUI")
				.translation("pipegoggles.configuration.client.show_help_tooltips")
				.define("showHelpTooltips", true);

		SCAN_REFRESH_RATE = builder
				.comment("The refresh rate of the scan for nearby cables in ticks (20 ticks = 1 second, max 1200 ticks = 1 minute)")
				.translation("pipegoggles.configuration.client.scan_refresh_rate")
				.defineInRange("scanRefreshRate", 10, 1, 1200);

		AUTO_MODE_REQUIRES_CROUCHING = builder
				.comment("If true, the auto mode only works when the player is crouching")
				.translation("pipegoggles.configuration.client.auto_mode_requires_crouching")
				.define("autoModeRequiresCrouching", true);

		builder.pop();
	}

	public void load() {
		showHelpTooltips = SHOW_HELP_TOOLTIPS.get();
		scanRadially = SCAN_RADIALLY.get();
		autoModeRequiresCrouching = AUTO_MODE_REQUIRES_CROUCHING.get();
		scanRefreshRate = SCAN_REFRESH_RATE.get();
	}
}

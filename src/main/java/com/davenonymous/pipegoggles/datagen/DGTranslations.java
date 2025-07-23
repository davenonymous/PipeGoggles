package com.davenonymous.pipegoggles.datagen;

import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.config.Client;
import com.davenonymous.pipegoggles.config.Rules;
import com.davenonymous.pipegoggles.data.EnumBoxOptimizationStrategy;
import com.davenonymous.pipegoggles.data.EnumGoggleMode;
import com.davenonymous.pipegoggles.setup.ModContainers;
import com.davenonymous.pipegoggles.setup.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.jetbrains.annotations.NotNull;

public class DGTranslations extends LanguageProvider {
	public DGTranslations(PackOutput output, String modid, String locale) {
		super(output, modid, locale);
	}

	@Override
	protected void addTranslations() {
		String pipeGoggles = "Pipe Goggles";
		add(ModItems.PIPE_GOGGLE_ITEM.get(), pipeGoggles);
		add(ModContainers.PIPE_GOGGLE_CONTAINER.get(), pipeGoggles);

		add(Client.SCAN_REFRESH_RATE, "Scan rate", "The refresh rate of the scan for nearby cables in ticks.");
		add(Client.SHOW_HELP_TOOLTIPS, "Show help", "Show help tooltips in the GUI.");
		add(Client.AUTO_MODE_REQUIRES_CROUCHING, "Auto mode only if crouching", "If true, the auto mode only works when you are crouching.");
		add(Client.SCAN_RADIALLY, "Scan radially", "If enabled the scan will be performed radially around the player. Otherwise it will scan rectangular.");

		add(Rules.ENERGY_PER_ACTIVE_COLOR, "Energy per active color", "The amount of energy consumed per active color.");
		add(Rules.ENERGY_PER_RANGE, "Energy per range", "The amount of energy consumed per range.");
		add(Rules.MAX_ENERGY, "Max energy", "The maximum amount of energy a Pipe Goggle can store.");
		add(Rules.MAX_ENERGY_RECEIVE, "Max energy receive", "The maximum amount of energy a Pipe Goggle can receive per tick.");
		add(Rules.REQUIRE_ENERGY, "Require energy", "If enabled Pipe Goggles require energy to operate.");
		add(Rules.RANGE_OPTIONS, "Range options", "The available range options for Pipe Goggles. "
				+ "This list should have at least 2 values, one being the minimum range and the other being the maximum range.");

		add("pipegoggles.configuration.rules", "Rules");
		add("pipegoggles.configuration.client", "Client Settings");

		add("pipegoggles.tooltip_1", "See nearby pipes through walls");
		add("pipegoggles.tooltip_2", "Right-click to configure");
		add("pipegoggles.tooltip_3", "Shift right-click to toggle on/off");

		add("pipegoggles.message.enable_goggles", "Click to enable goggles");
		add("pipegoggles.message.disable_goggles", "Click to disable goggles");
		add("pipegoggles.message.click_to_enable", "Click to enable");
		add("pipegoggles.message.click_to_disable", "Click to disable");
		add("pipegoggles.message.right_click_to_remove", "Right-click to remove");
		add("pipegoggles.message.changed_mode", "Changed mode to: %s");

		add("pipegoggles.message.always_on", "The goggles always show all enabled colors.");
		add("pipegoggles.message.auto_mode", "Only enabled colors matching the item in your hand will be shown.");
		add("pipegoggles.message.always_off", "The goggles are completely disabled.");
		add("pipegoggles.message.opacity", "Opacity:");
		add("pipegoggles.message.line_width", "Line Width:");
		add("pipegoggles.message.auto_mode_crouch", "Only works when crouching!");
		add("pipegoggles.message.stored_energy", "Stored Energy");

		add("pipegoggles.message.fabulous_warning", "Pipe Goggles do not work when it's raining with fabulous graphics!");

		add("pipegoggles.help.insert_items", "Insert items from the mod you want to see through the goggles");
		add("pipegoggles.help.set_range", "Set the range of the goggles");
		add("pipegoggles.help.keep_powered", "Keep the goggles powered with FE to maintain their functionality");

		add("curios.identifier.pipegoggles", pipeGoggles);

		add(EnumGoggleMode.OFF, "Off");
		add(EnumGoggleMode.ON, "On");
		add(EnumGoggleMode.SMART, "Smart");

		add(EnumBoxOptimizationStrategy.REMOVE_DUPLICATE_LINES, "Remove duplicate lines");
		add(EnumBoxOptimizationStrategy.SKIP_DUPLICATE_LINES, "Skip duplicate lines");
	}

	public void add(MenuType<?> container, String translation) {
		add(getContainerLanguageKey(container), translation);
	}

	public void add(@NotNull ModConfigSpec.ConfigValue<?> spec, String label, String tooltip) {
		var key = spec.getSpec().getTranslationKey();
		if (key == null || key.isEmpty()) {
			throw new IllegalArgumentException("Spec translation key cannot be null or empty");
		}

		add(key, label);
		add(key + ".tooltip", tooltip);
	}

	public <T extends Enum<?> & StringRepresentable> void add(T enumValue, String translation) {
		String key = PipeGoggles.MODID + "." + enumValue.getClass().getSimpleName().toLowerCase() + "." + enumValue.getSerializedName();
		add(key, translation);
	}

	public static String getContainerLanguageKey(MenuType<?> container) {
		ResourceLocation rLoc = BuiltInRegistries.MENU.getKey(container);
		return "container." + rLoc.getNamespace() + "." + rLoc.getPath();
	}
}

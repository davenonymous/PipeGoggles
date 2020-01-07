package com.davenonymous.pipegoggles.setup;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber
public class Config {
    public static final String CATEGORY_GENERAL = "general";

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.DoubleValue DRAW_LINE_WIDTH;
    public static ForgeConfigSpec.IntValue CACHE_TTL;
    public static ForgeConfigSpec.ConfigValue<List<String>> LINE_COLORS;

    public static ForgeConfigSpec.ConfigValue<List<Integer>> RANGE_CHOICES;


    static {
        COMMON_BUILDER.comment("General Settings").push(CATEGORY_GENERAL);
        setupGeneralConfig(COMMON_BUILDER);
        COMMON_BUILDER.pop();
        COMMON_CONFIG = COMMON_BUILDER.build();

        CLIENT_BUILDER.comment("General Settings").push(CATEGORY_GENERAL);
        setupClientConfig(CLIENT_BUILDER);
        CLIENT_BUILDER.pop();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    private static void setupGeneralConfig(ForgeConfigSpec.Builder b) {
        DRAW_LINE_WIDTH = b.comment("How thick the lines should be drawn").defineInRange("lineWidth", 1.5d, 0.1d, 4.0d);
        CACHE_TTL = b.comment("How long to cache nearby pipes").defineInRange("cacheTTL", 20, 0, 1000);
        LINE_COLORS = b.comment("Color of the pipe outlines (RGBA in hex)").define("lineColors", Arrays.asList("FF000080", "00FF0080", "0000FF80", "FFFF0080"));
    }

    private static void setupClientConfig(ForgeConfigSpec.Builder b) {
        RANGE_CHOICES = b.comment("List of valid range values. Please note that high values can have significant impact on FPS!").define("validRanges", Arrays.asList(4, 8, 16));
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {

        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {

    }

    @SubscribeEvent
    public static void onReload(final ModConfig.ConfigReloading configEvent) {
    }
}

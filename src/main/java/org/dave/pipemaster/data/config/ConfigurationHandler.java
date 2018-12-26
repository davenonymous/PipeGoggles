package org.dave.pipemaster.data.config;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.dave.pipemaster.PipeMaster;
import org.dave.pipemaster.items.goggles.PipeGogglesConfigOptions;
import org.dave.pipemaster.util.Logz;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigurationHandler {

    public static Configuration configuration;
    public static File baseDirectory;
    public static File blockGroupsDirectory;

    public static void init(File configFile) {
        if(configuration != null) {
            return;
        }

        baseDirectory = new File(configFile.getParentFile(), "pipemaster");
        if(!baseDirectory.exists()) {
            baseDirectory.mkdir();
        }

        blockGroupsDirectory = new File(baseDirectory, "blockgroups");
        if(!blockGroupsDirectory.exists()) {
            blockGroupsDirectory.mkdir();
        }

        configuration = new Configuration(new File(baseDirectory, "settings.cfg"), null);
        loadConfiguration();
    }

    private static void loadConfiguration() {
        Logz.info("Loading configuration");

        PipeGogglesConfigOptions.optimizerColorsHex[0] = configuration.getString("optimizerAColorHex", PipeGogglesConfigOptions.PIPE_GOOGLE_CATEGORY, "FF000080", "Color of the first pipe outlines (RGBA in hex)");
        PipeGogglesConfigOptions.optimizerColorsHex[1] = configuration.getString("optimizerBColorHex", PipeGogglesConfigOptions.PIPE_GOOGLE_CATEGORY, "00FF0080", "Color of the second pipe outlines (RGBA in hex)");
        PipeGogglesConfigOptions.optimizerColorsHex[2] = configuration.getString("optimizerCColorHex", PipeGogglesConfigOptions.PIPE_GOOGLE_CATEGORY, "0000FF80", "Color of the third pipe outlines (RGBA in hex)");
        PipeGogglesConfigOptions.optimizerColorsHex[3] = configuration.getString("optimizerDColorHex", PipeGogglesConfigOptions.PIPE_GOOGLE_CATEGORY, "FFFF0080", "Color of the fourth pipe outlines (RGBA in hex)");

        PipeGogglesConfigOptions.cacheTTL = configuration.getInt("cacheTTL", PipeGogglesConfigOptions.PIPE_GOOGLE_CATEGORY, 5, 0, 1000, "How long to cache nearby pipes");
        PipeGogglesConfigOptions.lineWidth = configuration.getFloat("lineWidth", PipeGogglesConfigOptions.PIPE_GOOGLE_CATEGORY, 1.5f, 0.1f, 5f, "How thick the lines should be drawn");

        String validRanges = configuration.getString("validRanges", PipeGogglesConfigOptions.PIPE_GOOGLE_CATEGORY, "4,8,16", "Comma separated list of valid range values. Please note that high values can have significant impact on FPS!");

        try {
            PipeGogglesConfigOptions.validRanges = Arrays.asList(validRanges.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
        } catch (RuntimeException e) {
            Logz.warn("Unable to interpret config value. Using default: 4,8,16");
            PipeGogglesConfigOptions.validRanges = Arrays.asList(4, 8, 16);
        }

        if(configuration.hasChanged()) {
            configuration.save();
        }
    }

    public static void saveConfiguration() {
        Logz.info("Saving configuration");
        configuration.save();
    }

    @SubscribeEvent
    public static void onConfigurationChanged(ConfigChangedEvent event) {
        if(!event.getModID().equalsIgnoreCase(PipeMaster.MODID)) {
            return;
        }

        loadConfiguration();
    }

    public static java.util.List<IConfigElement> getConfigElements() {
        List<IConfigElement> result = new ArrayList<>();
        result.add(new ConfigElement(configuration.getCategory(PipeGogglesConfigOptions.PIPE_GOOGLE_CATEGORY)));
        return result;
    }

    public static Color hex2Rgb(String colorStr) {
        String shorted = colorStr.replaceAll("#", "");
        try {
            if (shorted.length() == 8) {
                return new Color(
                        Integer.valueOf(shorted.substring(0, 2), 16),
                        Integer.valueOf(shorted.substring(2, 4), 16),
                        Integer.valueOf(shorted.substring(4, 6), 16),
                        Integer.valueOf(shorted.substring(6, 8), 16)
                );
            } else if (shorted.length() == 6) {
                return new Color(
                        Integer.valueOf(shorted.substring(0, 2), 16),
                        Integer.valueOf(shorted.substring(2, 4), 16),
                        Integer.valueOf(shorted.substring(4, 6), 16)
                );
            }
        } catch (StringIndexOutOfBoundsException e) {
            Logz.warn("Color String is misformatted: %s", colorStr);
        }

        return Color.MAGENTA;
    }
}

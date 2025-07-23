package com.davenonymous.pipegoggles.lib.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

public class ColorHelper {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final int COLOR_GREEN = 0xFF48BB3C;
	public static final int COLOR_ORANGE = 0xFFE6B300;
	public static final int COLOR_CYAN = 0xFF00A6BE;
	public static final int COLOR_PURPLE = 0xFF9B59B6;

	public static Color COLOR_ENABLED = new Color(50, 125, 50);
	public static Color COLOR_DISABLED = new Color(160, 160, 160, 255);
	public static Color COLOR_ERRORED = new Color(150, 50, 50);

	public static int rainbow(float time, float saturation, float brightness) {
		float hue = (time % 360.0f) / 360.0f;
		return Color.getHSBColor(hue, saturation, brightness).getRGB();
	}

	public static Color hex2Rgb(String colorStr) {
		if(colorStr == null) {
			LOGGER.warn("Color String is null");
			return Color.MAGENTA;
		}

		String shorted = colorStr.replaceAll("#", "");
		try {
			if(shorted.length() == 8) {
				return new Color(
					Integer.valueOf(shorted.substring(0, 2), 16), Integer.valueOf(shorted.substring(2, 4), 16), Integer.valueOf(shorted.substring(4, 6), 16),
					Integer.valueOf(shorted.substring(6, 8), 16)
				);
			} else if(shorted.length() == 6) {
				return new Color(Integer.valueOf(shorted.substring(0, 2), 16), Integer.valueOf(shorted.substring(2, 4), 16), Integer.valueOf(shorted.substring(4, 6), 16));
			}
		} catch (StringIndexOutOfBoundsException e) {
			LOGGER.warn("Color String is misformatted: %s", colorStr);
		}

		return Color.MAGENTA;
	}
}

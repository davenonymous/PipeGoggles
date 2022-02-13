package com.davenonymous.pipegoggles.datagen.client;


import com.davenonymous.libnonymous.base.BaseLanguageProvider;
import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.setup.Registration;
import net.minecraft.data.DataGenerator;

public class DatagenTranslations extends BaseLanguageProvider {
	public DatagenTranslations(DataGenerator gen, String locale) {
		super(gen, PipeGoggles.MODID, locale);
	}

	@Override
	protected void addTranslations() {
		add(Registration.PIPE_GOOGLES.get(), "Pipe Goggles");
		add(Registration.PIPEGOGGLES_CONTAINER.get(), "Pipe Goggles");

		add("pipegoggles.tooltip_1", "See nearby pipes through walls");
		add("pipegoggles.tooltip_2", "Right-click to configure");
	}
}
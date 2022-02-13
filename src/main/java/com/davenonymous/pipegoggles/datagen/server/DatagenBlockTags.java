package com.davenonymous.pipegoggles.datagen.server;

import com.davenonymous.pipegoggles.PipeGoggles;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class DatagenBlockTags extends BlockTagsProvider {
	public DatagenBlockTags(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, PipeGoggles.MODID, existingFileHelper);
	}

	@Override
	protected void addTags() {

	}

	@Override
	public String getName() {
		return "Pipe Goggles BlockTags";
	}
}
package com.davenonymous.pipegoggles.datagen.server;

import com.davenonymous.pipegoggles.PipeGoggles;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class DatagenItemTags extends ItemTagsProvider {
	public DatagenItemTags(DataGenerator generator, BlockTagsProvider blockTags, ExistingFileHelper existingFileHelper) {
		super(generator, blockTags, PipeGoggles.MODID, existingFileHelper);
	}

	@Override
	protected void addTags() {

	}

	@Override
	public String getName() {
		return "Pipe Goggles ItemTags";
	}
}
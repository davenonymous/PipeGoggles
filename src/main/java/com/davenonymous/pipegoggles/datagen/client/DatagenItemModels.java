package com.davenonymous.pipegoggles.datagen.client;

import com.davenonymous.pipegoggles.PipeGoggles;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class DatagenItemModels extends ItemModelProvider {
	public DatagenItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, PipeGoggles.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		//withExistingParent(Registration.BONSAI_POT_ITEM.get().getRegistryName().getPath(), modLoc("block/bonsaipot"));
	}
}
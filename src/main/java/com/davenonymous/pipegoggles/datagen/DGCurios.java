package com.davenonymous.pipegoggles.datagen;

import com.davenonymous.pipegoggles.PipeGoggles;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.concurrent.CompletableFuture;

public class DGCurios extends CuriosDataProvider {
	public DGCurios(String modId, PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
		super(modId, output, fileHelper, registries);
	}

	@Override
	public void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper) {
		this.createSlot("pipegoggles")
			.size(1)
			.dropRule(ICurio.DropRule.ALWAYS_KEEP)
			.icon(PipeGoggles.resource("slot/pipegoggles_slot"))
			.addValidator(ResourceLocation.fromNamespaceAndPath("curios", "tag"));
		this.createEntities("pipegoggles").addPlayer().addSlots("pipegoggles");

	}
}

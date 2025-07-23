package com.davenonymous.pipegoggles.datagen;

import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.data.BlockList;
import com.davenonymous.pipegoggles.data.EnumBoxOptimizationStrategy;
import com.davenonymous.pipegoggles.data.GoggleSupport;
import com.davenonymous.pipegoggles.data.ItemList;
import com.davenonymous.pipegoggles.setup.ModRegistries;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DGRegistries extends RegistrySetBuilder {
	public Map<ResourceKey<?>, List<ICondition>> conditions = new HashMap<>();
	public Map<ResourceKey<?>, GoggleSupport> supportData = new HashMap<>();

	public DGRegistries() {
		simpleSupport("integrateddynamics", "integrateddynamics:cable");
		simpleSupport("enderio", "enderio:conduit");
		simpleSupport("fluxnetworks", "fluxnetworks:flux_point", "fluxnetworks:flux_plug");
		simpleSupport("modern_industrialization", "modern_industrialization:pipe");
		simpleSupport("pipez", "pipez:universal_pipe", "pipez:energy_pipe", "pipez:fluid_pipe", "pipez:item_pipe", "pipez:gas_pipe");

		simpleSupport("laserio", "laserio:laser_connector", "laserio:laser_connector_advanced", "laserio:laser_node");


		support("xnet",
			BlockList.of("xnet:netcable", "xnet:connector"),
			ItemList.of("#xnet:cables", "#xnet:connectors"));

		support("refinedstorage",
			BlockList.of("refinedstorage:cable"),
			ItemList.of("#refinedstorage:cables"));

		support("ae2",
			BlockList.of("ae2:cable_bus"),
			ItemList.of("#ae2:glass_cable", "#ae2:covered_cable", "#ae2:covered_dense_cable", "#ae2:smart_cable", "#ae2:smart_dense_cable"));

		addMekanismSupport();
		addMinecraftRedstoneSupport();
		addMoreRedSupport();

		build();
	}

	public void addMoreRedSupport() {
		BlockList blockList = BlockList.ofDyes("morered:%s_network_cable");
		blockList.add("morered:red_alloy_wire");
		blockList.add("morered:bundled_network_cable");

		ItemList itemList = ItemList.of(
			"#morered:red_alloy_wires",
			"#morered:network_cables"
		);

		support("morered", blockList, itemList);
	}

	public void addMekanismSupport() {
		var types = List.of("logistical_transporter", "universal_cable", "mechanical_pipe", "pressurized_tube", "thermodynamic_conductor");
		var levels = List.of("basic", "advanced", "elite", "ultimate");

		BlockList blocks = new BlockList(new ArrayList<>());
		for(var level : levels) {
			for(var type : types) {
				blocks.add("mekanism:" + level + "_" + type);
			}

		}
		support("mekanism", blocks, ItemList.of());
	}

	public void addMinecraftRedstoneSupport() {
		var blockList = BlockList.of(
			"minecraft:redstone_block",
			"minecraft:redstone_wire",
			"minecraft:redstone_lamp",
			"minecraft:redstone_torch",
			"minecraft:repeater",
			"minecraft:comparator",
			"minecraft:lever",
			"minecraft:stone_button"
		);

		var itemList = ItemList.of(
			"#c:dusts/redstone"
		);

		var supportData = new GoggleSupport(
			"minecraft",
			EnumBoxOptimizationStrategy.REMOVE_DUPLICATE_LINES,
			blockList,
			itemList,
			true
		);

		addGoggleSupport("minecraft_redstone", supportData);
	}

	public void simpleSupportForMod(String groupName, String modId, String... blocks) {
		GoggleSupport simpleSupport = new GoggleSupport(
			modId,
			EnumBoxOptimizationStrategy.REMOVE_DUPLICATE_LINES,
			BlockList.of(blocks),
			ItemList.of(),
			true
		);

		addGoggleSupport(groupName, simpleSupport);
	}

	public void support(String groupName, BlockList blocks, ItemList items) {
		GoggleSupport support = new GoggleSupport(
			groupName,
			EnumBoxOptimizationStrategy.REMOVE_DUPLICATE_LINES,
			blocks,
			items,
			true
		);

		addGoggleSupport(groupName, support);
	}

	public void support(String groupName, String modId, BlockList blocks, ItemList items) {
		GoggleSupport support = new GoggleSupport(
			modId,
			EnumBoxOptimizationStrategy.REMOVE_DUPLICATE_LINES,
			blocks,
			items,
			true
		);

		addGoggleSupport(groupName, support);
	}

	public void simpleSupport(String groupName, String... blocks) {
		GoggleSupport simpleSupport = new GoggleSupport(
			groupName,
			EnumBoxOptimizationStrategy.REMOVE_DUPLICATE_LINES,
			BlockList.of(blocks),
			ItemList.of(),
			true
		);

		addGoggleSupport(groupName, simpleSupport);
	}

	public void addGoggleSupport(String groupName, GoggleSupport data) {
		ResourceLocation location = PipeGoggles.resource(groupName);
		ResourceKey<GoggleSupport> goggleSupportKey = ResourceKey.create(ModRegistries.GOGGLE_SUPPORT_REGISTRY_KEY, location);
		conditions.put(goggleSupportKey, List.of(new ModLoadedCondition(data.modId())));
		supportData.put(goggleSupportKey, data);
	}

	public void build() {
		this.add(ModRegistries.GOGGLE_SUPPORT_REGISTRY_KEY, bootstrapContext -> {
			for(var resourceKey : supportData.keySet()) {
				bootstrapContext.register((ResourceKey<GoggleSupport>) resourceKey, supportData.get(resourceKey));
			}
		});
	}
}

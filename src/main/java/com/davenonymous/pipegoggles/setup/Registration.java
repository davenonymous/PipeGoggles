package com.davenonymous.pipegoggles.setup;

import com.davenonymous.pipegoggles.data.BlockGroup;
import com.davenonymous.pipegoggles.data.BlockGroupHelper;
import com.davenonymous.pipegoggles.data.BlockGroupSerializer;
import com.davenonymous.pipegoggles.gui.PipeGogglesContainer;
import com.davenonymous.pipegoggles.item.PipeGogglesItem;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.davenonymous.pipegoggles.PipeGoggles.MODID;

public class Registration {
	private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
	private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MODID);
	private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MODID);
	private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);


	public static void init() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		BLOCKS.register(bus);
		ITEMS.register(bus);
		BLOCK_ENTITIES.register(bus);
		CONTAINERS.register(bus);
		RECIPE_SERIALIZERS.register(bus);
	}

	public static RecipeType<BlockGroup> blockGroupRecipeType;
	public static final RegistryObject<RecipeSerializer<?>> blockGroupSerializer = RECIPE_SERIALIZERS.register("blockgroup", () -> new BlockGroupSerializer());

	public static final RegistryObject<Item> PIPE_GOOGLES = ITEMS.register("pipegoggles", PipeGogglesItem::new);

	public static final RegistryObject<MenuType<PipeGogglesContainer>> PIPEGOGGLES_CONTAINER = CONTAINERS.register("bonsaipot", () -> IForgeMenuType.create((windowId, inv, data) -> new PipeGogglesContainer(windowId, data.readBlockPos(), inv, inv.player)));

}
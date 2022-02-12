package com.davenonymous.pipegoggles.network;

import com.davenonymous.pipegoggles.PipeGoggles;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.List;
import java.util.Optional;

public class Networking {
	public static SimpleChannel INSTANCE;
	private static final String CHANNEL_NAME = "channel";
	private static int ID = 0;

	public static int nextID() {
		return ID++;
	}

	public static void registerMessages() {
		INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(PipeGoggles.MODID, CHANNEL_NAME), () -> "1.0", s -> true, s -> true);

	}

	/*
	public static void sendXXXToServer(BlockPos pos) {
		INSTANCE.sendToServer(new CutBonsaiPacket(pos));
	}
	*/

	/*
	public static void sendXXXToClipboard(Connection target, MultiblockBlockModel model) {
		INSTANCE.sendTo(new PacketModelToJson(model), target, NetworkDirection.PLAY_TO_CLIENT);
	}
	*/

}
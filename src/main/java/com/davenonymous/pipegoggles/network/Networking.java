package com.davenonymous.pipegoggles.network;

import com.davenonymous.pipegoggles.PipeGoggles;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class Networking {
	public static SimpleChannel INSTANCE;
	private static final String CHANNEL_NAME = "channel";
	private static int ID = 0;

	public static int nextID() {
		return ID++;
	}

	public static void registerMessages() {
		INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(PipeGoggles.MODID, CHANNEL_NAME), () -> "1.0", s -> true, s -> true);

		INSTANCE.registerMessage(nextID(), PacketUpdateBlockGroup.class, PacketUpdateBlockGroup::toBytes, PacketUpdateBlockGroup::new, PacketUpdateBlockGroup::handle);
		INSTANCE.registerMessage(nextID(), PacketUpdateRange.class, PacketUpdateRange::toBytes, PacketUpdateRange::new, PacketUpdateRange::handle);
	}

	public static void sendUpdateRangeMessage(int slot, int range) {
		INSTANCE.sendToServer(new PacketUpdateRange(slot, range));
	}

	public static void sendUpdateBlockGroupMessage(int slot, int groupNum, ResourceLocation blockGroupId) {
		INSTANCE.sendToServer(new PacketUpdateBlockGroup(slot, groupNum, blockGroupId));
	}
}
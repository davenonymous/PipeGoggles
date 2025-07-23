package com.davenonymous.pipegoggles.networking;

import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.datacomponents.PipeGoggleDataComponent;
import com.davenonymous.pipegoggles.setup.ModDataComponents;
import com.davenonymous.pipegoggles.setup.ModItems;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.Map;

public record RemoveStackForColorPacket(DyeColor color) implements CustomPacketPayload {
	public static final Type<RemoveStackForColorPacket> TYPE = new Type<>(PipeGoggles.resource("remove_stack_for_color"));

	public static final StreamCodec<RegistryFriendlyByteBuf, RemoveStackForColorPacket> STREAM_CODEC = StreamCodec.composite(
			DyeColor.STREAM_CODEC, RemoveStackForColorPacket::color,
			RemoveStackForColorPacket::new
	);

	public RemoveStackForColorPacket(DyeColor color) {
		this.color = color;
	}

	public static void handleOnServer(RemoveStackForColorPacket packet, IPayloadContext context) {
		context.enqueueWork(() -> {
			ItemStack goggleStack = context.player().getItemInHand(InteractionHand.MAIN_HAND);
			if (goggleStack.isEmpty() || !goggleStack.is(ModItems.PIPE_GOGGLE_ITEM.get())) {
				return;
			}

			if(goggleStack.has(ModDataComponents.PIPEGOGGLES_COMPONENT.get())) {
				var oldData = goggleStack.get(ModDataComponents.PIPEGOGGLES_COMPONENT.get());
				var newData = oldData.without(packet.color());
				goggleStack.set(ModDataComponents.PIPEGOGGLES_COMPONENT.get(), newData);
			}
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}

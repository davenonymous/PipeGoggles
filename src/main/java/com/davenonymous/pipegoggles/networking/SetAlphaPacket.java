package com.davenonymous.pipegoggles.networking;

import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.items.PipeGoggleItem;
import com.davenonymous.pipegoggles.setup.ModDataComponents;
import com.davenonymous.pipegoggles.setup.ModItems;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetAlphaPacket(int alpha) implements CustomPacketPayload {
	public static final Type<SetAlphaPacket> TYPE = new Type<>(PipeGoggles.resource("set_alpha"));

	public static final StreamCodec<RegistryFriendlyByteBuf, SetAlphaPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT, SetAlphaPacket::alpha,
			SetAlphaPacket::new
	);

	public static void handleOnServer(SetAlphaPacket packet, IPayloadContext context) {
		context.enqueueWork(() -> {
			ItemStack goggleStack = context.player().getItemInHand(InteractionHand.MAIN_HAND);
			if (goggleStack.isEmpty() || !goggleStack.is(ModItems.PIPE_GOGGLE_ITEM.get())) {
				return;
			}

			var data = PipeGoggleItem.data(goggleStack);
			var newData = data.withAlpha(packet.alpha);
			goggleStack.set(ModDataComponents.PIPEGOGGLES_COMPONENT.get(), newData);
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}

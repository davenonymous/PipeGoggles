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

public record SetLineWidthPacket(int lineWidth) implements CustomPacketPayload {
	public static final Type<SetLineWidthPacket> TYPE = new Type<>(PipeGoggles.resource("set_line_width"));

	public static final StreamCodec<RegistryFriendlyByteBuf, SetLineWidthPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT, SetLineWidthPacket::lineWidth,
			SetLineWidthPacket::new
	);

	public static void handleOnServer(SetLineWidthPacket packet, IPayloadContext context) {
		context.enqueueWork(() -> {
			ItemStack goggleStack = context.player().getItemInHand(InteractionHand.MAIN_HAND);
			if (goggleStack.isEmpty() || !goggleStack.is(ModItems.PIPE_GOGGLE_ITEM.get())) {
				return;
			}

			var data = PipeGoggleItem.data(goggleStack);
			var newData = data.withLineWidth(packet.lineWidth);
			goggleStack.set(ModDataComponents.PIPEGOGGLES_COMPONENT.get(), newData);
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}

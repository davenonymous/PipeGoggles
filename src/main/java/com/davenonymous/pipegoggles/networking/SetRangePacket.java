package com.davenonymous.pipegoggles.networking;

import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.config.Rules;
import com.davenonymous.pipegoggles.datacomponents.PipeGoggleDataComponent;
import com.davenonymous.pipegoggles.items.PipeGoggleItem;
import com.davenonymous.pipegoggles.setup.ModDataComponents;
import com.davenonymous.pipegoggles.setup.ModItems;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetRangePacket(float range) implements CustomPacketPayload {
	public static final Type<SetRangePacket> TYPE = new Type<>(PipeGoggles.resource("set_range"));

	public static final StreamCodec<RegistryFriendlyByteBuf, SetRangePacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.FLOAT, SetRangePacket::range,
			SetRangePacket::new
	);

	public static void handleOnServer(SetRangePacket packet, IPayloadContext context) {
		context.enqueueWork(() -> {
			ItemStack goggleStack = context.player().getItemInHand(InteractionHand.MAIN_HAND);
			if (goggleStack.isEmpty() || !goggleStack.is(ModItems.PIPE_GOGGLE_ITEM.get())) {
				return;
			}

			var data = PipeGoggleItem.data(goggleStack);
			var maxRange = Rules.rangeOptions.getLast();
			if (packet.range < 0 || packet.range > maxRange) {
				return;
			}

			var newData = data.withRange(packet.range());
			goggleStack.set(ModDataComponents.PIPEGOGGLES_COMPONENT.get(), newData);
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}

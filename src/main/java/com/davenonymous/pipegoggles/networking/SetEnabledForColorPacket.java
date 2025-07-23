package com.davenonymous.pipegoggles.networking;

import com.davenonymous.pipegoggles.PipeGoggles;
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

import java.util.HashMap;
import java.util.Map;

public record SetEnabledForColorPacket(DyeColor color, boolean enabled) implements CustomPacketPayload {
	public static final Type<SetEnabledForColorPacket> TYPE = new Type<>(PipeGoggles.resource("set_enabled_for_color"));

	public static final StreamCodec<RegistryFriendlyByteBuf, SetEnabledForColorPacket> STREAM_CODEC = StreamCodec.composite(
			DyeColor.STREAM_CODEC, SetEnabledForColorPacket::color,
			ByteBufCodecs.BOOL, SetEnabledForColorPacket::enabled,
			SetEnabledForColorPacket::new
	);

	public static void handleOnServer(SetEnabledForColorPacket packet, IPayloadContext context) {
		context.enqueueWork(() -> {
			ItemStack goggleStack = context.player().getItemInHand(InteractionHand.MAIN_HAND);
			if (goggleStack.isEmpty() || !goggleStack.is(ModItems.PIPE_GOGGLE_ITEM.get())) {
				return;
			}

			PipeGoggleDataComponent data = PipeGoggleItem.data(goggleStack);
			var newData = data.withColorEnabled(packet.color(), packet.enabled());
			goggleStack.set(ModDataComponents.PIPEGOGGLES_COMPONENT.get(), newData);
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}

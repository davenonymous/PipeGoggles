package com.davenonymous.pipegoggles.networking;

import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.datacomponents.PipeGoggleDataComponent;
import com.davenonymous.pipegoggles.items.PipeGoggleItem;
import com.davenonymous.pipegoggles.setup.ModDataComponents;
import com.davenonymous.pipegoggles.setup.ModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.Map;

public record SetStackForColorPacket(DyeColor color, ItemStack stack) implements CustomPacketPayload {
	public static final Type<SetStackForColorPacket> TYPE = new Type<>(PipeGoggles.resource("set_stack_for_color"));

	public static final StreamCodec<RegistryFriendlyByteBuf, SetStackForColorPacket> STREAM_CODEC = StreamCodec.composite(
			DyeColor.STREAM_CODEC, SetStackForColorPacket::color,
			ItemStack.STREAM_CODEC, SetStackForColorPacket::stack,
			SetStackForColorPacket::new
	);

	public SetStackForColorPacket(DyeColor color, ItemStack stack) {
		this.color = color;
		this.stack = stack.copy();
	}

	public SetStackForColorPacket(DyeColor color) {
		this(color, ItemStack.EMPTY);
	}

	public static void handleOnServer(SetStackForColorPacket packet, IPayloadContext context) {
		context.enqueueWork(() -> {
			ItemStack goggleStack = context.player().getItemInHand(InteractionHand.MAIN_HAND);
			if (goggleStack.isEmpty() || !goggleStack.is(ModItems.PIPE_GOGGLE_ITEM.get())) {
				return;
			}

			PipeGoggleDataComponent data = PipeGoggleItem.data(goggleStack);
			var newData = data.with(packet.color(), packet.stack());
			goggleStack.set(ModDataComponents.PIPEGOGGLES_COMPONENT.get(), newData);
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}

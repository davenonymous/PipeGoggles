package com.davenonymous.pipegoggles.networking;

import com.davenonymous.pipegoggles.PipeGoggles;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = PipeGoggles.MODID)
public class Register {
	@SubscribeEvent
	public static void register(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar("1");

		registrar.playToServer(SetStackForColorPacket.TYPE, SetStackForColorPacket.STREAM_CODEC, SetStackForColorPacket::handleOnServer);
		registrar.playToServer(RemoveStackForColorPacket.TYPE, RemoveStackForColorPacket.STREAM_CODEC, RemoveStackForColorPacket::handleOnServer);
		registrar.playToServer(SetRangePacket.TYPE, SetRangePacket.STREAM_CODEC, SetRangePacket::handleOnServer);
		registrar.playToServer(SetAlphaPacket.TYPE, SetAlphaPacket.STREAM_CODEC, SetAlphaPacket::handleOnServer);
		registrar.playToServer(SetLineWidthPacket.TYPE, SetLineWidthPacket.STREAM_CODEC, SetLineWidthPacket::handleOnServer);
		registrar.playToServer(SetModePacket.TYPE, SetModePacket.STREAM_CODEC, SetModePacket::handleOnServer);
		registrar.playToServer(SetEnabledForColorPacket.TYPE, SetEnabledForColorPacket.STREAM_CODEC, SetEnabledForColorPacket::handleOnServer);

	}
}

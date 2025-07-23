package com.davenonymous.pipegoggles.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.function.IntFunction;

public enum EnumGoggleMode implements StringRepresentable {
    OFF(0, "off"),
    ON(1, "on"),
	SMART(2, "smart"),;

	private final int id;
	private final String key;

	public static final IntFunction<EnumGoggleMode> BY_ID =
		ByIdMap.continuous(
			EnumGoggleMode::getId,
			EnumGoggleMode.values(),
			ByIdMap.OutOfBoundsStrategy.ZERO
		);

	public static final EnumCodec<EnumGoggleMode> CODEC = StringRepresentable.fromEnum(EnumGoggleMode::values);

	public static final StreamCodec<ByteBuf, EnumGoggleMode> STREAM_CODEC =
		ByteBufCodecs.idMapper(EnumGoggleMode.BY_ID, EnumGoggleMode::getId);

	EnumGoggleMode(int id, String key) {
		this.id = id;
		this.key = key;
	}

	public static EnumGoggleMode byId(int id) {
		return BY_ID.apply(id);
	}

	public int getId() {
		return this.id;
	}

	@Override
	public String getSerializedName() {
		return this.key;
	}

	public EnumGoggleMode next() {
		return byId(id + 1 % values().length);
	}
}

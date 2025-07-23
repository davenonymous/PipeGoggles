package com.davenonymous.pipegoggles.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.function.IntFunction;

public enum EnumBoxOptimizationStrategy implements StringRepresentable {
    SKIP_DUPLICATE_LINES(0, "skip_duplicate_lines"),
    REMOVE_DUPLICATE_LINES(1, "remove_duplicate_lines"),;

	private final int id;
	private final String key;

	public static final IntFunction<EnumBoxOptimizationStrategy> BY_ID =
		ByIdMap.continuous(
			EnumBoxOptimizationStrategy::getId,
			EnumBoxOptimizationStrategy.values(),
			ByIdMap.OutOfBoundsStrategy.ZERO
		);

	public static final EnumCodec<EnumBoxOptimizationStrategy> CODEC = StringRepresentable.fromEnum(EnumBoxOptimizationStrategy::values);

	public static final StreamCodec<ByteBuf, EnumBoxOptimizationStrategy> STREAM_CODEC =
		ByteBufCodecs.idMapper(EnumBoxOptimizationStrategy.BY_ID, EnumBoxOptimizationStrategy::getId);

	EnumBoxOptimizationStrategy(int id, String key) {
		this.id = id;
		this.key = key;
	}

	public static EnumBoxOptimizationStrategy byId(int id) {
		return BY_ID.apply(id);
	}

	public int getId() {
		return this.id;
	}

	@Override
	public String getSerializedName() {
		return this.key;
	}
}

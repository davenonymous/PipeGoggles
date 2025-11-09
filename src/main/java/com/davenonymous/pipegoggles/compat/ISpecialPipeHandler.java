package com.davenonymous.pipegoggles.compat;

import com.davenonymous.pipegoggles.data.GoggleSupport;
import com.davenonymous.pipegoggles.data.cache.BoxLineCache;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Set;

public interface ISpecialPipeHandler {
	default boolean onLineAdd(GoggleSupport support, Level level, BlockPos pos, ResourceLocation blockId, BlockState state, VoxelShape shape, Set<BoxLineCache.Line> lines, BoxLineCache.Line line) {
		return true;
	}

	default boolean onShapeAdd(GoggleSupport support, Level level, BlockPos pos, ResourceLocation blockId, BlockState state, VoxelShape shape, Set<BoxLineCache.Line> lines) {
		return true;
	}

	ISpecialPipeHandler NOOP = new ISpecialPipeHandler() {};
}

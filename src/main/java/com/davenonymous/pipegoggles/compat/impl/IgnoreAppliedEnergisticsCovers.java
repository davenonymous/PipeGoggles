package com.davenonymous.pipegoggles.compat.impl;

import com.davenonymous.pipegoggles.compat.ISpecialPipeHandler;
import com.davenonymous.pipegoggles.compat.SpecialPipeHandler;
import com.davenonymous.pipegoggles.data.GoggleSupport;
import com.davenonymous.pipegoggles.data.cache.BoxLineCache;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3f;

import java.util.Set;

@SpecialPipeHandler("ae2")
public class IgnoreAppliedEnergisticsCovers implements ISpecialPipeHandler {

	private boolean hasTwoComponentsWith(Vector3f vector, float... values) {
		int count = 0;
		for(float v : values) {
			if(vector.x == v) count++;
			if(vector.y == v) count++;
			if(vector.z == v) count++;
		}
		return count >= 2;
	}

	@Override
	public boolean onLineAdd(GoggleSupport support, Level level, BlockPos pos, ResourceLocation blockId, BlockState state, VoxelShape shape, Set<BoxLineCache.Line> lines, BoxLineCache.Line line) {
		var shiftedLine = line.shift(pos);
		if(hasTwoComponentsWith(shiftedLine.start().toVector3f(), 0.0f, 1.0f, 0.0625f, 1.0f - 0.0625f)) {
			return false;
		}

		if(hasTwoComponentsWith(shiftedLine.end().toVector3f(), 0.0f, 1.0f, 0.0625f, 1.0f - 0.0625f)) {
			return false;
		}

		return true;
	}
}

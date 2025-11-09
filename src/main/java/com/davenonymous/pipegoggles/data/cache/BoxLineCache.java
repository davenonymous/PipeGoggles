package com.davenonymous.pipegoggles.data.cache;

import com.davenonymous.pipegoggles.compat.ISpecialPipeHandler;
import com.davenonymous.pipegoggles.compat.SpecialPipeHandlers;
import com.davenonymous.pipegoggles.data.GoggleSupport;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.davenonymous.pipegoggles.data.EnumBoxOptimizationStrategy.REMOVE_DUPLICATE_LINES;

public class BoxLineCache {
	public DyeColor color;

	public Set<Line> lines = new HashSet<>();

	public BoxLineCache(DyeColor color) {
		this.color = color;
	}

	public void clear() {
		this.lines.clear();
	}

	public void addBlock(GoggleSupport support, BlockPos pos, Level level) {
		ISpecialPipeHandler handler = SpecialPipeHandlers.forMod(support.modId());
		BlockState blockState = level.getBlockState(pos);
		ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(blockState.getBlock());
		VoxelShape shape = blockState.getShape(level, pos);

		boolean shouldContinue = handler.onShapeAdd(support, level, pos, blockId, blockState, shape, lines);
		if(!shouldContinue) {
			return;
		}

		boolean removeDuplicateLines = support.strategy() == REMOVE_DUPLICATE_LINES;
		shape.forAllEdges((x1, y1, z1, x2, y2, z2) -> {
			var newLine = new Line(
				new Vec3(x1 + pos.getX(), y1 + pos.getY(), z1 + pos.getZ()),
				new Vec3(x2 + pos.getX(), y2 + pos.getY(), z2 + pos.getZ())
			);
			if(lines.contains(newLine)) {
				if(removeDuplicateLines) {
					lines.remove(newLine);
				}
			} else {
				boolean shouldAddLine = handler.onLineAdd(support, level, pos, blockId, blockState, shape, lines, newLine);
				if(shouldAddLine) {
					lines.add(newLine);
				}
			}
		});
	}

	public record Line(Vec3 start, Vec3 end) {
		public Line(Vec3 start, Vec3 end) {
			Vec3 origin = new Vec3(0,0,0);
			if(start.distanceTo(origin) < end.distanceTo(origin)) {
				this.start = start;
				this.end = end;
			} else {
				this.end = start;
				this.start = end;
			}
		}

		public Line shift(BlockPos offset) {
			return new Line(
				new Vec3(this.start().toVector3f()).subtract(offset.getX(), offset.getY(), offset.getZ()),
				new Vec3(this.end().toVector3f()).subtract(offset.getX(), offset.getY(), offset.getZ())
			);
		}

		@Override
		public boolean equals(Object o) {
			if(!(o instanceof Line line)) {
				return false;
			}
			return Objects.equals(end(), line.end()) && Objects.equals(start(), line.start());
		}

		@Override
		public int hashCode() {
			return Objects.hash(start(), end());
		}
	}

}

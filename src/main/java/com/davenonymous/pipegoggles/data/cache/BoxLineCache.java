package com.davenonymous.pipegoggles.data.cache;

import com.davenonymous.pipegoggles.data.GoggleSupport;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.HashSet;
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
		VoxelShape shape = level.getBlockState(pos).getShape(level, pos);
		shape.forAllEdges((x1, y1, z1, x2, y2, z2) -> {
			var newLine = new Line(
				new Vec3(x1 + pos.getX(), y1 + pos.getY(), z1 + pos.getZ()),
				new Vec3(x2 + pos.getX(), y2 + pos.getY(), z2 + pos.getZ())
			);
			if(lines.contains(newLine)) {
				if(support.strategy() == REMOVE_DUPLICATE_LINES) {
					lines.remove(newLine);
				}
			} else {
				lines.add(newLine);
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
	}

}

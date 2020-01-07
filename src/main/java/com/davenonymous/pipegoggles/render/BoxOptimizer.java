package com.davenonymous.pipegoggles.render;

import com.davenonymous.libnonymous.gui.framework.ColorHelper;
import com.davenonymous.libnonymous.utils.WorldTools;
import com.davenonymous.pipegoggles.data.BlockGroup;
import com.davenonymous.pipegoggles.data.EnumBoxOptimizationStrategy;
import com.davenonymous.pipegoggles.item.PipeGogglesItemData;
import com.davenonymous.pipegoggles.setup.Config;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

import java.awt.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class BoxOptimizer {
    Set<Line> lines = new DeletingHashSet<>();

    int channelId;
    long lastUpdateTick = Long.MIN_VALUE;
    Color color = Color.RED;

    public BoxOptimizer(int channelId) {
        this.channelId = channelId;
    }

    public void eventuallyUpdate(PlayerEntity player, Hand hand, PipeGogglesItemData data) {
        World world = player.world;
        boolean cacheIsCurrent = lastUpdateTick + Config.CACHE_TTL.get() >= world.getGameTime();
        boolean hasLines = lines.size() > 0;
        boolean lastUpdateTickIsInThePast = lastUpdateTick <= world.getGameTime();
        if(cacheIsCurrent && hasLines && lastUpdateTickIsInThePast) {
            // Still up to date and lines in the cache
            return;
        }

        BlockPos base = new BlockPos(player.posX, player.posY, player.posZ);
        BlockPos min = base.add(-data.range, -data.range, -data.range);
        BlockPos max = base.add(data.range, data.range, data.range);

        BlockGroup group = data.getBlockGroupForSlot(world.getRecipeManager(), channelId);
        if(group == null) {
            lines = Collections.emptySet();
            return;
        }

        if(group.getOptimizationStrategy() == EnumBoxOptimizationStrategy.SKIP_DUPLICATE_LINES) {
            lines = new HashSet<>();
        } else {
            lines = new DeletingHashSet<>();
        }

        WorldTools.foreachBlockBetween(min, max, (BlockPos pos) -> {
            if (world.isAirBlock(pos)) {
                return;
            }

            BlockState state = world.getBlockState(pos);
            if (group.containsBlockState(state)) {

                VoxelShape shape = world.getBlockState(pos).getShape(world, pos);
                shape.forEachEdge((x1, y1, z1, x2, y2, z2) -> {
                    lines.add(new Line(
                            new Vec3d(x1+pos.getX(), y1+pos.getY(), z1+pos.getZ()),
                            new Vec3d(x2+pos.getX(), y2+pos.getY(), z2+pos.getZ())
                    ));
                });
            }
        });

        this.color = ColorHelper.hex2Rgb(Config.LINE_COLORS.get().get(channelId));
        this.lastUpdateTick = world.getGameTime();
    }

    public void render(PlayerEntity player, float partialTicks) {
        BoxRenderer.renderLines(player, partialTicks, color, this.lines);
    }

    class DeletingHashSet<E> extends HashSet<E> {
        @Override
        public boolean add(E e) {
            if(!this.contains(e)) {
                return super.add(e);
            }

            this.remove(e);
            return false;
        }
    }

    class TwoPointGeoObject {
        public Vec3d start;
        public Vec3d end;

        public TwoPointGeoObject(Vec3d start, Vec3d end) {
            Vec3d origin = new Vec3d(0,0,0);
            if(start.distanceTo(origin) < end.distanceTo(origin)) {
                this.start = start;
                this.end = end;
            } else {
                this.end = start;
                this.start = end;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TwoPointGeoObject that = (TwoPointGeoObject) o;
            return Objects.equals(start, that.start) && Objects.equals(end, that.end);
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "{" +
                    "start=" + start +
                    ", end=" + end +
                    '}';
        }
    }

    class Square extends TwoPointGeoObject {
        public Square(Vec3d start, Vec3d end) {
            super(start, end);
        }
    }

    class Line extends TwoPointGeoObject {
        public Line(Vec3d start, Vec3d end) {
            super(start, end);
        }
    }
}

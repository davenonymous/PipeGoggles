package com.davenonymous.pipegoggles.render;

import com.davenonymous.libnonymous.gui.framework.ColorHelper;
import com.davenonymous.libnonymous.utils.WorldTools;
import com.davenonymous.pipegoggles.config.ClientConfig;
import com.davenonymous.pipegoggles.data.BlockGroup;
import com.davenonymous.pipegoggles.data.EnumBoxOptimizationStrategy;
import com.davenonymous.pipegoggles.item.PipeGogglesItemData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;


import java.awt.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class BoxOptimizer {
    public Set<Line> lines = new DeletingHashSet<>();

    int channelId;
    long lastUpdateTick = Long.MIN_VALUE;
    public Color color = Color.RED;

    public BoxOptimizer(int channelId) {
        this.channelId = channelId;
    }

    public void eventuallyUpdate(Player player, InteractionHand hand, PipeGogglesItemData data) {
        Level level = player.level;
        boolean cacheIsCurrent = lastUpdateTick + ClientConfig.CACHE_TTL.get() >= level.getGameTime();
        boolean hasLines = lines.size() > 0;
        boolean lastUpdateTickIsInThePast = lastUpdateTick <= level.getGameTime();
        if(cacheIsCurrent && hasLines && lastUpdateTickIsInThePast) {
            // Still up to date and lines in the cache
            return;
        }

        BlockPos base = new BlockPos(player.getX(), player.getY(), player.getZ());
        BlockPos min = base.offset(-data.range, -data.range, -data.range);
        BlockPos max = base.offset(data.range, data.range, data.range);

        BlockGroup group = data.getBlockGroupForSlot(level.getRecipeManager(), channelId);
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
            if (level.isEmptyBlock(pos)) {
                return;
            }

            BlockState state = level.getBlockState(pos);
            if (group.containsBlockState(state)) {

                VoxelShape shape = level.getBlockState(pos).getShape(level, pos);
                shape.forAllEdges((x1, y1, z1, x2, y2, z2) -> {
                    lines.add(new Line(
                            new Vec3(x1 + pos.getX(), y1 + pos.getY(), z1 + pos.getZ()),
                            new Vec3(x2 + pos.getX(), y2 + pos.getY(), z2 + pos.getZ())
                    ));
                });
            }
        });

        this.color = ColorHelper.hex2Rgb(ClientConfig.LINE_COLORS.get().get(channelId));
        this.lastUpdateTick = level.getGameTime();
    }

    public void render(PoseStack pose, Player player, float partialTicks, Matrix4f projectionMatrix) {
        BoxRenderer.renderLines(pose, player, partialTicks, color, this.lines, projectionMatrix);
    }

    static class DeletingHashSet<E> extends HashSet<E> {
        @Override
        public boolean add(E e) {
            if(!this.contains(e)) {
                return super.add(e);
            }

            this.remove(e);
            return false;
        }
    }

    static class TwoPointGeoObject {
        public Vec3 start;
        public Vec3 end;

        public TwoPointGeoObject(Vec3 start, Vec3 end) {
            Vec3 origin = new Vec3(0,0,0);
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

    static class Square extends TwoPointGeoObject {
        public Square(Vec3 start, Vec3 end) {
            super(start, end);
        }
    }

    static class Line extends TwoPointGeoObject {
        public Line(Vec3 start, Vec3 end) {
            super(start, end);
        }
    }
}
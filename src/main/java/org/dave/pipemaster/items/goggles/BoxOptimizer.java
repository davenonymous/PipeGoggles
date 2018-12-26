package org.dave.pipemaster.items.goggles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.dave.pipemaster.data.blockgroups.BlockGroup;
import org.dave.pipemaster.data.config.ConfigurationHandler;
import org.dave.pipemaster.util.Logz;
import org.dave.pipemaster.util.WorldTools;

import java.awt.*;
import java.util.*;
import java.util.List;

public class BoxOptimizer {
    Set<Line> lines = new DeletingHashSet<>();

    int channelId;
    long lastUpdateTick = Long.MIN_VALUE;
    Color color = Color.RED;

    public BoxOptimizer(int channelId) {
        this.channelId = channelId;
    }

    public void eventuallyUpdate(EntityPlayer player, EnumHand hand, PipeGogglesData data) {
        World world = player.world;
        boolean cacheIsCurrent = lastUpdateTick + PipeGogglesConfigOptions.cacheTTL >= world.getTotalWorldTime();
        boolean hasLines = lines.size() > 0;
        boolean lastUpdateTickIsInThePast = lastUpdateTick <= world.getTotalWorldTime();
        if(cacheIsCurrent && hasLines && lastUpdateTickIsInThePast) {
            // Still up to date and lines in the cache
            return;
        }

        BlockPos base = new BlockPos(player.posX, player.posY, player.posZ);
        BlockPos min = base.add(-data.getRange(), -data.getRange(), -data.getRange());
        BlockPos max = base.add(data.getRange(), data.getRange(), data.getRange());

        BlockGroup group = data.getBlockGroupForSlot(channelId);
        if(group.getOptimizationStrategy() == EnumBoxOptimizationStrategy.SKIP_DUPLICATE_LINES) {
            lines = new HashSet<>();
        } else {
            lines = new DeletingHashSet<>();
        }

        WorldTools.foreachBlockBetween(min, max, (BlockPos pos) -> {
            if (world.isAirBlock(pos)) {
                return;
            }

            IBlockState state = world.getBlockState(pos);
            if (group.containsBlockState(state)) {
                List<AxisAlignedBB> boxes = new ArrayList<>();
                world.getBlockState(pos).addCollisionBoxToList(world, pos, new AxisAlignedBB(pos), boxes, null, false);
                boxes.forEach(box -> this.addBox(box));
            }
        });

        this.color = ConfigurationHandler.hex2Rgb(PipeGogglesConfigOptions.optimizerColorsHex[channelId]);
        this.lastUpdateTick = world.getTotalWorldTime();
    }

    public void render(EntityPlayer player, float partialTicks) {
        BoxRenderer.renderLines(player, partialTicks, color, this.lines);
    }

    public void addBox(AxisAlignedBB box) {
        // 8 Corners
        Vec3d topFrontLeft = new Vec3d(box.maxX, box.maxY, box.maxZ);
        Vec3d topFrontRight = new Vec3d(box.maxX, box.maxY, box.minZ);
        Vec3d topBackRight = new Vec3d(box.maxX, box.minY, box.minZ);
        Vec3d topBackLeft = new Vec3d(box.maxX, box.minY, box.maxZ);
        Vec3d bottomBackRight = new Vec3d(box.minX, box.minY, box.minZ);
        Vec3d bottomBackLeft = new Vec3d(box.minX, box.minY, box.maxZ);
        Vec3d bottomFrontLeft = new Vec3d(box.minX, box.maxY, box.maxZ);
        Vec3d bottomFrontRight = new Vec3d(box.minX, box.maxY, box.minZ);

        /*
        // In case we ever need this, here is the code for the 6 Squares of the bounding box
        squares.add(new Square(topFrontLeft, bottomFrontRight));
        squares.add(new Square(topFrontLeft, topBackRight));
        squares.add(new Square(topFrontLeft, bottomBackLeft));

        squares.add(new Square(bottomBackRight, topFrontRight));
        squares.add(new Square(bottomBackRight, topBackLeft));
        squares.add(new Square(bottomBackRight, bottomFrontLeft));
        */

        // 12 lines
        lines.add(new Line(topFrontLeft, topFrontRight));
        lines.add(new Line(topFrontLeft, topBackLeft));
        lines.add(new Line(topFrontLeft, bottomFrontLeft));

        lines.add(new Line(bottomFrontRight, topFrontRight));
        lines.add(new Line(bottomFrontRight, bottomBackRight));
        lines.add(new Line(bottomFrontRight, bottomFrontLeft));

        lines.add(new Line(bottomBackLeft, topBackLeft));
        lines.add(new Line(bottomBackLeft, bottomBackRight));
        lines.add(new Line(bottomBackLeft, bottomFrontLeft));

        lines.add(new Line(topBackRight, topBackLeft));
        lines.add(new Line(topBackRight, topFrontRight));
        lines.add(new Line(topBackRight, bottomBackRight));
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

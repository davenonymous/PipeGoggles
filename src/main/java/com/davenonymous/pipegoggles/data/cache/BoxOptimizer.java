package com.davenonymous.pipegoggles.data.cache;

import com.davenonymous.pipegoggles.config.Client;
import com.davenonymous.pipegoggles.data.GoggleSupport;
import com.davenonymous.pipegoggles.data.WorldTools;
import com.davenonymous.pipegoggles.datacomponents.PipeGoggleDataComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BoxOptimizer {
	private static Map<DyeColor, BoxLineCache> BOX_LINE_CACHES = new HashMap<>();
	private static long lastUpdateTick = Long.MIN_VALUE;

	public static BoxLineCache getBoxLineCache(DyeColor color) {
		if(BOX_LINE_CACHES.isEmpty()) {
			for(DyeColor dyeColor : DyeColor.values()) {
				BOX_LINE_CACHES.put(dyeColor, new BoxLineCache(dyeColor));
			}
		}

		return BOX_LINE_CACHES.get(color);
	}

	public static void updateBoxLineCaches(Player player, PipeGoggleDataComponent data) {
		Level level = player.level();
		boolean cacheIsCurrent = lastUpdateTick + Client.scanRefreshRate >= level.getGameTime();
		boolean lastUpdateTickIsInThePast = lastUpdateTick <= level.getGameTime();
		if(cacheIsCurrent && lastUpdateTickIsInThePast) {
			// Still up to date
			return;
		}

		BlockPos base = player.blockPosition();
		double rangeSqr = data.range() * data.range();
		int intRange = (int) data.range();
		BlockPos min = base.offset(-intRange, -intRange, -intRange);
		BlockPos max = base.offset(intRange, intRange, intRange);

		for(DyeColor color : DyeColor.values()) {
			var optimizer = getBoxLineCache(color);
			optimizer.clear();
		}

		WorldTools.foreachBlockBetween(min, max, (BlockPos pos) -> {
			if (level.isEmptyBlock(pos)) {
				return;
			}

			// Radial distance check
			if(Client.scanRadially && player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) > rangeSqr) {
				// Outside of range
				return;
			}

			BlockState state = level.getBlockState(pos);
			GoggleSupport support = GoggleSupportCache.SUPPORT_BY_BLOCK.get(state.getBlock());
			if (support == null) {
				return; // Not supported by goggles
			}


			Optional<DyeColor> color = data.getColorForBlock(state.getBlock());
			if (color.isEmpty()) {
				return; // No color assigned for this block
			}

			BoxLineCache cache = getBoxLineCache(color.get());
			cache.addBlock(support, pos, level);
		});

		for(DyeColor color : DyeColor.values()) {
			var optimizer = getBoxLineCache(color);
			optimizer.buildVBO();
		}

		lastUpdateTick = level.getGameTime();
	}

}

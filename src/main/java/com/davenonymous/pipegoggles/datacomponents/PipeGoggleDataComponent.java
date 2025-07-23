package com.davenonymous.pipegoggles.datacomponents;

import com.davenonymous.pipegoggles.config.Rules;
import com.davenonymous.pipegoggles.data.EnergyCosts;
import com.davenonymous.pipegoggles.data.EnumGoggleMode;
import com.davenonymous.pipegoggles.data.GoggleSupport;
import com.davenonymous.pipegoggles.data.cache.GoggleSupportCache;
import com.davenonymous.pipegoggles.lib.BiggerStreamCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.*;

public record PipeGoggleDataComponent(Map<DyeColor, ItemStack> colors, List<DyeColor> enabledColors, float range, EnumGoggleMode goggleMode, long storedEnergy, int alpha, int lineWidth) {

	public static final Codec<PipeGoggleDataComponent> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			Codec.unboundedMap(DyeColor.CODEC, ItemStack.CODEC).fieldOf("colors").forGetter(PipeGoggleDataComponent::colors),
			DyeColor.CODEC.listOf().optionalFieldOf("enabled_colors", new ArrayList<>()).forGetter(PipeGoggleDataComponent::enabledColors),
			Codec.FLOAT.optionalFieldOf("range", 16.0f).forGetter(PipeGoggleDataComponent::range),
			EnumGoggleMode.CODEC.optionalFieldOf("mode", EnumGoggleMode.SMART).forGetter(PipeGoggleDataComponent::goggleMode),
			Codec.LONG.optionalFieldOf("energy", 0L).forGetter(PipeGoggleDataComponent::storedEnergy),
			Codec.INT.optionalFieldOf("alpha", 255).forGetter(PipeGoggleDataComponent::alpha),
			Codec.INT.optionalFieldOf("line_width", 2).forGetter(PipeGoggleDataComponent::lineWidth)
		).apply(instance, PipeGoggleDataComponent::new)
	);

	public static final StreamCodec<RegistryFriendlyByteBuf, PipeGoggleDataComponent> STREAM_CODEC = BiggerStreamCodec.composite(
		ByteBufCodecs.map(HashMap::new, DyeColor.STREAM_CODEC, ItemStack.STREAM_CODEC, 16), PipeGoggleDataComponent::colors,
		DyeColor.STREAM_CODEC.apply(ByteBufCodecs.list(16)), PipeGoggleDataComponent::enabledColors,
		ByteBufCodecs.FLOAT, PipeGoggleDataComponent::range,
		EnumGoggleMode.STREAM_CODEC, PipeGoggleDataComponent::goggleMode,
		ByteBufCodecs.VAR_LONG, PipeGoggleDataComponent::storedEnergy,
		ByteBufCodecs.INT, PipeGoggleDataComponent::alpha,
		ByteBufCodecs.INT, PipeGoggleDataComponent::lineWidth,
		PipeGoggleDataComponent::new
	);

	public PipeGoggleDataComponent() {
		this(new HashMap<>(), new ArrayList<>(), 8.0f, EnumGoggleMode.SMART, 0L, 255, 2);
	}

	public PipeGoggleDataComponent(Map<DyeColor, ItemStack> colors) {
		this(new HashMap<>(colors), new ArrayList<>(), 8.0f, EnumGoggleMode.SMART, 0L, 255, 2);
	}

	public PipeGoggleDataComponent with(DyeColor color, ItemStack stack) {
		Map<DyeColor, ItemStack> newColors = new HashMap<>(this.colors);
		newColors.put(color, stack);
		var newEnabledColors = new ArrayList<>(this.enabledColors);
		if (!newEnabledColors.contains(color)) {
			newEnabledColors.add(color);
		}
		return new PipeGoggleDataComponent(newColors, newEnabledColors, this.range, this.goggleMode, this.storedEnergy, this.alpha, this.lineWidth);
	}

	public PipeGoggleDataComponent withRange(float range) {
		return new PipeGoggleDataComponent(new HashMap<>(this.colors), this.enabledColors, range, this.goggleMode, this.storedEnergy, this.alpha, this.lineWidth);
	}

	public PipeGoggleDataComponent without(DyeColor color) {
		Map<DyeColor, ItemStack> newColors = new HashMap<>(this.colors);
		newColors.remove(color);
		List<DyeColor> newEnabledColors = new ArrayList<>(this.enabledColors);
		newEnabledColors.remove(color);
		return new PipeGoggleDataComponent(newColors, newEnabledColors, this.range, this.goggleMode, this.storedEnergy, this.alpha, this.lineWidth);
	}

	public PipeGoggleDataComponent disable(DyeColor color) {
		List<DyeColor> newEnabledColors = new ArrayList<>(this.enabledColors);
		newEnabledColors.remove(color);
		return new PipeGoggleDataComponent(new HashMap<>(this.colors), newEnabledColors, this.range, this.goggleMode, this.storedEnergy, this.alpha, this.lineWidth);
	}

	public PipeGoggleDataComponent enable(DyeColor color) {
		List<DyeColor> newEnabledColors = new ArrayList<>(this.enabledColors);
		if (!newEnabledColors.contains(color)) {
			newEnabledColors.add(color);
		}
		return new PipeGoggleDataComponent(new HashMap<>(this.colors), newEnabledColors, this.range, this.goggleMode, this.storedEnergy, this.alpha, this.lineWidth);
	}

	public PipeGoggleDataComponent withColorEnabled(DyeColor color, boolean enabled) {
		return enabled ? enable(color) : disable(color);
	}

	public PipeGoggleDataComponent withMode(EnumGoggleMode enabled) {
		return new PipeGoggleDataComponent(new HashMap<>(this.colors), new ArrayList<>(this.enabledColors), this.range, enabled, this.storedEnergy, this.alpha, this.lineWidth);
	}

	public PipeGoggleDataComponent withStoredEnergy(long energy) {
		return new PipeGoggleDataComponent(new HashMap<>(this.colors), new ArrayList<>(this.enabledColors), this.range, this.goggleMode, energy, this.alpha, this.lineWidth);
	}

	public PipeGoggleDataComponent withAlpha(int alpha) {
		return new PipeGoggleDataComponent(new HashMap<>(this.colors), new ArrayList<>(this.enabledColors), this.range, this.goggleMode, this.storedEnergy, alpha, this.lineWidth);
	}

	public PipeGoggleDataComponent withLineWidth(int lineWidth) {
		return new PipeGoggleDataComponent(new HashMap<>(this.colors), new ArrayList<>(this.enabledColors), this.range, this.goggleMode, this.storedEnergy, this.alpha, lineWidth);
	}


	public boolean isEnabled() {
		return goggleMode != EnumGoggleMode.OFF;
	}

	public boolean isSmartMode() {
		return goggleMode == EnumGoggleMode.SMART;
	}

	public boolean isDisabled() {
		return goggleMode == EnumGoggleMode.OFF;
	}

	public boolean isColorEnabled(DyeColor color) {
		return colors.containsKey(color) && enabledColors.contains(color);
	}

	public boolean enoughEnergy() {
		return !Rules.requireEnergy || storedEnergy >= EnergyCosts.getCost(this);
	}

	public Optional<GoggleSupport> getSupportForColor(DyeColor color) {
		var stack = colors.get(color);
		if (stack == null || stack.isEmpty()) {
			return Optional.empty();
		}
		var optSupport = GoggleSupportCache.getSupportFor(stack);
		if (optSupport.isEmpty()) {
			return Optional.empty(); // No support for this color
		}
		return optSupport;
	}

	public Optional<GoggleSupport> getColorForTrigger(ItemStack stack) {
		if (stack.isEmpty()) {
			return Optional.empty();
		}

		for(DyeColor color : colors.keySet()) {
			var support = getSupportForColor(color);
			if (support.isPresent() && support.get().isTriggerItem(stack)) {
				return support; // Found a matching color and support
			}
		}

		return Optional.empty(); // No matching color found
	}

	public Optional<DyeColor> getColorForStack(ItemStack stack) {
		if (stack.isEmpty()) {
			return Optional.empty();
		}

		for(DyeColor color : colors.keySet()) {
			var support = getSupportForColor(color);
			if (support.isPresent() && support.get().isTriggerItem(stack)) {
				return Optional.of(color); // Found a matching color and support
			}
		}

		return Optional.empty(); // No matching color found
	}

	public String modIdForBlock(Block block) {
		if (block == null || !block.builtInRegistryHolder().isBound()) {
			return "minecraft"; // Default to Minecraft if block is not registered
		}

		return block.builtInRegistryHolder().getKey().location().getNamespace();
	}

	public String modIdForStack(ItemStack stack) {
		if (stack.isEmpty() || !stack.getItem().builtInRegistryHolder().isBound()) {
			return "minecraft"; // Default to Minecraft if item is not registered
		}

		return stack.getItem().builtInRegistryHolder().getKey().location().getNamespace();
	}

	public Optional<DyeColor> getColorForBlock(Block block) {
		String modId = modIdForBlock(block);

		return colors.entrySet().stream()
			.filter(entry -> modIdForStack(entry.getValue()).equals(modId))
			.map(Map.Entry::getKey)
			.findFirst();
	}

	public Optional<String> getModIdForColor(DyeColor color) {
		ItemStack stack = colors.get(color);
		if (stack == null || stack.isEmpty()) {
			return Optional.empty();
		}

		if (!stack.getItem().builtInRegistryHolder().isBound()) {
			return Optional.empty();
		}

		return Optional.of(stack.getItem().builtInRegistryHolder().getKey().location().getNamespace());
	}

	public Optional<ItemStack> getStackForColor(DyeColor color) {
		return Optional.ofNullable(colors.get(color));
	}

	public boolean colorsMatch(Map<DyeColor, ItemStack> otherColors) {
		if (this.colors.size() != otherColors.size()) {
			return false;
		}

		for (Map.Entry<DyeColor, ItemStack> entry : this.colors.entrySet()) {
			ItemStack otherStack = otherColors.get(entry.getKey());
			if(otherStack == null && entry.getValue() == null) {
				return true;
			}
			if(otherStack == null || entry.getValue() == null) {
				return false;
			}
			if (!ItemStack.isSameItemSameComponents(entry.getValue(), otherStack)) {
				return false;
			}
		}

		return true;
	}

	public boolean enabledMatch(List<DyeColor> otherEnabledColors) {
		if (this.enabledColors.size() != otherEnabledColors.size()) {
			return false;
		}

		for (DyeColor color : this.enabledColors) {
			if (!otherEnabledColors.contains(color)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof PipeGoggleDataComponent(Map<DyeColor, ItemStack> otherColors, List<DyeColor> otherEnabledColors, float otherRange, EnumGoggleMode otherMode, long otherEnergy, int otherAlpha, int otherLineWidth))) {
			return false;
		}
		return
			Float.compare(range(), otherRange) == 0 &&
			goggleMode() == otherMode &&
			storedEnergy() == otherEnergy &&
			alpha() == otherAlpha &&
			lineWidth() == otherLineWidth &&
			enabledMatch(otherEnabledColors) &&
			colorsMatch(otherColors);
	}

	private int hashColors() {
		return colors.entrySet().stream()
			.map(entry -> Objects.hash(entry.getKey(), ItemStack.hashItemAndComponents(entry.getValue())))
			.reduce(0, Integer::sum);
	}

	private int hashEnabledColors() {
		return enabledColors.stream()
			.map(DyeColor::ordinal)
			.map(i -> i * 31 + i)
			.reduce(0, Integer::sum);
	}

	@Override
	public int hashCode() {
		var hash = Objects.hash(hashColors(), hashEnabledColors(), range(), goggleMode(), storedEnergy(), alpha(), lineWidth());
		return hash;
	}


}

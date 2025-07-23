package com.davenonymous.pipegoggles.items;

import com.davenonymous.pipegoggles.config.Client;
import com.davenonymous.pipegoggles.config.Rules;
import com.davenonymous.pipegoggles.data.EnergyCosts;
import com.davenonymous.pipegoggles.data.EnumGoggleMode;
import com.davenonymous.pipegoggles.datacomponents.PipeGoggleDataComponent;
import com.davenonymous.pipegoggles.lib.gui.tooltip.HBoxTooltipComponent;
import com.davenonymous.pipegoggles.lib.gui.tooltip.StringTooltipComponent;
import com.davenonymous.pipegoggles.lib.gui.tooltip.VBoxTooltipComponent;
import com.davenonymous.pipegoggles.lib.gui.tooltip.WrappedStringTooltipComponent;
import com.davenonymous.pipegoggles.setup.CuriosCompat;
import com.davenonymous.pipegoggles.setup.ModDataComponents;
import com.davenonymous.pipegoggles.setup.ModItems;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.Optional;
import java.util.function.Predicate;

public class PipeGoggleItem extends Item {
	public PipeGoggleItem(Properties properties) {
		super(properties);
	}

	@Override
	public int getMaxStackSize(ItemStack stack) {
		return 1;
	}

	public static PipeGoggleDataComponent data(ItemStack stack) {
		return stack.getOrDefault(ModDataComponents.PIPEGOGGLES_COMPONENT, new PipeGoggleDataComponent());
	}

	public static long getStoredEnergy(ItemStack stack) {
		var goggleData = data(stack);
		return goggleData.storedEnergy();
	}

	public static ItemStack withEnergy(ItemStack stack, long energy) {
		var goggleData = data(stack);
		var newData = goggleData.withStoredEnergy(energy);
		var newStack = stack.copy();
		newStack.set(ModDataComponents.PIPEGOGGLES_COMPONENT, newData);
		return newStack;
	}

	public static Optional<ItemStack> getGoggleStack(Player player) {
		var playerInventory = player.getInventory();
		var optGoggleStack = playerInventory.items.stream().filter(Predicate.not(ItemStack::isEmpty)).filter(stack -> stack.is(ModItems.PIPE_GOGGLE_ITEM.get())).findFirst();
		if(optGoggleStack.isEmpty()) {
			IItemHandler curiosInventory = player.getCapability(CuriosCompat.CURIOS_INVENTORY);
			if (curiosInventory != null && curiosInventory.getSlots() > 0) {
				for(int slot = 0; slot < curiosInventory.getSlots(); slot++) {
					ItemStack stack = curiosInventory.getStackInSlot(slot);
					if (stack.is(ModItems.PIPE_GOGGLE_ITEM.get())) {
						optGoggleStack = Optional.of(stack);
						break;
					}
				}
			}
		}

		return optGoggleStack;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if(level.isClientSide() || hand == InteractionHand.OFF_HAND) {
			// Client-side only, no need to check server-side conditions
			return InteractionResultHolder.pass(player.getItemInHand(hand));
		}

		if (player.isShiftKeyDown()) {
			var googleStack = player.getItemInHand(hand);
			var goggleData = data(googleStack);
			var newGoggleData = goggleData.withMode(goggleData.goggleMode().next());
			googleStack.set(ModDataComponents.PIPEGOGGLES_COMPONENT, newGoggleData);

			String attribute = I18n.get("pipegoggles.enumgogglemode." + newGoggleData.goggleMode().getSerializedName());
			player.displayClientMessage(Component.translatable("pipegoggles.message.changed_mode", attribute), true);

			return InteractionResultHolder.success(player.getItemInHand(hand));
		}

		var provider = new SimpleMenuProvider((id, inventory, player1) -> new PipeGoggleContainer(id, player1.getInventory(), player1), Component.empty());
		player.openMenu(provider);
		return InteractionResultHolder.success(player.getItemInHand(hand));
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return false;
	}

	@Override
	public boolean isBarVisible(ItemStack googleStack) {
		return Rules.requireEnergy && getStoredEnergy(googleStack) > 0 && getStoredEnergy(googleStack) < Rules.maxEnergy;
	}

	@Override
	public int getBarColor(ItemStack googleStack) {
		float storedEnergy = getStoredEnergy(googleStack);
		float stackMaxDamage = Rules.maxEnergy;
		float f = 1 - Math.max(0.0F, (stackMaxDamage - storedEnergy) / stackMaxDamage);
		return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
	}

	@Override
	public int getBarWidth(ItemStack googleStack) {
		return Math.round((float)getStoredEnergy(googleStack) * 13.0F / (float)Rules.maxEnergy);
	}

	@Override
	public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
		var result = new VBoxTooltipComponent();
		if(Rules.requireEnergy) {
			var box = new HBoxTooltipComponent(
				StringTooltipComponent.green(I18n.get("pipegoggles.message.stored_energy") + ": "),
				StringTooltipComponent.gray(String.valueOf(getStoredEnergy(stack)))
			);
			result.add(box);
		}

		if(Client.showHelpTooltips && Minecraft.getInstance().options.graphicsMode().get() == GraphicsStatus.FABULOUS) {
			result.add(WrappedStringTooltipComponent.red(I18n.get("pipegoggles.message.fabulous_warning")));
		}

		if(result.isEmpty()) {
			return Optional.empty(); // No tooltip content to display
		}

		return Optional.of(result);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		super.inventoryTick(stack, level, entity, slotId, isSelected);
		if(!(entity instanceof ServerPlayer player)) {
			return; // Only process on server-side for players
		}

		if(!Rules.requireEnergy) {
			return; // No energy required, nothing to do
		}

		PipeGoggleDataComponent data = data(stack);
		if(data.goggleMode() == EnumGoggleMode.OFF) {
			return; // Goggles are disabled, nothing to do
		}

		if(!data.enoughEnergy()) {
			return; // No cost, nothing to do
		}

		var cost = EnergyCosts.getCost(data);
		var available = data.storedEnergy();
		if(slotId != -1) {
			var newStack = withEnergy(stack, available - cost);
			player.getInventory().setItem(slotId, newStack);
		} else {
			IItemHandler curiosInventory = player.getCapability(CuriosCompat.CURIOS_INVENTORY);
			if (curiosInventory != null && curiosInventory.getSlots() > 0) {
				for(int slot = 0; slot < curiosInventory.getSlots(); slot++) {
					ItemStack curioStack = curiosInventory.getStackInSlot(slot);
					if (curioStack.is(ModItems.PIPE_GOGGLE_ITEM.get())) {
						curioStack.set(ModDataComponents.PIPEGOGGLES_COMPONENT, data.withStoredEnergy(available - cost));
						break;
					}
				}
			}
		}
	}

}

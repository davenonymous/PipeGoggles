package com.davenonymous.pipegoggles.item;


import com.davenonymous.libnonymous.base.BaseLanguageProvider;
import com.davenonymous.libnonymous.render.IRenderLevelOnHeld;
import com.davenonymous.pipegoggles.gui.PipeGogglesContainer;
import com.davenonymous.pipegoggles.render.BoxOptimizer;
import com.davenonymous.pipegoggles.render.BoxOptimizerCache;
import com.davenonymous.pipegoggles.render.BoxRenderer;
import com.davenonymous.pipegoggles.setup.Registration;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class PipeGogglesItem extends Item implements IRenderLevelOnHeld {
    private static final BoxOptimizerCache cache = new BoxOptimizerCache();

    public PipeGogglesItem() {
        super(new Properties().stacksTo(1).tab(CreativeModeTab.TAB_MISC));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @org.jetbrains.annotations.Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

        pTooltipComponents.add(new TranslatableComponent("pipegoggles.tooltip_1").withStyle(ChatFormatting.YELLOW));
        pTooltipComponents.add(new TranslatableComponent("pipegoggles.tooltip_2").withStyle(ChatFormatting.YELLOW));

        if(pIsAdvanced.isAdvanced()) {
            for(var slot : new PipeGogglesItemData(pStack).slots) {
                if(slot == null) {
                    continue;
                }

                pTooltipComponents.add(new TextComponent("- " + slot));
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(pLevel.isClientSide()) {
            return super.use(pLevel, pPlayer, pUsedHand);
        }

        if(!(pPlayer instanceof ServerPlayer player)) {
            return super.use(pLevel, pPlayer, pUsedHand);
        }

        MenuProvider menuProvider = new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return new TranslatableComponent(BaseLanguageProvider.getContainerLanguageKey(Registration.PIPEGOGGLES_CONTAINER.get()));
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
                return new PipeGogglesContainer(pContainerId, pPlayer.getOnPos(), pInventory, pPlayer);
            }
        };

        NetworkHooks.openGui(player, menuProvider, pPlayer.getOnPos());

        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void renderEffectOnHeldItem(PoseStack poseStack, Player player, InteractionHand mainHand, float partialTick, Matrix4f projectionMatrix) {
        PipeGogglesItemData data = new PipeGogglesItemData(player.getItemInHand(mainHand));
        if(!data.enabled) {
            return;
        }

        for(BoxOptimizer optimizer : cache.optimizers) {
            optimizer.eventuallyUpdate(player, mainHand, data);
            BoxRenderer.renderLines(poseStack, player, partialTick, optimizer.color, optimizer.lines, projectionMatrix);
            //optimizer.render(poseStack, player, partialTick, projectionMatrix);
        }
    }
}
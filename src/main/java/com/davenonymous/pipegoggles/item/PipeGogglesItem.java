package com.davenonymous.pipegoggles.item;

import com.davenonymous.libnonymous.base.BaseItem;
import com.davenonymous.libnonymous.base.BaseNamedContainerProvider;
import com.davenonymous.pipegoggles.gui.PipeGogglesContainer;
import com.davenonymous.pipegoggles.render.BoxOptimizer;
import com.davenonymous.pipegoggles.render.BoxOptimizerCache;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class PipeGogglesItem extends BaseItem{
    private static final BoxOptimizerCache cache = new BoxOptimizerCache();

    public PipeGogglesItem() {
        super(new Properties().maxStackSize(1).group(ItemGroup.TOOLS));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        tooltip.add(new TranslationTextComponent("pipegoggles.tooltip_1").applyTextStyle(TextFormatting.YELLOW));
        tooltip.add(new TranslationTextComponent("pipegoggles.tooltip_2").applyTextStyle(TextFormatting.YELLOW));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if(worldIn.isRemote) {
            return super.onItemRightClick(worldIn, playerIn, handIn);
        }
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) playerIn;
        NetworkHooks.openGui(serverPlayer, new BaseNamedContainerProvider(
                new StringTextComponent("Pipe Goggles"),
                (id, inv, player) -> new PipeGogglesContainer(id, inv, player)
        ));

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void renderEffectOnHeldItem(PlayerEntity player, Hand mainHand, float partialTicks) {
        PipeGogglesItemData data = new PipeGogglesItemData(player.getHeldItem(mainHand));
        if(!data.enabled) {
            return;
        }

        for(BoxOptimizer optimizer : cache.optimizers) {
            optimizer.eventuallyUpdate(player, mainHand, data);
            optimizer.render(player, partialTicks);
        }
    }
}

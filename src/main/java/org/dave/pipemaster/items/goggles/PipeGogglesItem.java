package org.dave.pipemaster.items.goggles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.dave.pipemaster.PipeMaster;
import org.dave.pipemaster.base.ItemBase;
import org.dave.pipemaster.init.Itemss;
import org.dave.pipemaster.proxy.GuiProxy;

public class PipeGogglesItem extends ItemBase {
    private BoxOptimizer[] optimizers = {
            new BoxOptimizer(0),
            new BoxOptimizer(1),
            new BoxOptimizer(2),
            new BoxOptimizer(3),
    };

    public static void clearBoxOptimizerCache(int id) {
        Itemss.pipeGoggles.optimizers[id].lastUpdateTick = Long.MIN_VALUE;
    }

    public PipeGogglesItem() {
        super("pipe_goggles");

        this.setCreativeTab(PipeMaster.CREATIVE_TAB);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote) {
            if(player.isSneaking()) {
                PipeGogglesData originalData = new PipeGogglesData(stack);
                originalData.toggleEnabled();
                player.inventory.setInventorySlotContents(player.inventory.currentItem, originalData.createItemStack());
                player.inventory.markDirty();
            } else {
                player.openGui(PipeMaster.instance, GuiProxy.GuiIDS.PIPE_GOGGLES.ordinal(), world, hand.ordinal(), 0, 0);
            }

            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }

        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    public void renderEffectOnHeldItem(EntityPlayer player, EnumHand hand, float partialTicks) {
        PipeGogglesData data = new PipeGogglesData(player.getHeldItem(hand));
        if(!data.isEnabled()) {
            return;
        }

        for(int channelId = 0; channelId < optimizers.length; channelId++) {
            optimizers[channelId].eventuallyUpdate(player, hand, data);
            optimizers[channelId].render(player, partialTicks);
        }
    }
}

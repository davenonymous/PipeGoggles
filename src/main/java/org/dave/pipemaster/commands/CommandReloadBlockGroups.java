package org.dave.pipemaster.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import org.dave.pipemaster.PipeMaster;
import org.dave.pipemaster.base.CommandBaseExt;

public class CommandReloadBlockGroups extends CommandBaseExt {
    @Override
    public String getName() {
        return "reload-blockgroups";
    }

    @Override
    public boolean isAllowed(EntityPlayer player, boolean creative, boolean isOp) {
        return isOp;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        PipeMaster.blockGroupRegistry.reloadGroups();
        sender.getCommandSenderEntity().sendMessage(new TextComponentTranslation("commands.pipemaster.reload-blockgroups.blockgroups_reloaded"));
    }
}

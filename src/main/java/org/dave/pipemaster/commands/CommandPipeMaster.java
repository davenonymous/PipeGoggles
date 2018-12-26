package org.dave.pipemaster.commands;

import org.dave.pipemaster.PipeMaster;
import org.dave.pipemaster.base.CommandBaseMenu;

public class CommandPipeMaster extends CommandBaseMenu {
    @Override
    public void initEntries() {
        this.addSubcommand(new CommandReloadBlockGroups());
    }

    @Override
    public String getName() {
        return PipeMaster.MODID;
    }
}

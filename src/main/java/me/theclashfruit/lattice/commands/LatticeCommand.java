package me.theclashfruit.lattice.commands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;

public class LatticeCommand extends AbstractCommandCollection {
    public LatticeCommand() {
        super("lattice", "All Lattice commands.");

        this.addSubCommand(new LatticeLinkCommand());
        this.addSubCommand(new LatticeUnLinkCommand());
    }
}

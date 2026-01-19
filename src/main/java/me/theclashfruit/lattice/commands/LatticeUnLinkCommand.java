package me.theclashfruit.lattice.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.theclashfruit.lattice.LatticePlugin;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Map;

public class LatticeUnLinkCommand extends AbstractPlayerCommand {
    public LatticeUnLinkCommand() {
        super("unlink", "Unlink discord account.");
    }

    @Override
    protected void execute(@NotNull CommandContext commandContext, @NotNull Store<EntityStore> store, @NotNull Ref<EntityStore> ref, @NotNull PlayerRef playerRef, @NotNull World world) {
        try {
            Map<String, String> connections = LatticePlugin.connections.get().connections;

            if (connections.containsKey(playerRef.getUuid().toString())) {
                connections.remove(playerRef.getUuid().toString());
                LatticePlugin.connections.save();

                playerRef.sendMessage(Message.raw("Successfully unlinked the Discord account.").color(Color.GREEN).bold(true));
            } else {
                playerRef.sendMessage(Message.raw("You don't have a Discord account linked.").bold(true).color(Color.RED));
            }
        } catch (Exception e) {
            LOGGER.atWarning().withCause(e).log("Err:Lattice::UnlinkError");

            playerRef.sendMessage(Message.raw("An unknown error had happened.").bold(true).color(Color.RED));
        }
    }
}
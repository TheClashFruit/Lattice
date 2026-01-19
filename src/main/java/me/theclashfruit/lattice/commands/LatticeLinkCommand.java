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
import me.theclashfruit.lattice.util.LinkUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Map;

public class LatticeLinkCommand extends AbstractPlayerCommand {
    public LatticeLinkCommand() {
        super("link", "Link discord account.");
    }

    @Override
    protected void execute(@NotNull CommandContext commandContext, @NotNull Store<EntityStore> store, @NotNull Ref<EntityStore> ref, @NotNull PlayerRef playerRef, @NotNull World world) {
        try {
            Map<String, String> connections = LatticePlugin.connections.get().connections;

            if (!connections.containsKey(playerRef.getUuid().toString())) {
               String code = LinkUtil.genCode();
               LinkUtil.addCode(code, playerRef);

               playerRef.sendMessage(
                       Message.join(
                               Message.raw("Run the following command on Discord: ").bold(true),
                               Message.raw("/link code:" + code).monospace(true)
                       )
               );
           } else {
               playerRef.sendMessage(Message.raw("You're already have a Discord account linked, if you want to change accounts unlink it first.").bold(true).color(Color.RED));
           }
        } catch (Exception e) {
            LOGGER.atWarning().withCause(e).log("Err:Lattice::LinkError");

            playerRef.sendMessage(Message.raw("You're already in the process of linking your account, try again later.").bold(true).color(Color.RED));
        }
    }
}
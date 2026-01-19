package me.theclashfruit.lattice.discord;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import me.theclashfruit.lattice.LatticePlugin;
import me.theclashfruit.lattice.util.LinkUtil;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import java.awt.*;
import java.util.Map;

import static me.theclashfruit.lattice.LatticePlugin.LOGGER;

public class BotEventListener extends ListenerAdapter {
    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        LOGGER.atInfo().log("Logged in as %s#%s", event.getJDA().getSelfUser().getName(), event.getJDA().getSelfUser().getDiscriminator());
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        var user    = event.getAuthor();
        var channel = event.getChannel();
        var message = event.getMessage();

        var config = LatticePlugin.config.get();

        // TODO: Implement slash commands.
        if(handleCommands(user, message)) return;
        if (!channel.getId().equals(config.discord.channel_id)) return;

        var attachments = message.getAttachments();

        StringBuilder builder = new StringBuilder();

        if (!message.getContentDisplay().isEmpty()) {
            builder.append(" ");
            builder.append(message.getContentDisplay());
        }

        var attachmentMessages = attachments.stream().map(a -> Message.join(Message.raw("[" + a.getFileName() + "]").color(Color.CYAN).link(a.getUrl()), Message.raw(" "))).toList();
        var compiledAttachments = Message.join(
                Message.raw(" "),
                Message.join(attachmentMessages.toArray(new Message[0]))
        );

        var prefix = Message
                .raw(config.chat_prefix)
                .color(config.chat_prefix_colour)
                .bold(true);

        var joined = com.hypixel.hytale.server.core.Message.join(
                prefix,
                com.hypixel.hytale.server.core.Message.raw(" "),
                com.hypixel.hytale.server.core.Message.raw(user.getEffectiveName()),
                com.hypixel.hytale.server.core.Message.raw(":"),
                com.hypixel.hytale.server.core.Message.raw(builder.toString()),
                compiledAttachments
        );

        Universe.get().sendMessage(joined);
        LOGGER.atInfo().log("[Discord] %s:%s %s", user.getEffectiveName(), builder.toString(), String.join(" ", attachments.stream().map(a -> "[" + a.getFileName() + "]").toList()));
    }

    private boolean handleCommands(User user, net.dv8tion.jda.api.entities.Message message) {
        try {
            String contents = message.getContentDisplay();
            String[] tokens = contents.split(" ");

            if (tokens[0].equals("-link")) {
                String code = tokens[1];

                PlayerRef ref = LinkUtil.getPlayerFromCode(code);
                if (ref == null)
                    message.reply("Invalid code.").queue();
                else {
                    Map<String, String> connections = LatticePlugin.connections.get().connections;
                    connections.put(ref.getUuid().toString(), user.getId());
                    LatticePlugin.connections.save();

                    LinkUtil.removeCode(code);

                    ref.sendMessage(Message.raw("Successfully linked Discord account.").color(Color.GREEN).bold(true));
                }

                return true;
            }
        } catch (Exception e) {
            message.reply("There was an error executing this command!").queue();
            LOGGER.atWarning().withCause(e).log("Discord command failed!");

            return true;
        }

        return false;
    }
}

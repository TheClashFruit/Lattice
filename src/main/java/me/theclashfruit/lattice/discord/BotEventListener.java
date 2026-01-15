package me.theclashfruit.lattice.discord;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.Universe;
import me.theclashfruit.lattice.LatticePlugin;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import java.awt.*;

import static me.theclashfruit.lattice.LatticePlugin.LOGGER;

public class BotEventListener extends ListenerAdapter {
    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        LOGGER.atInfo().log("Logged in as %s#%s", event.getJDA().getSelfUser().getName(), event.getJDA().getSelfUser().getDiscriminator());
        event.getJDA().getPresence().setPresence(Activity.playing("Hytale"), false);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        var user    = event.getAuthor();
        var channel = event.getChannel();
        var message = event.getMessage();

        if (message.getContentRaw().equals("!ping"))
            channel.sendMessage("Pong!").queue();

        var config = LatticePlugin.config.get();

        LOGGER.atInfo().log("Message Received! %s vs %s", channel.getId(), config.discord.channel_id);

        if (!channel.getId().equals(config.discord.channel_id)) return;

        LOGGER.atInfo().log("Whoah, it works!");

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
        LOGGER.atInfo().log(joined.getAnsiMessage());
    }
}

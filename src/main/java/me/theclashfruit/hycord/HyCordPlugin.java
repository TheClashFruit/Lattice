package me.theclashfruit.hycord;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.util.Config;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.MessageEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.Webhook;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.TextChannel;
import me.theclashfruit.hycord.events.ChatEvent;
import me.theclashfruit.hycord.util.HyCordConfig;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Objects;
import java.util.logging.Level;

public class HyCordPlugin extends JavaPlugin {
    public static DiscordClient client;
    public static GatewayDiscordClient gateway;

    public static Config<HyCordConfig> config;

    public static HytaleLogger LOGGER;

    public HyCordPlugin(@Nonnull JavaPluginInit init) {
        super(init);

        LOGGER = this.getLogger();
        config = this.withConfig("HyCord", HyCordConfig.CODEC);

    }

    @Override
    protected void setup() {
        super.setup();
        config.save();

        this.getEventRegistry().registerGlobal(PlayerChatEvent.class, ChatEvent::onPlayerChat);

        client = DiscordClient.create(config.get().token);
        client.withGateway(gateway -> {
            HyCordPlugin.gateway = gateway;

            gateway.getEventDispatcher()
                    .on(ReadyEvent.class)
                    .subscribe(e ->
                            LOGGER.atInfo().log(
                                    "Logged in as %s#%s",
                                    e.getSelf().getUsername(),
                                    e.getSelf().getDiscriminator()
                            )
                    );

            gateway.getEventDispatcher()
                    .on(MessageCreateEvent.class)
                    .flatMap(event -> {
                        Message msg = event.getMessage();

                        if (msg.getAuthor().map(User::isBot).orElse(true)) {
                            return Mono.empty();
                        }

                        return msg.getChannel()
                                .filter(ch -> ch.getId().equals(Snowflake.of(config.get().channel_id)))
                                .flatMap(ch ->
                                        msg.getAuthorAsMember().doOnNext(member -> {
                                            LOGGER.atInfo().log(
                                                    "%s %s: %s",
                                                    config.get().chat_prefix,
                                                    member.getDisplayName(),
                                                    msg.getContent()
                                            );

                                            var prefix =
                                                    com.hypixel.hytale.server.core.Message
                                                            .raw(config.get().chat_prefix)
                                                            .bold(true)
                                                            .color(config.get().chat_prefix_colour);

                                            var joined = com.hypixel.hytale.server.core.Message.join(
                                                    prefix,
                                                    com.hypixel.hytale.server.core.Message.raw(" "),
                                                    com.hypixel.hytale.server.core.Message.raw(member.getDisplayName()),
                                                    com.hypixel.hytale.server.core.Message.raw(": "),
                                                    com.hypixel.hytale.server.core.Message.raw(msg.getContent())
                                            );

                                            Universe.get().sendMessage(joined);
                                        })
                                );
                    })
                    .subscribe();

            return Mono.never();
        }).subscribe();
    }

    @Override
    protected void shutdown() {
        super.shutdown();

        client.withGateway(GatewayDiscordClient::logout).subscribe();
    }
}

package me.theclashfruit.lattice.events;

import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import discord4j.common.util.Snowflake;
import me.theclashfruit.lattice.LatticePlugin;
import reactor.core.publisher.Mono;

public class ChatEvent {
    public static void onPlayerChat(PlayerChatEvent event) {
        PlayerRef sender = event.getSender();
        String content = event.getContent();

        String webhook = LatticePlugin.config.get().webhook_id;

        LatticePlugin.client
                .withGateway((gateway) -> {
                    return gateway.getWebhookById(Snowflake.of(webhook))
                            .flatMap(hook -> {
                                try {
                                    return hook.execute().withUsername(sender.getUsername()).withContent(content);
                                } catch (Exception e) {
                                    LatticePlugin.LOGGER.atSevere().log("Error while executing webhook", e);
                                    return Mono.error(e);
                                }
                            }).then();
                }).subscribe();
    }
}

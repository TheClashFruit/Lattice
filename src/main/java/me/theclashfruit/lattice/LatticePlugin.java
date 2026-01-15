package me.theclashfruit.lattice;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import me.theclashfruit.lattice.discord.BotEventListener;
import me.theclashfruit.lattice.events.PlayerEvents;
import me.theclashfruit.lattice.util.LatticeConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.annotation.Nonnull;

public class LatticePlugin extends JavaPlugin {
    public static JDA jda;
    public static Config<LatticeConfig> config;

    public static HytaleLogger LOGGER;

    public LatticePlugin(@Nonnull JavaPluginInit init) {
        super(init);

        LOGGER = this.getLogger();
        config = this.withConfig("Lattice", LatticeConfig.CODEC);
    }

    @Override
    protected void setup() {
        super.setup();
        config.save();

        jda = JDABuilder
                .createDefault(config.get().discord.token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .setEventPassthrough(true)
                .addEventListeners(new BotEventListener())
                .build();

        this.getEventRegistry().registerGlobal(PlayerChatEvent.class, PlayerEvents::onPlayerChat);
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, PlayerEvents::onPlayerReady);
        this.getEventRegistry().registerGlobal(PlayerDisconnectEvent.class, PlayerEvents::onPlayerDisconnect);
    }

    @Override
    protected void shutdown() {
        super.shutdown();

        jda.shutdown();
    }
}

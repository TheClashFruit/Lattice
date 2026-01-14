package me.theclashfruit.lattice.util;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class LatticeConfig {
    public static final BuilderCodec<LatticeConfig> CODEC = BuilderCodec.builder(LatticeConfig.class, LatticeConfig::new)
            .append(
                    new KeyedCodec<>("Token", BuilderCodec.STRING),
                    (config, value, info) -> config.token = value,
                    (config, info) -> config.token
            )
            .add()
            .append(
                    new KeyedCodec<>("ChannelId", BuilderCodec.STRING),
                    (config, value, info) -> config.channel_id = value,
                    (config, info) -> config.channel_id
            )
            .add()
            .append(
                    new KeyedCodec<>("WebhookId", BuilderCodec. STRING),
                    (config, value, info) -> config.webhook_id = value,
                    (config, info) -> config.webhook_id
            )
            .add()
            .append(
                    new KeyedCodec<>("ChatPrefix", BuilderCodec.STRING),
                    (config, value, info) -> config.chat_prefix = value,
                    (config, info) -> config.chat_prefix
            )
            .add()
            .append(
                    new KeyedCodec<>("ChatPrefixColour", BuilderCodec. STRING),
                    (config, value, info) -> config.chat_prefix_colour = value,
                    (config, info) -> config.chat_prefix_colour
            )
            .add()
            .build();

    public LatticeConfig() {}

    public String token = "your_token_here";
    public String channel_id = "";
    public String webhook_id = "";
    public String chat_prefix = "[Discord]";
    public String chat_prefix_colour = "#5865F2";
}

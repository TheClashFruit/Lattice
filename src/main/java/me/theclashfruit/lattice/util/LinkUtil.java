package me.theclashfruit.lattice.util;

import com.hypixel.hytale.server.core.universe.PlayerRef;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LinkUtil {
    private static final Map<String, PlayerRef> codes = new HashMap<>();

    public static void addCode(String code, PlayerRef playerRef) throws Exception {
        if (!codes.containsValue(playerRef))
            codes.put(code, playerRef);
        else
            throw new Exception("Player is already linking their account.");
    }

    public static void removeCode(String code) {
        codes.remove(code);
    }

    public static PlayerRef getPlayerFromCode(String code) {
        return codes.getOrDefault(code, null);
    }

    public static String getCodeFromPlayer(PlayerRef playerRef) {
        for (Map.Entry<String, PlayerRef> entry : codes.entrySet()) {
            if (Objects.equals(playerRef, entry.getValue())) {
                return entry.getKey();
            }
        }

        return null;
    }

    public static String genCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 6; i++)
            builder.append(chars.charAt((int) Math.floor(chars.length() * Math.random())));

        // scary cpu heavy recursion
        return codes.containsKey(builder.toString()) ? genCode() : builder.toString();
    }
}

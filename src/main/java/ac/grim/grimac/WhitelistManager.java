package ac.grim.grimac.whitelist;

import java.util.Set;
import java.util.UUID;

public class WhitelistManager {

    private static final Set<UUID> whitelisted = Set.of(
        UUID.fromString("123e4567-e89b-12d3-a456-426614174000") // replace with real Geyser UUIDs
    );

    public static boolean isWhitelisted(UUID uuid) {
        return whitelisted.contains(uuid);
    }
}


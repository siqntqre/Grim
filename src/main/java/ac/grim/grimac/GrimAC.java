package ac.grim.grimac;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

public final class GrimAC extends JavaPlugin {

    @Override
    public void onLoad() {
        GrimAPI.INSTANCE.load(this);
    }

    @Override
    public void onDisable() {
        GrimAPI.INSTANCE.stop(this);
    }

    @Override
    public void onEnable() {
        GrimAPI.INSTANCE.start(this);

        // Register Geyser plugin message channel listener
        getServer().getMessenger().registerIncomingPluginChannel(this, "geyser:handshake", (channel, player, message) -> {
            if (channel.equals("geyser:handshake")) {
                WhitelistManager.add(player.getUniqueId());
                getLogger().info("Whitelisted Geyser player: " + player.getName());
            }
        });
    }
}

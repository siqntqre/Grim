package ac.grim.grimac.checks.impl.movement;

import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;

// ✅ Import your whitelist manager
import ac.grim.grimac.whitelist.WhitelistManager;

@CheckData(name = "NoSlow", description = "Was not slowed while using an item", setback = 5)
public class NoSlow extends Check implements PostPredictionCheck {
    double offsetToFlag;
    double bestOffset = 1;

    public boolean didSlotChangeLastTick = false;
    public boolean flaggedLastTick = false;

    public NoSlow(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPredictionComplete(final PredictionComplete predictionComplete) {
        // ✅ Skip check if player is whitelisted (Eagler/Geyser)
        if (WhitelistManager.isWhitelisted(player.getUUID())) return;

        if (!predictionComplete.isChecked()) return;

        if (player.packetStateData.isSlowedByUsingItem()) {
            if (player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_8) && didSlotChangeLastTick) {
                didSlotChangeLastTick = false;
                flaggedLastTick = false;
            }

            if (bestOffset > offsetToFlag) {
                if (flaggedLastTick) {
                    flagAndAlertWithSetback();
                }
                flaggedLastTick = true;
            } else {
                reward();
                flaggedLastTick = false;
            }
        }
        bestOffset = 1;
    }

    public void handlePredictionAnalysis(double offset) {
        bestOffset = Math.min(bestOffset, offset);
    }

    @Override
    public void onReload(ConfigManager config) {
        offsetToFlag = config.getDoubleElse(getConfigName() + ".threshold", 0.001);
    }
}

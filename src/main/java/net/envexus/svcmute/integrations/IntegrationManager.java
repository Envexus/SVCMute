package net.envexus.svcmute.integrations;

import net.envexus.svcmute.integrations.advancedbans.AdvancedBansMuteChecker;
import net.envexus.svcmute.integrations.essentials.EssentialsMuteChecker;
import net.envexus.svcmute.integrations.litebans.LiteBansMuteChecker;
import net.envexus.svcmute.integrations.svcmute.SQLiteMuteChecker;
import net.envexus.svcmute.util.SQLiteHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IntegrationManager {
    private final List<MuteChecker> muteCheckers = new ArrayList<>();
    private final List<MutedPlayer> mutedPlayers = new ArrayList<>(); // List to track muted players
    private final SQLiteHelper sqliteHelper;

    public IntegrationManager(SQLiteHelper sqliteHelper) {
        this.sqliteHelper = sqliteHelper;
        registerPlugins();
    }

    /**
     * Register all supported mute-management plugins.
     */
    private void registerPlugins() {
        Plugin liteBansPlugin = Bukkit.getPluginManager().getPlugin("LiteBans");
        boolean isLiteBansEnabled = liteBansPlugin != null && liteBansPlugin.isEnabled();

        Plugin advancedBansPlugin = Bukkit.getPluginManager().getPlugin("AdvancedBan");
        boolean isAdvancedBanEnabled = advancedBansPlugin != null && advancedBansPlugin.isEnabled();

        if (!isLiteBansEnabled && !isAdvancedBanEnabled) {
            Plugin essentialsPlugin = Bukkit.getPluginManager().getPlugin("Essentials");
            if (essentialsPlugin != null && essentialsPlugin.isEnabled()) {
                muteCheckers.add(new EssentialsMuteChecker(essentialsPlugin));
            }
        }

        if (isLiteBansEnabled) {
            muteCheckers.add(new LiteBansMuteChecker());
        }

        if (isAdvancedBanEnabled) {
            muteCheckers.add(new AdvancedBansMuteChecker(advancedBansPlugin));
        }

        muteCheckers.add(new SQLiteMuteChecker(sqliteHelper));
    }

    /**
     * Check if the player is muted.
     *
     * @param player the player to check
     * @return true if the player is muted, false otherwise
     */
    public boolean isPlayerMuted(Player player) {
        UUID playerUUID = player.getUniqueId();
        // Check the list of manually muted players
        for (MutedPlayer mutedPlayer : mutedPlayers) {
            if (mutedPlayer.getPlayerUUID().equals(playerUUID) && mutedPlayer.getUnmuteTime() > System.currentTimeMillis()) {
                return true;
            }
        }
        // Also check through all registered mute checkers
        for (MuteChecker checker : muteCheckers) {
            if (checker.isPlayerMuted(player)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add a player to the list of muted players.
     *
     * @param playerUUID the UUID of the player
     * @param unmuteTime the time when the player should be unmuted
     */
    public void addMutedPlayer(UUID playerUUID, long unmuteTime) {
        mutedPlayers.add(new MutedPlayer(playerUUID, unmuteTime));
    }

    /**
     * Remove a player from the list of muted players.
     *
     * @param playerUUID the UUID of the player
     */
    public void removeMutedPlayer(UUID playerUUID) {
        mutedPlayers.removeIf(mutedPlayer -> mutedPlayer.getPlayerUUID().equals(playerUUID));
    }

    // Inner class to represent a muted player
    private static class MutedPlayer {
        private final UUID playerUUID;
        private final long unmuteTime;

        public MutedPlayer(UUID playerUUID, long unmuteTime) {
            this.playerUUID = playerUUID;
            this.unmuteTime = unmuteTime;
        }

        public UUID getPlayerUUID() {
            return playerUUID;
        }

        public long getUnmuteTime() {
            return unmuteTime;
        }
    }
}

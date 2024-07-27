package net.envexus.svcmute.integrations;

import net.envexus.svcmute.integrations.essentials.EssentialsMuteChecker;
import net.envexus.svcmute.integrations.litebans.LiteBansMuteChecker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class IntegrationManager {
    private final List<MuteChecker> muteCheckers = new ArrayList<>();

    public IntegrationManager() {
        registerPlugins();
    }

    /**
     * Register all supported mute-management plugins.
     */
    private void registerPlugins() {
        Plugin essentialsPlugin = Bukkit.getPluginManager().getPlugin("Essentials");
        if (essentialsPlugin != null && essentialsPlugin.isEnabled()) {
            muteCheckers.add(new EssentialsMuteChecker(essentialsPlugin));
        }

        Plugin liteBansPlugin = Bukkit.getPluginManager().getPlugin("LiteBans");
        if (liteBansPlugin != null && liteBansPlugin.isEnabled()) {
            muteCheckers.add(new LiteBansMuteChecker());
        }
    }

    /**
     * Check if the player is muted.
     *
     * @param player the player to check
     * @return true if the player is muted, false otherwise
     */
    public boolean isPlayerMuted(Player player) {
        for (MuteChecker checker : muteCheckers) {
            if (checker.isPlayerMuted(player)) {
                return true;
            }
        }
        return false;
    }
}


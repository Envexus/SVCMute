package net.envexus.svcmute.integrations.litebans;

import litebans.api.Database;
import net.envexus.svcmute.integrations.MuteChecker;
import org.bukkit.entity.Player;

public class LiteBansMuteChecker implements MuteChecker {
    @Override
    public boolean isPlayerMuted(Player player) {
        return Database.get().isPlayerMuted(player.getUniqueId(), null);
    }
}

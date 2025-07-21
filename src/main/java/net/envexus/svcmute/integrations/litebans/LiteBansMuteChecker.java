package net.envexus.svcmute.integrations.litebans;

import litebans.api.Database;
import net.envexus.svcmute.integrations.MuteChecker;
import org.bukkit.entity.Player;

public class LiteBansMuteChecker implements MuteChecker {
    private final Database database;

    public LiteBansMuteChecker() {
        this.database = Database.get();
    }

    @Override
    public boolean isPlayerMuted(Player player) {
        return database.isPlayerMuted(player.getUniqueId(), null);
    }

    @Override
    public long getUnmuteTime(Player player) {
        var userMute = database.getMute(player.getUniqueId(), null, null);

        if (userMute == null) {
            return -1;
        }

        return userMute.getDateEnd();
    }
}

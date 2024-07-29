package net.envexus.svcmute.integrations.svcmute;

import net.envexus.svcmute.integrations.MuteChecker;
import net.envexus.svcmute.util.SQLiteHelper;
import org.bukkit.entity.Player;

public class SQLiteMuteChecker implements MuteChecker {
    private final SQLiteHelper db;

    public SQLiteMuteChecker(SQLiteHelper db) {
        this.db = db;
    }

    @Override
    public boolean isPlayerMuted(Player player) {
        Long unmuteTime = db.getUnmuteTime(player.getUniqueId().toString());
        return unmuteTime != null && unmuteTime > System.currentTimeMillis();
    }
}
package net.envexus.svcmute.integrations.svcmute;

import net.envexus.svcmute.integrations.MuteChecker;
import net.envexus.svcmute.util.SQLiteHelper;
import org.bukkit.entity.Player;

import java.util.UUID;

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

    @Override
    public long getUnmuteTime(Player player) {
        Long unmuteTime = db.getUnmuteTime(player.getUniqueId().toString());

        if (unmuteTime == null) {
            return -1;
        }

        return unmuteTime;
    }
}
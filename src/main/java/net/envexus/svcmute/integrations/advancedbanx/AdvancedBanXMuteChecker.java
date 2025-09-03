package net.envexus.svcmute.integrations.advancedbanx;

import net.hnt8.advancedban.manager.PunishmentManager;
import net.hnt8.advancedban.utils.Punishment;
import net.envexus.svcmute.integrations.MuteChecker;
import net.hnt8.advancedban.manager.UUIDManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class AdvancedBanXMuteChecker implements MuteChecker {
    private final UUIDManager uuidManager;
    private final PunishmentManager punishmentManager;

    public AdvancedBanXMuteChecker(Plugin plugin) {
        this.punishmentManager = PunishmentManager.get();
        this.uuidManager = UUIDManager.get();
    }

    @Override
    public boolean isPlayerMuted(Player player) {
        String uuid = uuidManager.getUUID(player.getName());
        Punishment punishment = punishmentManager.getMute(uuid);
        return punishment != null;
    }

    @Override
    public long getUnmuteTime(Player player) {
        String uuid = uuidManager.getUUID(player.getName());
        Punishment punishment = punishmentManager.getMute(uuid);

        if (punishment == null) {
            return -1;
        }

        return punishment.getEnd();
    }
}

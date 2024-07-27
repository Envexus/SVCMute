package net.envexus.svcmute.integrations.advancedbans;

import me.leoko.advancedban.manager.PunishmentManager;
import me.leoko.advancedban.manager.UUIDManager;
import me.leoko.advancedban.utils.Punishment;
import net.envexus.svcmute.integrations.MuteChecker;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class AdvancedBansMuteChecker implements MuteChecker {
    private final PunishmentManager punishmentManager;

    public AdvancedBansMuteChecker(Plugin plugin) {
        this.punishmentManager = PunishmentManager.get();
    }

    @Override
    public boolean isPlayerMuted(Player player) {
        String uuid = UUIDManager.get().getUUID(player.getName());
        Punishment punishment = punishmentManager.getMute(uuid);
        // Return true if there's any mute, regardless of duration
        return punishment != null;
    }
}

package net.envexus.svcmute.integrations;

import org.bukkit.entity.Player;

public interface MuteChecker {
    boolean isPlayerMuted(Player player);
    long getUnmuteTime(Player player);
}

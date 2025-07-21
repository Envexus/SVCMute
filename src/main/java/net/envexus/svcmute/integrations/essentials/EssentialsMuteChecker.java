package net.envexus.svcmute.integrations.essentials;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import net.envexus.svcmute.integrations.MuteChecker;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class EssentialsMuteChecker implements MuteChecker {
    private final Essentials essentials;

    public EssentialsMuteChecker(Plugin plugin) {
        this.essentials = (Essentials) plugin;
    }

    @Override
    public boolean isPlayerMuted(Player player) {
        User user = essentials.getUser(player);
        return user != null && user.isMuted();
    }

    @Override
    public long getUnmuteTime(Player player) {
        User user = essentials.getUser(player);

        if (user == null) {
            return -1;
        }

        return user.getMuteTimeout();
    }
}

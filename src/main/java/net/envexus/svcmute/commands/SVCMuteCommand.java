package net.envexus.svcmute.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Syntax;
import com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;
import net.envexus.svcmute.SVCMute;
import net.envexus.svcmute.integrations.IntegrationManager;
import net.envexus.svcmute.util.SQLiteHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.Anon8281.universalScheduler.UniversalScheduler;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@CommandAlias("svcmute")
@CommandPermission("voicechat.mute")
public class SVCMuteCommand extends BaseCommand {

    private final SQLiteHelper db;
    private final SVCMute plugin;
    private final IntegrationManager integrationManager;

    public SVCMuteCommand(SQLiteHelper db, SVCMute plugin, IntegrationManager integrationManager) {
        this.db = db;
        this.plugin = plugin;
        this.integrationManager = integrationManager;
    }

    @Default
    @Syntax("<player> <time>")
    @Description("Mute a player from voice chat for a specified time.")
    public void onMute(CommandSender sender, String playerName, String timeStr) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            sender.sendMessage("Player not found.");
            return;
        }

        long muteDurationMillis = parseTime(timeStr);
        if (muteDurationMillis <= 0) {
            sender.sendMessage("Invalid time format. Use examples: 1s, 5m, 2d.");
            return;
        }

        UUID playerUUID = player.getUniqueId();
        long unmuteTime = System.currentTimeMillis() + muteDurationMillis;

        db.addMute(playerUUID.toString(), unmuteTime);
        sender.sendMessage(playerName + " has been muted for " + timeStr + ".");

        integrationManager.addMutedPlayer(playerUUID, unmuteTime);

        // Schedule unmute task
        UniversalScheduler.getScheduler(this.plugin).runTaskLater(() -> {
            Long storedUnmuteTime = db.getUnmuteTime(playerUUID.toString());
            if (storedUnmuteTime != null && storedUnmuteTime <= System.currentTimeMillis()) {
                db.removeMute(playerUUID.toString());
                integrationManager.removeMutedPlayer(playerUUID);
            }
        }, muteDurationMillis / 50L); // Bukkit scheduler uses ticks, so divide by 50
    }

    private long parseTime(String timeStr) {
        long durationMillis = -1;
        try {
            char unit = timeStr.charAt(timeStr.length() - 1);
            long amount = Long.parseLong(timeStr.substring(0, timeStr.length() - 1));

            switch (unit) {
                case 's':
                    durationMillis = TimeUnit.SECONDS.toMillis(amount);
                    break;
                case 'm':
                    durationMillis = TimeUnit.MINUTES.toMillis(amount);
                    break;
                case 'h':
                    durationMillis = TimeUnit.HOURS.toMillis(amount);
                    break;
                case 'd':
                    durationMillis = TimeUnit.DAYS.toMillis(amount);
                    break;
                default:
                    return -1; // Invalid unit
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            return -1; // Invalid format
        }

        return durationMillis;
    }
}

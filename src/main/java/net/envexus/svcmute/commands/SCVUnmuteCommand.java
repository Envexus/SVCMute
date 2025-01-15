package net.envexus.svcmute.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.envexus.svcmute.integrations.IntegrationManager;
import net.envexus.svcmute.util.SQLiteHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("svcunmute")
@CommandPermission("voicechat.svcunmute")
@Description("Unmute a player from voice chat.")
public class SCVUnmuteCommand extends BaseCommand {

    private final SQLiteHelper db;
    private final IntegrationManager integrationManager;

    public SCVUnmuteCommand(SQLiteHelper db, IntegrationManager integrationManager) {
        this.db = db;
        this.integrationManager = integrationManager;
    }

    @Default
    @Syntax("<player>")
    @Description("Unmute a player from voice chat.")
    public void onUnmute(CommandSender sender, String playerName) {

        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            sender.sendMessage("Player not found.");
            return;
        }

        UUID playerUUID = player.getUniqueId();
        if (!db.isMuted(playerUUID.toString())) {
            sender.sendMessage(playerName + " is not muted.");
            return;
        }

        sender.sendMessage(playerName + " has been unmuted.");
        db.removeMute(playerUUID.toString());
        integrationManager.removeMutedPlayer(playerUUID);
    }
}

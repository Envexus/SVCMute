package net.envexus.svcmute.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.envexus.svcmute.integrations.IntegrationManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SVCMutePlaceholderExpansion extends PlaceholderExpansion {
    private final IntegrationManager integrationManager;

    public SVCMutePlaceholderExpansion(IntegrationManager manager) {
        this.integrationManager = manager;
    }

    @Override
    public @NotNull String getAuthor() {
        return "Envexus";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "svcmute";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
        if (offlinePlayer instanceof Player player) {
            return switch (params) {
                case "muted" -> integrationManager.isPlayerMuted(player) ? "yes" : "no";
                case "remaining" -> integrationManager.getRemainingTime(player);
                case "remaining_ms" -> String.valueOf(integrationManager.getRemainingMilliseconds(player));
                default -> null;
            };
        }

        return null;
    }

    @Override
    public @NotNull List<String> getPlaceholders() {
        return List.of("muted", "remaining", "remaining_ms");
    }
}

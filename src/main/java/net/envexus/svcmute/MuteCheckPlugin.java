package net.envexus.svcmute;

import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import org.bukkit.entity.Player;
import litebans.api.Database;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MuteCheckPlugin implements VoicechatPlugin {

    private Set<UUID> notifiedPlayers;

    public MuteCheckPlugin() {
        this.notifiedPlayers = new HashSet<>();
    }

    /**
     * @return the unique ID for this voice chat plugin
     */
    @Override
    public String getPluginId() {
        return "mutecheck_voicechat";
    }

    /**
     * Called when the voice chat initializes the plugin.
     *
     * @param api the voice chat API
     */
    @Override
    public void initialize(VoicechatApi api) {

    }

    /**
     * Called once by the voice chat to register all events.
     *
     * @param registration the event registration
     */
    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(MicrophonePacketEvent.class, this::onMicrophone);
    }

    /**
     * This method is called whenever a player sends audio to the server via the voice chat.
     *
     * @param event the microphone packet event
     */
    private void onMicrophone(MicrophonePacketEvent event) {

        if (event.getSenderConnection() == null) {
            return;
        }
        if (!(event.getSenderConnection().getPlayer().getPlayer() instanceof Player player)) {
            return;
        }

        if (isPlayerMuted(player)) {
            event.cancel();

            if (!notifiedPlayers.contains(player.getUniqueId())) {
                player.sendMessage("You are muted and cannot use voice chat.");
                notifiedPlayers.add(player.getUniqueId());
            }
        } else {
            notifiedPlayers.remove(player.getUniqueId());
        }
    }

    /**
     * Check if the player is muted using LiteBans
     *
     * @param player the player to check
     * @return true if the player is muted, false otherwise
     */
    private boolean isPlayerMuted(Player player) {
        return Database.get().isPlayerMuted(player.getUniqueId(), null);
    }
}

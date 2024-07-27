package net.envexus.svcmute;

import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import net.envexus.svcmute.integrations.IntegrationManager;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MuteCheckPlugin implements VoicechatPlugin {

    private final Set<UUID> notifiedPlayers;
    private final IntegrationManager integrationManager;

    public MuteCheckPlugin(IntegrationManager integrationManager) {
        this.notifiedPlayers = new HashSet<>();
        this.integrationManager = integrationManager;
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

        if (integrationManager.isPlayerMuted(player)) {
            event.cancel();

            if (!notifiedPlayers.contains(player.getUniqueId())) {
                player.sendMessage("You are muted and cannot use voice chat.");
                notifiedPlayers.add(player.getUniqueId());
            }
        } else {
            notifiedPlayers.remove(player.getUniqueId());
        }
    }
}

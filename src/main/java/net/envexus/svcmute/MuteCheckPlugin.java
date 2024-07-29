package net.envexus.svcmute;

import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import net.envexus.svcmute.configuration.ConfigurationManager;
import net.envexus.svcmute.integrations.IntegrationManager;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MuteCheckPlugin implements VoicechatPlugin {

    private final Set<UUID> notifiedPlayers;
    private final IntegrationManager integrationManager;
    private final ConfigurationManager configurationManager;

    public MuteCheckPlugin(IntegrationManager integrationManager, ConfigurationManager messagesManager) {
        this.notifiedPlayers = new HashSet<>();
        this.integrationManager = integrationManager;
        this.configurationManager = messagesManager;
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
        // Initialization logic if needed
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

            if (configurationManager.getConfig().getBoolean("actionbar", false)) {
                player.sendActionBar(configurationManager.getLocaleString("actionbar.muted"));
            }

            if (configurationManager.getConfig().getBoolean("message", false) && !notifiedPlayers.contains(player.getUniqueId())) {
                player.sendMessage(configurationManager.getLocaleString("messages.muted"));
                notifiedPlayers.add(player.getUniqueId());
            }
        } else {
            notifiedPlayers.remove(player.getUniqueId());
        }
    }
}

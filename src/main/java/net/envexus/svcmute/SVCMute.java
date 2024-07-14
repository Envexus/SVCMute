package net.envexus.svcmute;

import de.maxhenkel.voicechat.api.BukkitVoicechatService;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class SVCMute extends JavaPlugin {

    private static final String PLUGIN_ID = "mutecheck_voicechat";
    public static final Logger LOGGER = Logger.getLogger(PLUGIN_ID);

    private MuteCheckPlugin voicechatPlugin;

    @Override
    public void onEnable() {
        BukkitVoicechatService service = getServer().getServicesManager().load(BukkitVoicechatService.class);
        if (service != null) {
            voicechatPlugin = new MuteCheckPlugin();
            service.registerPlugin(voicechatPlugin);
            LOGGER.info("Successfully registered voice chat mutecheck plugin");
        } else {
            LOGGER.info("Failed to register voice chat mutecheck plugin");
        }
    }

    @Override
    public void onDisable() {
        if (voicechatPlugin != null) {
            getServer().getServicesManager().unregister(voicechatPlugin);
            LOGGER.info("Successfully unregistered voice chat mutecheck plugin");
        }
    }
}

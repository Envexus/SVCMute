package net.envexus.svcmute;

import co.aikar.commands.BukkitCommandManager;
import de.maxhenkel.voicechat.api.BukkitVoicechatService;
import net.envexus.svcmute.commands.SVCMuteCommand;
import net.envexus.svcmute.configuration.ConfigurationManager;
import net.envexus.svcmute.integrations.IntegrationManager;
import net.envexus.svcmute.util.SQLiteHelper;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class SVCMute extends JavaPlugin {

    private static final String PLUGIN_ID = "mutecheck_voicechat";
    public static final Logger LOGGER = Logger.getLogger(PLUGIN_ID);

    private MuteCheckPlugin voicechatPlugin;
    private SQLiteHelper sqliteHelper;
    private IntegrationManager integrationManager;

    @Override
    public void onEnable() {
        ConfigurationManager messageManager = new ConfigurationManager(this);

        // Initialize SQLiteHelper
        sqliteHelper = new SQLiteHelper(this);

        // Initialize IntegrationManager with SQLiteHelper
        integrationManager = new IntegrationManager(sqliteHelper);

        // Register voice chat plugin
        BukkitVoicechatService service = getServer().getServicesManager().load(BukkitVoicechatService.class);
        if (service != null) {
            voicechatPlugin = new MuteCheckPlugin(integrationManager, messageManager);
            service.registerPlugin(voicechatPlugin);
            LOGGER.info("Successfully registered voice chat mutecheck plugin");
        } else {
            LOGGER.info("Failed to register voice chat mutecheck plugin");
        }

        // Initialize ACF Command Manager
        BukkitCommandManager manager = new BukkitCommandManager(this);

        // Register commands
        manager.registerCommand(new SVCMuteCommand(sqliteHelper, this, integrationManager));
    }

    @Override
    public void onDisable() {
        if (voicechatPlugin != null) {
            getServer().getServicesManager().unregister(voicechatPlugin);
            LOGGER.info("Successfully unregistered voice chat mutecheck plugin");
        }
    }
}

package net.envexus.svcmute.configuration;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ConfigurationManager {

    private final JavaPlugin plugin;
    private final MiniMessage miniMessage;
    private FileConfiguration messagesConfig;
    private FileConfiguration pluginConfig;

    public ConfigurationManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.miniMessage = MiniMessage.miniMessage();
        loadFiles();
    }

    /**
     * Reload configuration from files.
     */
    public void loadFiles() {
        this.messagesConfig = loadConfiguration("locale.yml");
        this.pluginConfig = loadConfiguration("config.yml");
    }

    /**
     * Function for quickly loading configuration.
     * @param filename name of the file from resources
     * @return configuration
     */
    private FileConfiguration loadConfiguration(String filename) {
        File messagesFile = new File(plugin.getDataFolder(), filename);

        if (!messagesFile.exists()) {
            plugin.saveResource(filename, false);
        }

        return YamlConfiguration.loadConfiguration(messagesFile);
    }

    /**
     * Get localized string from "locale.yml".
     * @param key key of the localized message
     * @param resolvers adventure resolvers for custom tags
     * @return adventure formatted component
     */
    public Component getLocaleString(String key, TagResolver... resolvers) {
        TagResolver[] combinedResolvers = new TagResolver[resolvers.length + 1];
        combinedResolvers[0] = Placeholder.component("prefix", miniMessage.deserialize(messagesConfig.getString("prefix", "")));
        System.arraycopy(resolvers, 0, combinedResolvers, 1, resolvers.length);
        return miniMessage.deserialize(messagesConfig.getString(key, "<red>Message not found for key: %s</red>".formatted(key)), combinedResolvers);
    }

    /**
     * Get the loaded plugin configuration.
     * @return configuration from "config.yml" file
     */
    public FileConfiguration getConfig() {
        return pluginConfig;
    }
}

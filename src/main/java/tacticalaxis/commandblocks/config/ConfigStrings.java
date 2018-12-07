package tacticalaxis.commandblocks.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginDescriptionFile;
import tacticalaxis.commandblocks.Main;

public class ConfigStrings {

    public static String createCommandMessage;
    public static String removeCommandMessage;
    public static String onTeleportMessage;
    public static String onReloadMessage;
    public static String addCommandMessage;
    public static String noPermissionMessage;
    public static String consoleMessage;
    public static String versionMessage;

    static {
        createCommandMessage = "create-command";
        removeCommandMessage = "remove-command";
        addCommandMessage = "add-command";
        onTeleportMessage = "on-teleport";
        onReloadMessage = "on-reload";
        noPermissionMessage = "no-permission";
        consoleMessage = "console-message";
        versionMessage = "version-message";
    }

    public static String getMessage(String type, String name) {
        ConfigurationSection cs = Main.getInstance().getConfig().getConfigurationSection("messages");
        PluginDescriptionFile pdf = Main.getInstance().getDescription();
        return cs.get(type).toString()
                .replace("&","§")
                .replace("%name%", name)
                .replace("%version%", pdf.getVersion());
    }
}

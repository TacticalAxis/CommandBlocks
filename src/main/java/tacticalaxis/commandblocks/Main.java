package tacticalaxis.commandblocks;

import org.bukkit.plugin.java.JavaPlugin;
import tacticalaxis.commandblocks.commands.CBCommand;
import tacticalaxis.commandblocks.config.ConfigurationManager;
import tacticalaxis.commandblocks.events.PlayerInteract;

public final class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        ConfigurationManager.createConfig();
        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        this.getCommand("cb").setExecutor(new CBCommand());
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static Main getInstance() {
        return instance;
    }
}

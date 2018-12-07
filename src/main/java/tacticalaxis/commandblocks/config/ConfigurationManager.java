package tacticalaxis.commandblocks.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import tacticalaxis.commandblocks.Main;

import java.io.File;
import java.util.Arrays;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ConfigurationManager {

    public static void createConfig() {
        try {
            if (!Main.getInstance().getDataFolder().exists()) {
                Main.getInstance().getDataFolder().mkdirs();
            }
            File file = new File(Main.getInstance().getDataFolder(), "config.yml");
            if (!file.exists()) {
                Main.getInstance().getLogger().info("config.yml not found, creating!");
                Main.getInstance().saveDefaultConfig();
                Main.getInstance().getConfig().createSection("messages");
                Main.getInstance().getConfig().createSection("command-list");
                Main.getInstance().reloadConfig();
            } else {
                Main.getInstance().getLogger().info("config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reloadConfig() {
        ConfigurationManager.createConfig();
        Main.getInstance().reloadConfig();
    }

    public void addCommandBlock(String name, boolean playerCommand, Location l, String command) {
        FileConfiguration config = Main.getInstance().getConfig();
        ConfigurationSection commandListSection = config.getConfigurationSection("command-list");
        ConfigurationSection newCommand = commandListSection.createSection(name);
        newCommand.set("playercommand", String.valueOf(playerCommand) + ";");
        newCommand.set("location", l.getWorld().getName() + ";" + l.getBlockX() + ";" + l.getBlockY() + ";" + l.getBlockZ());
        newCommand.set("commands", command + ";");
        Main.getInstance().saveConfig();
        ConfigurationManager.createConfig();
        Main.getInstance().reloadConfig();
    }

    public void appendCommand(String name, boolean playerCommand, String command) {
        FileConfiguration config = Main.getInstance().getConfig();
        ConfigurationSection commandListSection = config.getConfigurationSection("command-list");
        String set1 = commandListSection.getConfigurationSection(name).get("commands").toString().concat(command + ";");
        commandListSection.getConfigurationSection(name).set("commands", set1);
        String previousBoolean = commandListSection.getConfigurationSection(name).get("playercommand").toString().concat(String.valueOf(playerCommand) + ";");
        commandListSection.getConfigurationSection(name).set("playercommand", previousBoolean);
        Main.getInstance().saveConfig();
        ConfigurationManager.createConfig();
        Main.getInstance().reloadConfig();
    }

    public void removeCommand(String name) {
        FileConfiguration config = Main.getInstance().getConfig();
        ConfigurationSection commandListSection = config.getConfigurationSection("command-list");
        if (commandListSection.getConfigurationSection(name) != null) {
            commandListSection.set(name, null);
            Main.getInstance().saveConfig();
            ConfigurationManager.createConfig();
            Main.getInstance().reloadConfig();
        }
    }

    public void execute(Player player, Location location) {
        ConfigurationSection configurationSection = Main.getInstance().getConfig().getConfigurationSection("command-list");
        for (String s : configurationSection.getKeys(false)) {
            String[] all = configurationSection.getConfigurationSection(s).get("location").toString().split(";");
            World world = Bukkit.getWorld(all[0]);
            Double x = Double.parseDouble(all[1]);
            Double y = Double.parseDouble(all[2]);
            Double z = Double.parseDouble(all[3]);
            Location l = new Location(world, x, y, z);
            if (location.distance(l) < 1) {
                String[] commands = configurationSection.getConfigurationSection(s).get("commands").toString().split(";");
                String[] booleans = configurationSection.getConfigurationSection(s).get("playercommand").toString().split(";");
                if (commands.length == 1 && booleans.length == 1) {
                    if (booleans[0].equalsIgnoreCase("true")) {
                        if (Bukkit.getOperators().contains(player)) {
                            player.performCommand(commands[0]);
                        } else {
                            player.setOp(true);
                            player.performCommand(commands[0]);
                            player.setOp(false);
                        }
                    } else if (booleans[0].equalsIgnoreCase("false")) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commands[0]);
                    }
                } else {
                    for (String cmds : commands) {
                        int number = Arrays.asList(commands).indexOf(cmds);
                        if (booleans[number].equalsIgnoreCase("true")) {
                            if (Bukkit.getOperators().contains(player)) {
                                player.performCommand(cmds);
                            } else {
                                player.setOp(true);
                                player.performCommand(cmds);
                                player.setOp(false);
                            }
                        } else if (booleans[number].equalsIgnoreCase("false")) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmds);
                        }
                    }
                }
            }
        }
    }
}

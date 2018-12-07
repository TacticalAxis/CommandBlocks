package tacticalaxis.commandblocks.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import tacticalaxis.commandblocks.Main;
import tacticalaxis.commandblocks.config.ConfigStrings;
import tacticalaxis.commandblocks.config.ConfigurationManager;

import java.util.Set;

public class CBCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("cb")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Location location = player.getTargetBlock((Set<Material>) null, 100).getLocation();
                ConfigurationManager configurationManager = new ConfigurationManager();
                ConfigurationSection cs = Main.getInstance().getConfig().getConfigurationSection("command-list");
                if (sender.hasPermission("commandblocks.admin")) {
                    if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("list")) {
                            sender.sendMessage(ChatColor.GREEN + "=======CURRENT COMMAND BLOCKS=======");
                            for (String s : cs.getKeys(false)) {
                                sender.sendMessage(s);
                            }
                            sender.sendMessage(ChatColor.GREEN + "====================================");
                        } else if (args[0].equalsIgnoreCase("reload")) {
                            ConfigurationManager.reloadConfig();
                            sender.sendMessage(ConfigStrings.getMessage(ConfigStrings.onReloadMessage, ""));
                        } else {
                            sender.sendMessage(ConfigStrings.getMessage(ConfigStrings.versionMessage, ""));
                        }
                    } else if (args.length == 2) {
                        if (args[0].equalsIgnoreCase("tp")) {
                            if (cs.get(args[1]).toString() != null) {
                                String[] locationArray1 = cs.getConfigurationSection(args[1]).get("location").toString().split(";");
                                player.teleport(new Location(Bukkit.getWorld(locationArray1[0]), Double.parseDouble(locationArray1[1]) + 0.5, Double.parseDouble(locationArray1[2]) + 1, Double.parseDouble(locationArray1[3]) + 0.5));
                            }
                            sender.sendMessage(ConfigStrings.getMessage(ConfigStrings.onTeleportMessage, args[1]));
                        } else if (args[0].equalsIgnoreCase("remove")) {
                            configurationManager.removeCommand(args[1]);
                            sender.sendMessage(ConfigStrings.getMessage(ConfigStrings.removeCommandMessage, args[1]));
                        } else {
                            sender.sendMessage(ConfigStrings.getMessage(ConfigStrings.versionMessage, ""));
                        }
                    } else if (args.length > 3) {
                        if (args[0].equalsIgnoreCase("create")) {
                            if (!(args[2].contains("true") || args[2].contains("false"))) {
                                sender.sendMessage(ChatColor.RED + "Your second argument must be 'true' or 'false' (player command)");
                                return true;
                            }
                            StringBuilder sb = new StringBuilder();
                            for (int i = 3; i < args.length; i++) {
                                sb.append(args[i]).append(" ");
                            }
                            String allArgs = sb.toString().trim();
                            configurationManager.addCommandBlock(args[1], Boolean.getBoolean(args[2]), location, allArgs);
                            sender.sendMessage(ConfigStrings.getMessage(ConfigStrings.createCommandMessage, args[1]));
                        } else if (args[0].equalsIgnoreCase("add")) {
                            if (!(args[2].contains("false") || args[2].contains("true"))) {
                                sender.sendMessage(ChatColor.RED + "Your second argument must be 'true' or 'false' (player command)");
                                return true;
                            }
                            StringBuilder sb = new StringBuilder();
                            for (int i = 3; i < args.length; i++) {
                                sb.append(args[i]).append(" ");
                            }
                            String allArgs = sb.toString().trim();
                            try {
                                ConfigurationSection cs2 = Main.getInstance().getConfig().getConfigurationSection("command-list");
                                boolean set = false;
                                for (String s : cs2.getKeys(false)) {
                                    String[] all = cs2.getConfigurationSection(s).get("location").toString().split(";");
                                    World world = Bukkit.getWorld(all[0]);
                                    Double x = Double.parseDouble(all[1]);
                                    Double y = Double.parseDouble(all[2]);
                                    Double z = Double.parseDouble(all[3]);
                                    Location l = new Location(world, x, y, z);
                                    if (location.distance(l) < 1) {
                                        set = true;
                                    }
                                }
                                if (!set) {
                                    sender.sendMessage(ChatColor.RED + "Sorry, that command block name already exists or has not been created yet!");
                                } else {
                                    configurationManager.appendCommand(args[1], Boolean.getBoolean(args[2]), allArgs);
                                }
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            sender.sendMessage(ConfigStrings.getMessage(ConfigStrings.addCommandMessage, args[1]));
                        }
                    }
                } else {
                    sender.sendMessage(ConfigStrings.getMessage(ConfigStrings.versionMessage, ""));
                }
            } else {
                sender.sendMessage(ConfigStrings.getMessage(ConfigStrings.noPermissionMessage, ""));
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(ConfigStrings.getMessage(ConfigStrings.consoleMessage, ""));
        }
        return true;
    }
}

package tacticalaxis.commandblocks.events;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import tacticalaxis.commandblocks.config.ConfigurationManager;

public class PlayerInteract implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        try {
            if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if(event.getPlayer().hasPermission("commandblock.use")) {
                    ConfigurationManager configurationManager = new ConfigurationManager();
                    Location location = event.getClickedBlock().getLocation();
                    configurationManager.execute(event.getPlayer(), location);
                }
            }
        } catch (Exception ignored){}

    }

}

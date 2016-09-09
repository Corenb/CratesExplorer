package eu.horyzon.cratesexplorer.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import eu.horyzon.cratesexplorer.objects.cratestype.Crates;
import net.md_5.bungee.api.ChatColor;

public class PlayerModify implements Listener {
	public static Map<UUID, String> modify = new HashMap<UUID, String>();

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();

		if(e.getAction().equals(Action.PHYSICAL) || e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getClickedBlock() == null)
			return;

		if(modify.containsKey(p.getUniqueId())) {
			Crates c = Crates.getCrates(modify.get(p.getUniqueId()));
			if(c.getMaterial().equals(e.getMaterial()))
				return;

			if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
				if(c.removeCrate(e.getClickedBlock().getState()))
					p.sendMessage(ChatColor.DARK_GREEN + "Crate removed!");
				else
					p.sendMessage(ChatColor.RED + "Isn't a crate!");
			} else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				if(c.addCrate(e.getClickedBlock().getState()))
					p.sendMessage(ChatColor.DARK_GREEN + "Crate added!");
				else
					p.sendMessage(ChatColor.RED + "It's already a crate!");
			}
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
		
	}
}
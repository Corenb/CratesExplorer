package eu.horyzon.cratesexplorer.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import eu.horyzon.cratesexplorer.objects.cratestype.Crates;
import net.md_5.bungee.api.ChatColor;

public class PlayerModify implements Listener {
	public static Map<UUID, String> modify = new HashMap<UUID, String>();

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();

		if(!modify.containsKey(p.getUniqueId()) || e.getAction().equals(Action.PHYSICAL) || !e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getClickedBlock() == null)
			return;

		Crates c = Crates.getCrates(modify.get(p.getUniqueId()));

		if(!c.getMaterial().equals(e.getClickedBlock().getType()))
			return;

		if(p.isSneaking()) {
			if(c.removeCrate(e.getClickedBlock().getState()))
				p.sendMessage(ChatColor.DARK_GREEN + "Crate removed!");
			else
				p.sendMessage(ChatColor.RED + "Isn't a crate!");
		} else {
			if(c.addCrate(e.getClickedBlock().getState()))
				p.sendMessage(ChatColor.DARK_GREEN + "Crate added!");
			else
				p.sendMessage(ChatColor.RED + "It's already a crate!");
		}
		e.setCancelled(true);
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractAtEntityEvent e) {
		Player p = e.getPlayer();

		if(!modify.containsKey(p.getUniqueId()) || !(e.getRightClicked() instanceof ArmorStand))
			return;

		Crates c = Crates.getCrates(modify.get(p.getUniqueId()));

		if(p.isSneaking()) {
			if(c.removeCrate(e.getRightClicked()))
				p.sendMessage(ChatColor.DARK_GREEN + "Crate removed!");
			else
				p.sendMessage(ChatColor.RED + "Isn't a crate!");
		} else {
			if(c.addCrate(e.getRightClicked()))
				p.sendMessage(ChatColor.DARK_GREEN + "Crate added!");
			else
				p.sendMessage(ChatColor.RED + "It's already a crate!");
		}
		e.setCancelled(true);
	}
}
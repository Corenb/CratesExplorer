package eu.horyzon.cratesexplorer.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.DirectionalContainer;

import eu.horyzon.cratesexplorer.objects.cratestype.Crates;

public class PlayerExplore implements Listener {

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Block bloc = e.getClickedBlock();
		if(bloc == null || !(bloc.getState() instanceof DirectionalContainer))
			return;

		for (Crates crate : Crates.cratesList) {
			if (!crate.isCrates(bloc.getState()))
				continue;

			crate.playAnimation(crate, e.getPlayer());
			break;
		}
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
		
	}
}
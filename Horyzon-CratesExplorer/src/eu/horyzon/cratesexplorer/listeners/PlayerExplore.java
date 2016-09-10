package eu.horyzon.cratesexplorer.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.DirectionalContainer;

import eu.horyzon.cratesexplorer.CratesExplorer;
import eu.horyzon.cratesexplorer.objects.cratestype.Crates;

public class PlayerExplore implements Listener {
	private static Map<Object, UUID> inUse = new HashMap<Object, UUID>();

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {

		Block bloc = e.getClickedBlock();
		if(bloc == null || !(bloc.getState().getData() instanceof DirectionalContainer) || !e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;
		CratesExplorer.getInstance().getLogger().info("ok");

		for (Crates crate : Crates.cratesList) {
			if (!crate.isCrates(bloc.getState()))
				continue;

			if (crate.isModifyMode())
				return;

			e.setCancelled(true);

			if (crate.respawn()) {
				if (inUse.containsKey(bloc))
					return;
				else
					inUse.put(bloc, e.getPlayer().getUniqueId());
			}

			crate.playAnimation(bloc, e.getPlayer());
			break;
		}
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
		
	}

	public static void removeUse(Object crate) {
		inUse.remove(crate);
	}
}
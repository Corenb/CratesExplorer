package eu.horyzon.cratesexplorer.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.DirectionalContainer;

import eu.horyzon.cratesexplorer.objects.cratestype.ArmorstandCrates;
import eu.horyzon.cratesexplorer.objects.cratestype.ContainerCrates;
import eu.horyzon.cratesexplorer.objects.cratestype.Crates;

public class PlayerExplore implements Listener {
	private static Map<Object, UUID> inUse = new HashMap<Object, UUID>();

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Block bloc = e.getClickedBlock();

		if (bloc == null || !(bloc.getState().getData() instanceof DirectionalContainer)
				|| !e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;

		Player p = e.getPlayer();

		for (Crates crate : Crates.cratesList) {
			if (!(crate instanceof ContainerCrates) || !crate.isCrate(bloc.getState()))
				continue;

			if (crate.isModifyMode())
				return;

			e.setCancelled(true);

			if (crate.respawn()) {
				if (inUse.containsKey(bloc))
					return;
				else
					inUse.put(bloc, p.getUniqueId());
			}

			crate.playAnimation(bloc, p);
			break;
		}
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractAtEntityEvent e) {
		if (!(e.getRightClicked() instanceof ArmorStand))
			return;

		Player p = e.getPlayer();
		ArmorStand as = (ArmorStand) e.getRightClicked();

		for (Crates crate : Crates.cratesList) {
			if (!(crate instanceof ArmorstandCrates) || !crate.isCrate(as))
				continue;

			if (crate.isModifyMode())
				return;

			e.setCancelled(true);

			if (crate.respawn()) {
				if (inUse.containsKey(as))
					return;
				else
					inUse.put(as, p.getUniqueId());
			}

			crate.playAnimation(as, p);
			break;
		}
	}

	public static void removeUse(Object crate) {
		inUse.remove(crate);
	}

	public static boolean isInUse(Object crate) {
		return inUse.containsKey(crate);
	}
}
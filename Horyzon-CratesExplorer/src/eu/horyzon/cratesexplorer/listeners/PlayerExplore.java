package eu.horyzon.cratesexplorer.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import eu.horyzon.cratesexplorer.objects.cratestype.Crate;
import eu.horyzon.cratesexplorer.objects.cratestype.RunnableCrate;

public class PlayerExplore implements Listener {
	private static Map<Object, UUID> inUse = new HashMap<Object, UUID>();

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getClickedBlock() == null)
			return;

		Block bloc = e.getClickedBlock();

		try {
			RunnableCrate crate = (RunnableCrate) Crate.getCratesfromCrate(bloc.getState());

			if (crate.isModify())
				return;

			Player p = e.getPlayer();

			if (!crate.isPermanent())
				if (inUse.containsKey(bloc))
					return;
				else
					inUse.put(bloc, p.getUniqueId());

			crate.open(bloc, p);
			e.setCancelled(true);
		} catch (NullPointerException ex) {
		}
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractAtEntityEvent e) {
		try {
			Entity en = e.getRightClicked();
			RunnableCrate crate = (RunnableCrate) Crate.getCratesfromCrate(en.getUniqueId());

			if (crate.isModify())
				return;

			Player p = e.getPlayer();

			if (!crate.isPermanent())
				if (inUse.containsKey(en.getUniqueId()))
					return;
				else
					inUse.put(en.getUniqueId(), p.getUniqueId());

			crate.open(en, p);
			e.setCancelled(true);
		} catch (NullPointerException ex) {
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		try {
			Crate.getCratesfromCrate(e.getEntity()).removeCrate(e.getEntity().getUniqueId());
		} catch (NullPointerException ex) {
		}
	}

	public static void removeUse(Object crate) {
		inUse.remove(crate);
	}

	public static boolean isInUse(Object crate) {
		return inUse.containsKey(crate);
	}
}
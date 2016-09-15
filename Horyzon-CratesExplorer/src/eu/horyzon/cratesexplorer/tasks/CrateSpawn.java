package eu.horyzon.cratesexplorer.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import eu.horyzon.cratesexplorer.CratesExplorer;
import eu.horyzon.cratesexplorer.objects.cratestype.Crates;

public class CrateSpawn extends BukkitRunnable {

	public CrateSpawn(CratesExplorer plugin) {
		runTaskTimer(plugin, 0, 20);
	}

	@Override
	public void run() {
		long now = System.currentTimeMillis();
		for (Crates crate : Crates.cratesList) {
			if (crate.isRun() && (now - crate.getLast()) / 1000 >= crate.getSpawnTime())
				crate.respawnCrates();
		}
	}

}
package eu.horyzon.cratesexplorer.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import eu.horyzon.cratesexplorer.CratesExplorer;
import eu.horyzon.cratesexplorer.objects.cratestype.Crate;
import eu.horyzon.cratesexplorer.objects.cratestype.RunnableCrate;

public class CrateSpawn extends BukkitRunnable {

	public CrateSpawn(CratesExplorer plugin) {
		runTaskTimer(plugin, 0, 20);
	}

	@Override
	public void run() {
		for (Crate crate : Crate.allCrates) {
			if (!(crate instanceof RunnableCrate))
				return;

			RunnableCrate runnable = (RunnableCrate) crate;
			if (runnable.isValid(System.currentTimeMillis()))
				runnable.respawnCrates();
		}
	}
}
package eu.horyzon.cratesexplorer.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import eu.horyzon.cratesexplorer.CratesExplorer;
import eu.horyzon.cratesexplorer.objects.cratestype.Crates;

public class SpawnTask extends BukkitRunnable {
	String id;
	Boolean run = false;
	int spanTime;

	public SpawnTask(String id, int spanTime) {
		this.id = id;
		this.spanTime = spanTime;
	}

	@Override
	public void run() {
		Crates showCase = Crates.getShowCase(id);

		showCase.unspawnShowCase();
		showCase.spawnRandomShowCase();
	}

	public void start() {
		run = true;
		runTaskTimer(CratesExplorer.getInstance(), 0, spanTime);
	}

	public void stop() {
		run = false;
		cancel();
	}

	public void restart() {
		stop();
		start();
	}

	public boolean isRun() {
		return run;
	}
}
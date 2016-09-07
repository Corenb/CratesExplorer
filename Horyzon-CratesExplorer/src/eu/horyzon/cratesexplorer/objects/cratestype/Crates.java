package eu.horyzon.cratesexplorer.objects.cratestype;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import eu.horyzon.cratesexplorer.CratesExplorer;
import eu.horyzon.cratesexplorer.objects.rewardstype.Reward;

public abstract class Crates extends BukkitRunnable {
	protected String id;
	protected int useTime, spawnTime;
	protected double pourcent;
	protected Effect effect;
	protected Material material;
	protected Set<Reward> rewards;
	protected Set<Object> crates;
	protected Map<UUID, Long> repeat;
	protected boolean run = false;

	public static Set<Crates> cratesList = new HashSet<Crates>();

	@Override
	public void run() {
		unspawnCrates();
		spawnRandomCrates();
	}

	public void start() {
		run = true;
		runTaskTimer(CratesExplorer.getInstance(), 0, spawnTime);
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

	public static Crates getCrates(String id) throws IllegalArgumentException {
		for (Crates crate : cratesList) {
			if (crate.id.equals(id))
				return crate;
		}

		throw new IllegalArgumentException("No crates with this name");
	}

	public Set<Object> getCrates() {
		return crates;
	}

	public boolean hasEffect() {
		return effect != null;
	}

	public String getId() {
		return id;
	}

	public int getSpanTime() {
		return spawnTime;
	}

	public Material getMaterial() {
		return material;
	}

	public double getPourcent() {
		return pourcent;
	}

	public Boolean canRepeat() {
		return useTime != 0;
	}

	public void respawnCrates() {
		unspawnCrates();
		spawnRandomCrates();
	}

	public abstract Boolean isCrates(Object showCase);

	public abstract void spawnCrates(int amount);

	public abstract void spawnRandomCrates();

	public abstract void spawnAllCrates();

	public abstract void unspawnCrates();

	public abstract void playAnimation();
}
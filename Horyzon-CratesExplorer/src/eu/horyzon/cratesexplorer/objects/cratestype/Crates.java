package eu.horyzon.cratesexplorer.objects.cratestype;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Effect;
import org.bukkit.Material;

import eu.horyzon.cratesexplorer.objects.rewardstype.Reward;

public abstract class Crates {
	protected String id;
	protected int useTime, spawnTime;
	protected double pourcent;
	protected long last;
	protected Effect effect;
	protected Material material;
	protected Set<Reward> rewards;
	protected Set<Object> crates;
	protected Map<UUID, Long> repeat;
	protected boolean run = false;

	public static Set<Crates> cratesList = new HashSet<Crates>();

	public void start() throws IllegalStateException {
		if (run)
			throw new IllegalStateException("Task already started");

		run = true;
		respawnCrates();
	}

	public void stop() throws IllegalStateException {
		if (!run)
			throw new IllegalStateException("Task already stopped");

		run = false;
		spawnAllCrates();
	}

	public void restart() throws IllegalStateException {
		stop();
		start();
	}

	public boolean isRun() {
		return run;
	}

	public long getLast() {
		return last;
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

	public int getSpawnTime() {
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
		last = System.currentTimeMillis();
	}

	public Boolean isCrates(Object crate) {
		return crates.contains(crate);
	}

	public abstract boolean addCrate(Object crate);

	public abstract boolean removeCrate(Object crate);

	public abstract Boolean isSpawned(Object crate);

	public abstract void spawnCrates(int amount);

	public abstract void spawnRandomCrates();

	public abstract void spawnAllCrates();

	public abstract void unspawnCrates();

	public abstract void playAnimation();
}
package eu.horyzon.cratesexplorer.objects.cratestype;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import eu.horyzon.cratesexplorer.listeners.PlayerModify;
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
	protected Map<UUID, Long> repeat = new HashMap<UUID, Long>();
	protected Sound sound;
	protected boolean run = false;

	public static Set<Crates> cratesList = new HashSet<Crates>();

	public void start() throws IllegalStateException, IllegalArgumentException {
		if (run)
			throw new IllegalStateException("Task already started");
		else if (stay())
			throw new IllegalArgumentException("Can't start task to immobile crate");

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

	public Boolean canReUse() {
		return useTime != 0;
	}

	public Boolean respawn() {
		return spawnTime < 0;
	}

	public Boolean stay() {
		return spawnTime == 0;
	}

	public void respawnCrates() {
		unspawnCrates();
		spawnRandomCrates();
		last = System.currentTimeMillis();
	}

	protected Reward getReward() throws NullPointerException {
		int total = 0;

		for (Reward reward : rewards) {
			total += reward.getPourcent();
		}

		double random = Math.random() * total;
		int index = 0;

		for (Reward reward : rewards) {
			index += reward.getPourcent();

			if (random < index)
				return reward;
		}

		throw new NullPointerException();
	}

	public boolean isModifyMode() {
		return PlayerModify.modify.containsValue(id);
	}

	public void spawnRandomCrates() {
		spawnCrates((int) Math.round(pourcent * crates.size()));
	}

	public void spawnAllCrates() {
		spawnCrates(crates.size());
	}

	public void spawnCrates(int amount) {
		Set<Object> randomCrates = new HashSet<Object>();
		Set<Object> copyCrates = new HashSet<Object>(crates);
		Random r = new Random();

		while (randomCrates.size() < amount && copyCrates.size() > 0) {
			Object crate = copyCrates.toArray()[r.nextInt(copyCrates.size())];

			copyCrates.remove(crate);
			randomCrates.add(crate);
		}

		spawnCrates(randomCrates);
	}

	public abstract boolean addCrate(Object crate);

	public abstract boolean removeCrate(Object crate);

	public abstract boolean isSpawned(Object crate);

	public abstract boolean isCrate(Object crate);

	public abstract void spawnCrates(Set<Object> crate);

	public abstract void unspawnCrates();

	public abstract void playAnimation(Object crate, Player p);
}
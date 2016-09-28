package eu.horyzon.cratesexplorer.objects.cratestype;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.BlockState;

import eu.horyzon.cratesexplorer.objects.rewardstype.Reward;
import eu.horyzon.cratesexplorer.tasks.animations.AnimationPlaySpawn;

public abstract class RunnableCrate extends Crate {
	protected int cooldownCrate;
	protected double pourcent;
	protected long last = 0;
	protected boolean run = false;
	protected Effect effect;

	public RunnableCrate(String id, Material type, Effect effect, int cooldownPlayer, int cooldownCrate, double pourcent, Set<Object> crates, Set<Reward> rewards) {
		super.id = id;
		super.type = type;
		this.effect = effect;
		this.cooldownPlayer = cooldownPlayer;
		this.cooldownCrate = cooldownCrate;
		this.pourcent = pourcent;
		super.crates = crates;
		super.rewards = rewards;

		if (cooldownCrate > 0)
			start();
		else
			spawnAllCrates();

		if (cooldownPlayer == -1)
			state = State.ILLIMITY;
		else if (cooldownPlayer == 0)
			state = State.ONETIME;
		else
			state = State.COOLDOWN;

		crates.add(this);
	}

	public State getState() {
		return state;
	}

	public boolean isValid(long now) {
		return run && (now - last) / 1000 >= cooldownCrate;
	}

	public boolean isPermanent() {
		return cooldownCrate == 0;
	}

	public boolean hasEffect() {
		return effect != null;
	}

	public Effect getEffect() {
		return effect;
	}

	public void start() {
		if (run)
			return;

		run = true;
		spawnRandomCrates();
	}

	public void stop() {
		if (!run)
			return;

		run = false;
		unspawnCrates();
	}

	public void respawn() {
		stop();
		start();
	}

	public void spawnRandomCrates() {
		spawnCrates((int) Math.round(pourcent * crates.size()));
	}

	public void spawnAllCrates() {
		spawnCrates(crates.size());
	}

	public void spawnCrates(int amount) {
		Set<BlockState> randomCrates = new HashSet<BlockState>();
		Set<Object> copyCrates = new HashSet<Object>(crates);
		Random r = new Random();

		while (randomCrates.size() < amount && copyCrates.size() > 0) {
			Object crate = copyCrates.toArray()[r.nextInt(copyCrates.size())];

			copyCrates.remove(crate);
			randomCrates.add((BlockState) crate);
		}

		spawnCrates(randomCrates);
	}

	public void respawnCrates() {
		unspawnCrates();
		spawnRandomCrates();
		last = System.currentTimeMillis();
	}

	protected void spawnCrates(Set<BlockState> crate) {
		new AnimationPlaySpawn(this, crate);
	}

	public abstract void unspawnCrates();

	public abstract void unspawnCrate(BlockState crate);
}
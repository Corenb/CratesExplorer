package eu.horyzon.cratesexplorer.objects.cratestype;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Sound;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import eu.horyzon.cratesexplorer.listeners.PlayerModify;
import eu.horyzon.cratesexplorer.objects.rewardstype.Reward;
import eu.horyzon.cratesexplorer.utils.FileUtils;

public abstract class Crate {
	protected String id;
	protected Object type;
	protected int cooldownPlayer;
	protected Sound sound;
	protected Set<Reward> rewards;
	protected Set<Object> crates;
	protected State state;

	public static Set<Crate> allCrates = new HashSet<Crate>();

	public static enum State {
		COOLDOWN, ONETIME, ILLIMITY;
	}

	public static Set<RunnableCrate> getRunnableCrates() {
		Set<RunnableCrate> crates = new HashSet<RunnableCrate>();
		for (Crate crate : allCrates)
			if (crate instanceof RunnableCrate)
				crates.add((RunnableCrate) crate);

		return crates;
	}

	public static Crate getCratesfromId(String id) throws NullPointerException {
		for (Crate crate : allCrates) {
			if (crate.id.equals(id))
				return crate;
		}

		throw new NullPointerException("Can't find crate");
	}

	public static Crate getCratesfromCrate(Object crate) throws NullPointerException {
		for (Crate crateType : allCrates) {
			if (crateType.isCrate(crate))
				return crateType;
		}

		throw new NullPointerException();
	}

	public boolean isModify() {
		return PlayerModify.modify.containsValue(id);
	}

	public boolean isCrate(Object crate) {
		return crates.contains(crate);
	}

	public boolean hasSound() {
		return sound != null;
	}

	public Set<Object> getCrates() {
		return crates;
	}

	public String getId() {
		return id;
	}

	public Object getType() {
		return type;
	}

	public State getState() {
		return state;
	}

	public Sound getSound() {
		return sound;
	}

	public void open(Object crate, Player player) {
		Reward reward = getReward();
		if (hasSound())
			player.playSound(player.getLocation(), sound, 20, 20);
		reward.giveReward(player);

		playAnimation(crate, player, reward);
	}

	protected boolean addCrate(Object crate, File dir) {
		if (!crates.contains(crate)) {
			crates.add(crate);
			FileUtils.addCrate(new File(dir, id), parseCrate(((BlockState) crate)));
			return true;
		}

		return false;
	}

	protected boolean removeCrate(Object crate, File dir) {
		if (crates.remove(crate)) {
			FileUtils.removeCrate(new File(dir, id), parseCrate(crate));
			return true;
		}

		return false;
	}

	protected Reward getReward() {
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

		return null;
	}

	protected abstract String parseCrate(Object crate);

	public abstract boolean addCrate(Object crate);

	public abstract boolean removeCrate(Object crate);

	protected abstract void playAnimation(Object crate, Player p, Reward reward);
}
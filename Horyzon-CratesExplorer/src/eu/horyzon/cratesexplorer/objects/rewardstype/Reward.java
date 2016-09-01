package eu.horyzon.cratesexplorer.objects.rewardstype;

import org.bukkit.entity.Player;

public abstract class Reward {
	protected int multiplicator;
	protected double amount;

	public int getMultiplicator() {
		return multiplicator;
	}

	public double getAmount() {
		return amount;
	}

	public abstract void giveReward(Player p);
}
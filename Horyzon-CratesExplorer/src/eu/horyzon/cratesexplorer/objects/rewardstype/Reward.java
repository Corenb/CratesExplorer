package eu.horyzon.cratesexplorer.objects.rewardstype;

import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import eu.horyzon.cratesexplorer.CratesExplorer;

public abstract class Reward {
	protected int pourcent;
	protected double amount;
	protected FireworkEffect firework;

	public int getPourcent() {
		return pourcent;
	}

	public double getAmount() {
		return amount;
	}

	public boolean hasFirework() {
		return firework != null;
	}

	public FireworkEffect getFirework() {
		return firework;
	}

	protected void spawnFirework(Location loc) {
		Firework fw = (Firework) loc.getWorld().spawn(loc, Firework.class);
		FireworkMeta fwm = fw.getFireworkMeta();
		fwm.addEffect(firework);
		fwm.setPower(0);
		fw.setFireworkMeta(fwm);

		CratesExplorer.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(CratesExplorer.getInstance(), new Runnable() {
			@Override
			public void run() {
				fw.detonate();
			}
		}, 0);
	}

	public abstract void giveReward(Player p);

	public abstract void playEffect(Location loc);
}
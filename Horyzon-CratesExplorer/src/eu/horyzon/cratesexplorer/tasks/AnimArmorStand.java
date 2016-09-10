package eu.horyzon.cratesexplorer.tasks;

import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;

public class AnimArmorStand extends BukkitRunnable {
	int i = 0;
	ArmorStand as;

	public AnimArmorStand(ArmorStand as) {
		this.as = as;
	}

	@Override
	public void run() {
		if (i < 40 && as.isValid()) {
			as.teleport(as.getLocation().add(0, 0.025, 0));
			i++;
		} else
			cancel();
	}
}

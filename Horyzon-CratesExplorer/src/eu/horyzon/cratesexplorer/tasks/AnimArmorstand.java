package eu.horyzon.cratesexplorer.tasks;

import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;

public class AnimArmorstand extends BukkitRunnable {
	int i = 0;
	ArmorStand as;

	public AnimArmorstand(ArmorStand as) {
		this.as = as;
	}

	@Override
	public void run() {
		if (i < 30 && as.isValid()) {
			as.teleport(as.getLocation().add(0, 0.03, 0));
		} else if (as.isValid())
			as.remove();
		else if (i > 120)
			cancel();
		i++;
	}
}

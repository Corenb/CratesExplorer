package eu.horyzon.cratesexplorer.tasks.animations;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import eu.horyzon.cratesexplorer.objects.cratestype.Crate;
import eu.horyzon.cratesexplorer.objects.rewardstype.Reward;

public class AnimationPlayEntityOpener extends AnimationPlayOpen {
	protected Entity entity;

	public AnimationPlayEntityOpener(Crate crateType, Entity entity, Player player, Reward reward) {
		super(crateType, entity.getLocation().clone().add(0, -0.4, 0), player, reward);
		this.entity = entity;
	}

	@Override
	public void run() {

		if (i > 35) {
			if (armorstand.isValid() || (armorstand = getArmorStand()).isValid())
				armorstand.remove();

			if (reward.hasFirework())
				reward.spawnFirework(location.clone().add(0, 2, 0));

			cancel();
		} else if (armorstand.isValid())
			armorstand.getLocation().add(0, 0.02, 0);

		i++;
	}
}
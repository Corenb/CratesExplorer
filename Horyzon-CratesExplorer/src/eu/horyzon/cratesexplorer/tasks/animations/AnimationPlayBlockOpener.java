package eu.horyzon.cratesexplorer.tasks.animations;

import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import eu.horyzon.cratesexplorer.CratesExplorer;
import eu.horyzon.cratesexplorer.listeners.PlayerExplore;
import eu.horyzon.cratesexplorer.objects.cratestype.ContainerCrate;
import eu.horyzon.cratesexplorer.objects.cratestype.RunnableCrate;
import eu.horyzon.cratesexplorer.objects.rewardstype.Reward;
import eu.horyzon.cratesexplorer.utils.ChestUtils;

public class AnimationPlayBlockOpener extends AnimationPlayOpen {
	protected BlockState block;

	public AnimationPlayBlockOpener(RunnableCrate crateType, BlockState block, Player player, Reward reward) {
		super(crateType, block.getLocation().clone().add(-0.5, 0.5, -0.5), player, reward);
		this.block = block;
	}

	@Override
	public void run() {
		if (i > 35) {
			if (armorstand.isValid() || (armorstand = getArmorStand()).isValid())
				armorstand.remove();

			if (reward.hasFirework())
				reward.spawnFirework(location.clone().add(0, 2, 0));

			if (!((RunnableCrate) crateType).isPermanent())
				new BukkitRunnable() {
					@Override
					public void run() {
						((RunnableCrate) crateType).unspawnCrate(block);

						PlayerExplore.removeUse(block);
					}
				}.runTask(CratesExplorer.getInstance());
			else if (crateType instanceof ContainerCrate)
				ChestUtils.setChest(block.getBlock(), false, 30);

			cancel();
		} else if (armorstand.isValid())
			armorstand.getLocation().add(0, 0.02, 0);

		i++;
	}
}
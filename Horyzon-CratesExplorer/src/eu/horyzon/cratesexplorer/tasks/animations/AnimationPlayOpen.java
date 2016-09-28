package eu.horyzon.cratesexplorer.tasks.animations;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import eu.horyzon.cratesexplorer.CratesExplorer;
import eu.horyzon.cratesexplorer.objects.cratestype.Crate;
import eu.horyzon.cratesexplorer.objects.rewardstype.Reward;
import net.md_5.bungee.api.ChatColor;

public abstract class AnimationPlayOpen extends BukkitRunnable {
	protected Crate crateType;
	protected Location location;
	protected Player player;
	protected Reward reward;
	protected ArmorStand armorstand;
	protected int i = 0;

	public AnimationPlayOpen(Crate crateType, Location location, Player player, Reward reward) {
		this.crateType = crateType;
		this.location = location;
		this.player = player;
		this.reward = reward;
		armorstand = createArmorStand(location);

		runTaskTimerAsynchronously(CratesExplorer.getInstance(), 0, 1);
	}

	protected ArmorStand createArmorStand(Location location) {
		ArmorStand armorstand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

		armorstand.setCustomName(ChatColor.GOLD + "+ " + reward.getAmount());
		armorstand.setCustomNameVisible(true);
		armorstand.setVisible(false);
		armorstand.setGravity(false);

		return armorstand;
	}

	protected ArmorStand getArmorStand() {
		for (Entity e : armorstand.getWorld().getEntitiesByClass(ArmorStand.class)) {
			if (e.getUniqueId().equals(armorstand.getUniqueId()))
				return ((ArmorStand) e);
		}

		return null;
	}
}
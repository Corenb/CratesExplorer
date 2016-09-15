package eu.horyzon.cratesexplorer.tasks;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import eu.horyzon.cratesexplorer.CratesExplorer;
import eu.horyzon.cratesexplorer.objects.cratestype.Crates;
import eu.horyzon.cratesexplorer.objects.rewardstype.Reward;
import net.md_5.bungee.api.ChatColor;

public class AnimationPlay extends BukkitRunnable {
	protected int i = 0;
	protected ArmorStand armorstand;
	protected UUID id;
	protected Crates crate;
	protected Reward reward;
	protected Location loc;
	protected int startArmorstand, stopArmorstand, startFirework;

	public AnimationPlay(Crates crate, Reward reward, Location loc, int startArmorstand, int stopArmorstand, int startFirework) {
		this.crate = crate;
		this.reward = reward;
		this.loc = loc;
		this.startArmorstand = startArmorstand;
		this.stopArmorstand = stopArmorstand;
		this.startFirework = startFirework;
		this.armorstand = createArmorstand();
		this.id = armorstand.getUniqueId();

		runTaskTimer(CratesExplorer.getInstance(), 0, 1);
	}

	@Override
	public void run() {
		ArmorStand as = getArmorStand();

		if(as == null || !as.isValid()){
		} else if (i >= stopArmorstand && as != null && as.isValid())
			as.remove();
		else if (as != null && as.isValid())
			as.teleport(armorstand.getLocation().add(0, 0.02, 0));

		if (i >= startFirework)
			reward.spawnFirework(loc);

		if (i > startFirework && i > stopArmorstand)
			cancel();

		i++;
	}

	protected ArmorStand createArmorstand() {
		ArmorStand armorstand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);

		armorstand.setCustomName(ChatColor.GOLD + "+ " + reward.getAmount());
		armorstand.setCustomNameVisible(true);
		armorstand.setVisible(false);
		armorstand.setGravity(false);

		return armorstand;
	}

	protected ArmorStand getArmorStand() {
		return armorstand != null && armorstand.isValid() ? armorstand : null;
	}
}
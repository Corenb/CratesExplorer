package eu.horyzon.cratesexplorer.objects;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import eu.horyzon.cratesexplorer.utils.ItemStackUtils;
import eu.horyzon.cratesexplorer.utils.LocationUtils;

public class ArmorStandObject {
	protected UUID armorstand;
	protected Location location;
	protected boolean arms, basePlat, small, visible, nameVisible, gravity, invulnerable;
	protected ItemStack boots, chestPlate, helmet, leggins, hand;
	protected EulerAngle body, head, rightArm, leftArm, rightLeg, leftLeg;
	protected String name;

	public ArmorStandObject(String[] split) {
		location = LocationUtils.parseLocationWithYaw(split);
		arms = Boolean.parseBoolean(split[6]);
		basePlat = Boolean.parseBoolean(split[7]);
		small = Boolean.parseBoolean(split[8]);
		visible = Boolean.parseBoolean(split[9]);
		nameVisible = Boolean.parseBoolean(split[10]);
		gravity = Boolean.parseBoolean(split[11]);
		invulnerable = Boolean.parseBoolean(split[12]);
		name = split[13];
		boots = ItemStackUtils.serialize(split[14]);
		chestPlate = ItemStackUtils.serialize(split[15]);
		helmet = ItemStackUtils.serialize(split[16]);
		leggins = ItemStackUtils.serialize(split[17]);
		hand = ItemStackUtils.serialize(split[18]);
		body = LocationUtils.parseAngle(split[19].split("%"));
		head = LocationUtils.parseAngle(split[20].split("%"));
		rightArm = LocationUtils.parseAngle(split[21].split("%"));
		leftArm = LocationUtils.parseAngle(split[22].split("%"));
		rightLeg = LocationUtils.parseAngle(split[23].split("%"));
		leftLeg = LocationUtils.parseAngle(split[24].split("%"));
	}

	public ArmorStandObject(ArmorStand as) {
		location = as.getLocation();
		arms = as.hasArms();
		basePlat = as.hasBasePlate();
		small = as.isSmall();
		visible = as.isVisible();
		nameVisible = as.isCustomNameVisible();
		gravity = as.hasGravity();
		invulnerable = as.isInvulnerable();
		name = as.getCustomName();
		boots = as.getBoots();
		chestPlate = as.getChestplate();
		helmet = as.getHelmet();
		leggins = as.getLeggings();
		hand = as.getItemInHand();
		body = as.getBodyPose();
		head = as.getHeadPose();
		rightArm = as.getRightArmPose();
		leftArm = as.getLeftArmPose();
		rightLeg = as.getRightLegPose();
		leftLeg = as.getLeftLegPose();

		armorstand = as.getUniqueId();
	}

	public void spawn() {
		ArmorStand armorstand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

		armorstand.setArms(arms);
		armorstand.setBasePlate(basePlat);
		armorstand.setSmall(small);
		armorstand.setVisible(visible);
		armorstand.setCustomNameVisible(nameVisible);
		armorstand.setGravity(gravity);
		armorstand.setInvulnerable(invulnerable);
		armorstand.setCustomName(name);
		armorstand.setBoots(boots);
		armorstand.setChestplate(chestPlate);
		armorstand.setHelmet(helmet);
		armorstand.setLeggings(leggins);
		armorstand.setItemInHand(hand);
		armorstand.setBodyPose(body);
		armorstand.setHeadPose(head);
		armorstand.setRightArmPose(rightArm);
		armorstand.setLeftArmPose(leftArm);
		armorstand.setRightLegPose(rightLeg);
		armorstand.setLeftLegPose(leftLeg);

		this.armorstand = armorstand.getUniqueId();
	}

	public ArmorStand getArmorStand() throws NullPointerException {
		for (Entity e : location.getWorld().getEntitiesByClass(ArmorStand.class)) {
			if (e.getUniqueId().equals(armorstand))
				return (ArmorStand) e;
		}

		throw new NullPointerException();
	}

	public boolean remove() {
		try {
			getArmorStand().remove();
			return true;
		} catch (NullPointerException e) {
			return false;
		}
	}

	public boolean isValid() {
		try {
			return getArmorStand().isValid();
		} catch (NullPointerException e) {
			return false;
		}
	}

	public boolean isArmorstand(ArmorStand as) {
		return armorstand.equals(as.getUniqueId());
	}

	public Location getLocation() {
		return location;
	}

	public String deserialize() {
		String loc = LocationUtils.parseLocationWithYaw(location);
		String boots = ItemStackUtils.deserialize(this.boots);
		String chestPlate = ItemStackUtils.deserialize(this.chestPlate);
		String helmet = ItemStackUtils.deserialize(this.helmet);
		String leggins = ItemStackUtils.deserialize(this.leggins);
		String hand = ItemStackUtils.deserialize(this.hand);
		String body = LocationUtils.parseAngle(this.body);
		String head = LocationUtils.parseAngle(this.head);
		String rightArm = LocationUtils.parseAngle(this.rightArm);
		String leftArm = LocationUtils.parseAngle(this.leftArm);
		String rightLeg = LocationUtils.parseAngle(this.rightLeg);
		String leftLeg = LocationUtils.parseAngle(this.leftLeg);

		return String.join(":", loc, Boolean.toString(arms), Boolean.toString(basePlat), Boolean.toString(small),
				Boolean.toString(visible), Boolean.toString(nameVisible), Boolean.toString(gravity),
				Boolean.toString(invulnerable), name, boots, chestPlate, helmet, leggins, hand, body, head, rightArm,
				leftArm, rightLeg, leftLeg);
	}
}
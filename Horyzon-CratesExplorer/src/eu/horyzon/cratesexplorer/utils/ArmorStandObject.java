package eu.horyzon.cratesexplorer.utils;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public class ArmorStandObject {
	protected ArmorStand armorstand;
	protected Location location;
	protected boolean arms, basePlat, small, visible, nameVisible, gravity, invulnerable;
	protected ItemStack boots, chestPlate, helmet, leggins, hand;
	protected EulerAngle body, head, rightArm, leftArm, rightLeg, leftLeg;
	protected String name;

	public ArmorStandObject(String[] split) {
		location = ParseUtils.parseLocation(split);
		arms = Boolean.parseBoolean(split[4]);
		basePlat = Boolean.parseBoolean(split[5]);
		small = Boolean.parseBoolean(split[6]);
		visible = Boolean.parseBoolean(split[7]);
		nameVisible = Boolean.parseBoolean(split[8]);
		gravity = Boolean.parseBoolean(split[9]);
		invulnerable = Boolean.parseBoolean(split[10]);
		name = split[11];
		boots = ItemStackUtils.deserial(split[12]);
		chestPlate = ItemStackUtils.deserial(split[13]);
		helmet = ItemStackUtils.deserial(split[14]);
		leggins = ItemStackUtils.deserial(split[15]);
		hand = ItemStackUtils.deserial(split[16]);
		body = ParseUtils.parseAngle(split[17].split("%"));
		head = ParseUtils.parseAngle(split[18].split("%"));
		rightArm = ParseUtils.parseAngle(split[19].split("%"));
		leftArm = ParseUtils.parseAngle(split[20].split("%"));
		rightLeg = ParseUtils.parseAngle(split[21].split("%"));
		leftLeg = ParseUtils.parseAngle(split[22].split("%"));
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

		armorstand = as;
	}

	public void spawn() {
		armorstand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

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
	}

	public void remove() {
		if (isValid())
			armorstand.remove();
	}

	public boolean isValid() {
		return armorstand != null && armorstand.isValid();
	}

	public boolean isArmorstand(ArmorStand as) {
		return armorstand == as;
	}

	public Location getLocation() {
		return location;
	}

	public String deserialize() {
		String loc = ParseUtils.parseLocation(location);
		String boots = ItemStackUtils.deserialize(this.boots);
		String chestPlate = ItemStackUtils.deserialize(this.chestPlate);
		String helmet = ItemStackUtils.deserialize(this.helmet);
		String leggins = ItemStackUtils.deserialize(this.leggins);
		String hand = ItemStackUtils.deserialize(this.hand);
		String body = ParseUtils.parseAngle(this.body);
		String head = ParseUtils.parseAngle(this.head);
		String rightArm = ParseUtils.parseAngle(this.rightArm);
		String leftArm = ParseUtils.parseAngle(this.leftArm);
		String rightLeg = ParseUtils.parseAngle(this.rightLeg);
		String leftLeg = ParseUtils.parseAngle(this.leftLeg);

		return String.join(":", loc, Boolean.toString(arms), Boolean.toString(basePlat), Boolean.toString(small),
				Boolean.toString(visible), Boolean.toString(nameVisible), Boolean.toString(gravity), Boolean.toString(invulnerable), name, boots,
				chestPlate, helmet, leggins, hand, body, head, rightArm, leftArm, rightLeg, leftLeg);
	}
}
package eu.horyzon.cratesexplorer.utils;

import org.bukkit.Color;

public enum FireworkColors {
	AQUA(Color.AQUA), BLACK(Color.BLACK), BLUE(Color.BLUE), FUCSHIA(Color.FUCHSIA), GRAY(Color.GRAY), GREEN(
			Color.GREEN), LIME(Color.LIME), MAROON(Color.MAROON), NAVY(Color.NAVY), OLIVE(Color.OLIVE), ORANGE(
					Color.ORANGE), PURPLE(Color.PURPLE), RED(Color.RED), SILVER(
							Color.SILVER), TEAL(Color.TEAL), WHITE(Color.WHITE), YELLOW(Color.YELLOW);

	private Color color;

	FireworkColors(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
}
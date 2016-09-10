package eu.horyzon.cratesexplorer.objects;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import eu.horyzon.cratesexplorer.objects.cratestype.Crates;

public class PlayerReward {
	Player player;
	Crates crate;
	Block block;

	public PlayerReward(Player player, Crates crate, Block block){
		this.player = player;
		this.crate = crate;
		this.block = block;
	}

}
package eu.horyzon.cratesexplorer.objects.rewardstype;

import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import eu.horyzon.currencydispenser.CurrencyManager;

public class CurrencyReward extends Reward {
	public CurrencyManager currency;

	public CurrencyReward(CurrencyManager currency, double amount, int pourcent, FireworkEffect firework) {
		this.currency = currency;
		super.amount = amount;
		super.pourcent = pourcent;
		super.firework = firework;
	}

	@Override
	public void giveReward(Player p) {
		currency.addAccount(p.getUniqueId(), amount);
	}

	@Override
	public void playEffect(Location loc) {
		if(hasFirework())
			spawnFirework(loc.clone().add(0.5, 1.0, 0.5));
	}
}
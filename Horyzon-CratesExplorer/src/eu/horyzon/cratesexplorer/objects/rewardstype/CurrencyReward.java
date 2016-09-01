package eu.horyzon.cratesexplorer.objects.rewardstype;

import org.bukkit.entity.Player;

import eu.horyzon.currencydispenser.CurrencyManager;

public class CurrencyReward extends Reward {
	public CurrencyManager currency;

	public CurrencyReward(CurrencyManager currency, double amount, int multiplicator) {
		this.currency = currency;
		super.amount = amount;
		super.multiplicator = multiplicator;
	}

	@Override
	public void giveReward(Player p) {
		currency.addAccount(p.getUniqueId(), amount);
	}
}
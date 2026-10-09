package mymod.relics.palomitero.palomitas.menores;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;

public class PalomitaHuecaPower extends AbstractPower {
	public static final String POWER_ID = "KioMod:PalomitaHuecaPower";

	public PalomitaHuecaPower(AbstractCreature owner) {
		this.name = "ⴰⴰⴰ";
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = -1;
		this.type = PowerType.BUFF;
		this.loadRegion("blur");
	}

	// atStartOfTurn() al inicio del siguiente turno antes de robar
	// atStartOfTurnPostDraw al inicio del siguiente turno
	// onInitialApplication() al lanzarse
	@Override
	public void atStartOfTurnPostDraw() {
		this.flash();
		AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
		AbstractDungeon.actionManager.addToBottom(new DrawCardAction(this.owner, 1));

	}

	@Override
	public void updateDescription() {
		this.description = ""; // Texto vacío o glifos
	}

}

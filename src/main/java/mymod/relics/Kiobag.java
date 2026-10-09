package mymod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import mymod.MyMod;

public class Kiobag extends CustomRelic {
	public static final String ID = MyMod.makeID("Kiobag");

	private static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
	public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
	
	private static final int STRENGTH_GAIN = 5;
	private static final RelicTier TIER = RelicTier.STARTER;
	private static final LandingSound SOUND = LandingSound.MAGICAL;

	public Kiobag() {
		super(ID, new Texture("images/kio/relics/Kiobag128.png"), TIER, SOUND);
	}

	@Override
	public String getUpdatedDescription() {

		if (DESCRIPTIONS == null || DESCRIPTIONS.length < 2) {
			return "Al inicio de cada combate, gana " + STRENGTH_GAIN + " de Fuerza.";
		}
		return DESCRIPTIONS[0] + STRENGTH_GAIN + DESCRIPTIONS[1];
	}

	@Override
	public void atBattleStart() {
		AbstractPlayer p = AbstractDungeon.player;
		this.flash(); // hace que brille visualmente al activarse
		addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, STRENGTH_GAIN), STRENGTH_GAIN));
	}

	@Override
	public AbstractRelic makeCopy() {
		return new Kiobag();
	}
}
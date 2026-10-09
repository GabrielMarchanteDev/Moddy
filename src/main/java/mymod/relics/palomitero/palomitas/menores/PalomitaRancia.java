package mymod.relics.palomitero.palomitas.menores;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import basemod.abstracts.CustomCard;
import mymod.cards.kio.KioCardColor;

public class PalomitaRancia extends CustomCard {
	public static final String ID = "PalomitaRancia";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String IMG_PATH = "images/cards/palomitas/Palomita2.png";
	private static final int COST = 0;
	private static final int WEAK = 2;

	public PalomitaRancia() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.SKILL, KioCardColor.KIO_COLOR, CardRarity.BASIC,
				CardTarget.ENEMY);
		this.exhaust = true;
		this.isEthereal = true;

		this.baseMagicNumber = WEAK;
		this.magicNumber = this.baseMagicNumber;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager
				.addToBottom(new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber));

	}

	@Override
	public AbstractCard makeCopy() {
		return new PalomitaRancia();
	}

	@Override
	public void upgrade() {
	    if (!upgraded) {
	        upgradeName();
	        upgradeMagicNumber(1); // Ahora será 3 débil
	        initializeDescription(); // Actualiza el texto con el nuevo !M!
	    }
	}

}

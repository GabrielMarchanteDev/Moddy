package mymod.relics.palomitero.palomitas.menores;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedPower;

import basemod.abstracts.CustomCard;
import mymod.cards.kio.KioCardColor;

public class PalomitaDulce extends CustomCard {
    public static final String ID = "PalomitaDulce";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "images/cards/palomitas/Palomita3.png";
    private static final int COST = 0;

	private static final int ENERGY = 2;
    public PalomitaDulce() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.SKILL, KioCardColor.KIO_COLOR, CardRarity.BASIC, CardTarget.SELF);
        this.exhaust = true;
        this.isEthereal = true;
        this.baseMagicNumber = ENERGY;
		this.magicNumber = this.baseMagicNumber;

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	AbstractDungeon.actionManager.addToBottom(
    		    new ApplyPowerAction(p, p, new EnergizedPower(p, magicNumber), magicNumber)
    		);
    }

    @Override
    public AbstractCard makeCopy() {
        return new PalomitaDulce();
    }

    @Override
    public void upgrade() {
    	 if (!upgraded) {
 	        upgradeName();
 	        upgradeMagicNumber(1); // Ahora será 2 energía
 	        initializeDescription(); // Actualiza el texto con el nuevo !M!
 	    }
 	}

}

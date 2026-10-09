package mymod.relics.palomitero.palomitas.mayores;


import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import basemod.abstracts.CustomCard;
import mymod.cards.kio.KioCardColor;

public class PalomitaMayorEnergetica extends CustomCard {
    public static final String ID = "PalomitaMayorEnergetica";
  	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "images/cards/palomitas/Palomita4.png";

    private static final int COST = 0;
    private static final int STR = 2;

    public PalomitaMayorEnergetica() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
              CardType.SKILL, KioCardColor.KIO_COLOR,
              CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = STR;
        this.magicNumber = baseMagicNumber;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber)
        );
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(p, p, new LoseStrengthPower(p, this.magicNumber), this.magicNumber)
        );
    }

    @Override
    public void upgrade() {if (!upgraded) {
	        upgradeName();
	        upgradeMagicNumber(1); // Ahora será 2 energía
	        initializeDescription(); // Actualiza el texto con el nuevo !M!
	    }}

    @Override
    public AbstractCard makeCopy() {
        return new PalomitaMayorEnergetica();
    }
}
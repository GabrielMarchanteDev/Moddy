package mymod.relics.palomitero.palomitas.mayores;


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

public class PalomitaMayorDulce extends CustomCard {
    public static final String ID = "PalomitaMayorDulce";
  	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "images/cards/palomitas/Palomita4.png";

    private static final int COST = 1;
    private static final int ENERGY = 3;

    public PalomitaMayorDulce() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
              CardType.SKILL, KioCardColor.KIO_COLOR,
              CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = ENERGY;
        this.magicNumber = baseMagicNumber;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(p, p, new EnergizedPower(p, this.magicNumber), this.magicNumber)
        );
    }

    @Override
    public void upgrade() {}

    @Override
    public AbstractCard makeCopy() {
        return new PalomitaMayorDulce();
    }
}
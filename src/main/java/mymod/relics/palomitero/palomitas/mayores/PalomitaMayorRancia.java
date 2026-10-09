package mymod.relics.palomitero.palomitas.mayores;

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

public class PalomitaMayorRancia extends CustomCard {
    public static final String ID = "PalomitaMayorRancia";
  	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "images/cards/palomitas/Palomita4.png";
    
    // código como PalomitaRancia pero más potente

    public PalomitaMayorRancia() {
        super(ID,NAME, IMG_PATH,
            1, DESCRIPTION, CardType.SKILL, KioCardColor.KIO_COLOR,
            CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.baseMagicNumber = 4;
        this.magicNumber = baseMagicNumber;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(mo, p, new WeakPower(mo, this.magicNumber, false), this.magicNumber)
            );
        }
    }

    @Override
    public void upgrade() {}

    @Override
    public AbstractCard makeCopy() {
        return new PalomitaMayorRancia();
    }
}


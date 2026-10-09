package mymod.cards.kio;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;

public class ComerPalomitas extends CustomCard {
	 public static boolean isIn85Mode = false;
    public static final String ID = "ComerPalomitas";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "images/cards/palomitas/comerselas.png";

    private static final int COST = 1;
    private static final int BLOCK_PER_PALOMITA = 4;

    public ComerPalomitas() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
              CardType.SKILL, KioCardColor.KIO_COLOR,
              CardRarity.BASIC, CardTarget.SELF);
        this.baseMagicNumber = BLOCK_PER_PALOMITA;
        this.magicNumber = baseMagicNumber;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
        	if (c.cardID.startsWith("Palomita")) {
                AbstractDungeon.actionManager.addToBottom(
                    new GainBlockAction(p, p, this.magicNumber)
                );
                AbstractDungeon.actionManager.addToBottom(
                    new ExhaustSpecificCardAction(c, p.hand)
                );
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ComerPalomitas();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(2); // Ahora da 8 de bloqueo por palomita
            initializeDescription();
        }
    }
}

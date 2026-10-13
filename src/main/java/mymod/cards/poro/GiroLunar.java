package mymod.cards.poro;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mymod.actions.AdvanceLunarPhaseAction;

public class GiroLunar extends CustomCard {
    public static final String ID = "mymod:GiroLunar";
    private static final CardStrings TEXT = CardCrawlGame.languagePack.getCardStrings(ID);

    public GiroLunar() {
        super(ID, TEXT.NAME, "images/cards/giroLunar.png", 0, TEXT.DESCRIPTION,
                CardType.SKILL, PoroCardColor.PORO_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    @Override public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new AdvanceLunarPhaseAction());
    }

    @Override public void upgrade() {
        if (!upgraded) {
            upgradeName();
            isInnate = true;
            rawDescription = TEXT.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override public AbstractCard makeCopy() { return new GiroLunar(); }
}

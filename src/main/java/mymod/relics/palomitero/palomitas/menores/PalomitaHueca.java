package mymod.relics.palomitero.palomitas.menores;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.AbstractCard.CardTarget;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import mymod.cards.kio.KioCardColor;

public class PalomitaHueca extends CustomCard {
    public static final String ID = "PalomitaHueca";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "images/cards/palomitas/Palomita.png";
    private static final int COST = 0;

    public PalomitaHueca() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.SKILL, KioCardColor.KIO_COLOR, CardRarity.BASIC, CardTarget.NONE);
        this.exhaust = true;
        this.isEthereal = true;

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Aplica un power oculto que hace algo más tarde
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new PalomitaHuecaPower(p)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new PalomitaHueca();
    }

    @Override
    public void upgrade() {}
}

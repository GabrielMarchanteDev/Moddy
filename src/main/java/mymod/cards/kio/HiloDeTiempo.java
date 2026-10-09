package mymod.cards.kio;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;

public class HiloDeTiempo extends CustomCard {
    public static final String ID = "HiloDeTiempo"; 
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "images/cards/defend.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = KioCardColor.KIO_COLOR;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 3;
    private static final int BLOCK = 20;
    private static final int UPGRADE_BLOCK = 6;

    public HiloDeTiempo() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseBlock = BLOCK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        // No reseteamos el coste: se mantiene tras el uso
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        // Si la carta está en la mano al final del turno (y no se ha jugado)
        if (AbstractDungeon.player.hand.contains(this) && this.cost > 0) {
            this.cost -= 1;
            this.cost = Math.max(0, this.cost);
            this.costForTurn = this.cost;
            this.isCostModified = true;
            this.isCostModifiedForTurn = true;
        }
    }

    @Override
    public void atTurnStart() {
        // Asegura que el coste para este turno coincida con el actual coste base modificado
        this.costForTurn = this.cost;
        this.isCostModifiedForTurn = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new HiloDeTiempo();
    }
}
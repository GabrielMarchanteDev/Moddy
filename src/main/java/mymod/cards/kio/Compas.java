package mymod.cards.kio;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;

public class Compas extends CustomCard {
    public static final String ID = "Compas";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "images/cards/compas.png";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = KioCardColor.KIO_COLOR;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int BLOCK = 5;
    private static final int BONUS_BLOCK = 10;
    private static final int UPGRADE_BLOCK = 2;
    private static final int UPGRADE_BONUS_BLOCK = 2;
    private static boolean seJugoCompasEnTurnoAnterior = false;
    private static boolean seJugoCompasEsteTurno = false;


    public Compas() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseBlock = BLOCK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int totalBlock = this.block;

        if (seJugoCompasEnTurnoAnterior) {
            totalBlock += BONUS_BLOCK;
        }

        addToBot(new GainBlockAction(p, p, totalBlock));

        // Marca que fue jugado este turno
        seJugoCompasEsteTurno = true;
    }
    @Override
    public void atTurnStart() {
        // Lo que ocurrió en el turno anterior pasa a "historial"
        seJugoCompasEnTurnoAnterior = seJugoCompasEsteTurno;
        seJugoCompasEsteTurno = false;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK);
            this.upgradeBlock(UPGRADE_BONUS_BLOCK);// Mejora: +3 al bloque base
        }
    } 
    @Override
    public void triggerOnGlowCheck() {
    	this.glowColor = new Color(1f, 0.9f, 0.4f, 1f); // dorado más suave

    }


    @Override
    public AbstractCard makeCopy() {
        return new Compas();
    }
}

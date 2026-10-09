package mymod.cards.kio;

import java.util.ArrayList;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import mymod.relics.palomitero.palomitas.menores.PalomitaFactory;
import mymod.util.FlexibleDiscoveryAction;

public class RacionDoble extends CustomCard {
    public static boolean isIn85Mode = false;
    public static final String ID = "RacionDoble";
    public static final String NAME = "Ración Doble";
    public static final String IMG_PATH = "images/cards/racionDoble.png";
    public static final String DESCRIPTION = "Elige 1 de entre 3 palomitas menores para añadir a tu mano.";

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    private static final int COST = 1;

    public RacionDoble() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, KioCardColor.KIO_COLOR, RARITY, TARGET);
        this.exhaust = false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	
    	 for (int i = 0; i < 2; i++) {
    	        ArrayList<AbstractCard> opciones = PalomitaFactory.getRandomPalomitasUnique(3);
    	        AbstractDungeon.actionManager.addToBottom(new FlexibleDiscoveryAction(opciones, false));
    	    }

    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.exhaust = false;
            this.rawDescription = "Elige 1 de entre 3 palomitas menores para añadir a tu mano. No se agota.";
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new RacionDoble();
    }
}

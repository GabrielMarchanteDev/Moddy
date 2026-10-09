package mymod.cards.kio;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import mymod.MyMod;
import mymod.characters.Kio;

public class ContarDobles extends CustomCard {
    public static final String ID = MyMod.makeID("ContarDobles");
    public static final String NAME = "Contar Dobles";
    public static final String DESCRIPTION = "Gana 2 de Suerte por cada carta de coste 2 en tu mano (incluyéndose a sí misma).";
    public static final String IMG_PATH = "images/cards/defend.png";

    private static final int COST = 2;
    private static final int LUCK_PER_CARD = 2;

    public ContarDobles() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, KioCardColor.KIO_COLOR,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.exhaust = true; // que se agote para no spamear infinito
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = 0;

        // Recorremos todas las cartas de la mano
        for (AbstractCard c : p.hand.group) {
            if (c.cost == 2 || c.costForTurn == 2) {
                count++;
            }
        }

        int luckTotal = count * LUCK_PER_CARD;

        // Añadimos acción para aplicar la suerte al personaje
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                if (p instanceof Kio) {
                    ((Kio) p).addKioLuck(luckTotal);
                }
                this.isDone = true;
            }
        });

        // Espera pequeña para feedback visual
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1F));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.selfRetain = true; // mejora: se queda en mano hasta usarse
            this.rawDescription = "Retiene. NL " + DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public CustomCard makeCopy() {
        return new ContarDobles();
    }
}

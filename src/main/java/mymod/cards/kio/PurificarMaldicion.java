package mymod.cards.kio;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import mymod.MyMod;
import mymod.characters.Kio;

import java.util.ArrayList;

public class PurificarMaldicion extends CustomCard {
    public static final String ID = MyMod.makeID("PurificarMaldicion");
    public static final String NAME = "Purificar Maldición";
    public static final String DESCRIPTION = "Elige 1 Maldición de tu mano. Exhausta esa carta y gana 7 de Suerte. Si no tienes Maldiciones, no hace nada.";
    public static final String IMG_PATH = "images/cards/racionDoble.png";
    private static final int COST = 1;
    private static final int LUCK_GAIN = 7;

    public PurificarMaldicion() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, KioCardColor.KIO_COLOR,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.exhaust = true; // La carta siempre se agota
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Filtramos todas las Maldiciones en mano
        ArrayList<AbstractCard> cursesInHand = new ArrayList<>();
        for (AbstractCard c : p.hand.group) {
            if (c.type == CardType.CURSE) {
                cursesInHand.add(c);
            }
        }

        if (!cursesInHand.isEmpty()) {
            // Abrimos selector para elegir UNA Maldición
            AbstractDungeon.handCardSelectScreen.open("Elige una Maldición para purificar", 1, false, false, false, false);

            // Acción que maneja el resultado
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                        for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                            if (c.type == CardType.CURSE) {
                                // Exhaustar la Maldición elegida
                                AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(c, p.hand));

                                // Dar Suerte
                                if (p instanceof Kio) {
                                    ((Kio) p).addKioLuck(LUCK_GAIN);
                                }
                            }
                        }
                        AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                        AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
                    }
                    this.isDone = true;
                }
            });
        }
        // si no hay Maldiciones, no pasa nada: la carta igual se agota por defecto
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(0); // mejorar: coste 0
        }
    }

    @Override
    public CustomCard makeCopy() {
        return new PurificarMaldicion();
    }
}

package mymod.cards.kio;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import mymod.MyMod;
import mymod.characters.Kio;

import java.util.ArrayList;

public class VisionDoble extends CustomCard {
    public static final String ID = MyMod.makeID("VisionDoble");
    public static final String NAME = "Visión Doble";
    public static final String DESCRIPTION = "Roba 2 cartas. Si ambas cuestan 2, gana 7 de Suerte.";
    public static final String IMG_PATH = "images/cards/defend.png";

    private static final int COST = 1;
    private static final int CARDS_TO_DRAW = 2;
    private static final int LUCK_GAIN = 7;

    public VisionDoble() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, KioCardColor.KIO_COLOR,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.exhaust = true; // se agota para evitar loops fáciles
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
            new DrawCardAction(CARDS_TO_DRAW, new AbstractGameAction() {
                @Override
                public void update() {
                    ArrayList<AbstractCard> drawnCards = DrawCardAction.drawnCards;

                    if (drawnCards.size() >= 2) {
                        AbstractCard c1 = drawnCards.get(drawnCards.size() - 1);
                        AbstractCard c2 = drawnCards.get(drawnCards.size() - 2);

                        if ((c1.cost == 2 || c1.costForTurn == 2) &&
                            (c2.cost == 2 || c2.costForTurn == 2)) {
                            if (p instanceof Kio) {
                                ((Kio) p).addKioLuck(LUCK_GAIN);
                            }
                        }
                    }
                    this.isDone = true;
                }
            })
        );
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = "Roba 2 cartas. Si ambas cuestan 2, gana 10 de Suerte.";
            this.initializeDescription();
            // al mejorar sube la recompensa
        }
    }

    @Override
    public CustomCard makeCopy() {
        return new VisionDoble();
    }
}

package mymod.cards.kio;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import mymod.MyMod;
import mymod.characters.Kio;

public class Transformacion extends CustomCard {
    public static boolean isIn85Mode = false;
    public static final String ID = MyMod.makeID("Transformacion");
    public static final String NAME = "Transformación";
    public static final String DESCRIPTION = "Cambia tu forma a Kio Furia. (Cambia el modelo Spine)";
    public static final String IMG_PATH = "images/cards/racionDoble.png";// Añade tu propia imagen

    private static final int COST = 1;

    public Transformacion() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, KioCardColor.KIO_COLOR,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p instanceof Kio) {
            Kio kio = (Kio) p;

            kio.cambiarModeloSpine(
                "images/kio/char/spine/Proyecto1.atlas",
                "images/kio/char/spine/Proyecto1.json",
                2.0f,
                "animtion0", true
            );

            kio.state.addAnimation(0, "animtion0", true, 0f); // loop tras intro
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(0);
        }
    }
  

    @Override
    public CustomCard makeCopy() {
        return new Transformacion();
    }
}

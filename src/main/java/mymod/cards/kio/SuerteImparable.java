package mymod.cards.kio;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mymod.MyMod;
import mymod.characters.Kio;

public class SuerteImparable extends CustomCard {
    public static boolean isIn85Mode = false;
    public static final String ID = MyMod.makeID("SuerteImparable");
    public static final String NAME = "Suerte Imparable";
    public static final String DESCRIPTION = "Gana 10 de Suerte permanente.";
    public static final String IMG_PATH = "images/cards/racionDoble.png";// Añade tu propia imagen

    private static final int COST = 1;
    private static final int LUCK_GAIN = 10;

    public SuerteImparable() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.SKILL,
                KioCardColor.KIO_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p instanceof Kio) {
            ((Kio) p).addKioLuck(LUCK_GAIN);
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
    public AbstractCard makeCopy() {
        return new SuerteImparable();
    }
}

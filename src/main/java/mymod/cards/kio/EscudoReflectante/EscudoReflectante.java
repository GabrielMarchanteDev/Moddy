package mymod.cards.kio.EscudoReflectante;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import mymod.MyMod;
import mymod.cards.kio.KioCardColor;

public class EscudoReflectante extends CustomCard {
    public static final String ID = MyMod.makeID("EscudoReflectante");
    public static final String NAME = "Escudo Reflectante";
    public static final String DESCRIPTION = "Gana 8 de Bloque. El próximo turno, el 50% del daño absorbido por el Bloque se devuelve a un enemigo aleatorio.";
    public static final String IMG_PATH = "images/cards/palomitas/comerselas.png";

    private static final int COST = 2;
    private static final int BLOCK = 8;

    public EscudoReflectante() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, KioCardColor.KIO_COLOR,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseBlock = BLOCK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        addToBot(new ApplyPowerAction(p, p, new ReflejoMitigadoPower(p, 1), 1));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(3); // +3 al mejorar
        }
    }

    @Override
    public CustomCard makeCopy() {
        return new EscudoReflectante();
    }
}

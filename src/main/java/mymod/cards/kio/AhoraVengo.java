package mymod.cards.kio;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import mymod.characters.Kio;
import mymod.characters.ui.PickTopNextTurnFromDrawPileAction;
import mymod.powers.AhoraVengoThisTurnPower;

public class AhoraVengo extends CustomCard {
    public static final String ID = "AhoraVengo";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "images/cards/palomitas/comerselas.png";
    private static final int COST = 1;
    public AhoraVengo() {
        super(ID, NAME, IMG_PATH, COST,
        		DESCRIPTION,
              CardType.SKILL, KioCardColor.KIO_COLOR,
              CardRarity.RARE, CardTarget.SELF);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Animación: salir y quedar invisible
        if (p instanceof Kio) {
        	Kio kio = (Kio) p;
            kio.state.clearTracks();
            kio.state.setAnimation(0, "pocket", false);
            kio.state.addAnimation(0, "none", true, 0f);
        }

        // Inmunidad este turno
        addToBot(new ApplyPowerAction(p, p, new AhoraVengoThisTurnPower(p)));
        // Abrir selección del mazo de robo y programar la vuelta + carta primera
        addToBot(new PickTopNextTurnFromDrawPileAction());
    }

    @Override public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(0); // ej.: mejora coste a 0
        }
    }
}

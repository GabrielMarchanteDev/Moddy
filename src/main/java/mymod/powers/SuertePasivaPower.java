package mymod.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import mymod.MyMod;
import mymod.characters.Kio;

public class SuertePasivaPower extends AbstractPower {

    public static final String POWER_ID = MyMod.makeID("SuertePasiva");  
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private boolean haDobladoUnAtaqueEsteTurno = false;

    public SuertePasivaPower(AbstractCreature owner) {
        this.name = "Suerte Pasiva";
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        this.loadRegion("focus");
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (!(owner instanceof Kio)) return;
        Kio kio = (Kio) owner;

        float suerte = kio.getSuertePorcentaje();

        // Robar carta extra
        if (Math.random() < suerte) {
            addToBot(new DrawCardAction(owner, 1));
        }

        // Energía extra
        if (Math.random() < suerte) {
            addToBot(new GainEnergyAction(1));
        }

        haDobladoUnAtaqueEsteTurno = false; // se reinicia cada turno
    }

    // Guardamos referencia para aplicar el efecto cuando se inflige daño
    public boolean debeDobleDanio() {
        float suerte = ((Kio) owner).getSuertePorcentaje();
        return !haDobladoUnAtaqueEsteTurno && Math.random() < suerte;
    }

    public void marcarDobleDanioUsado() {
        haDobladoUnAtaqueEsteTurno = true;
    }
    
    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
    
    @Override
    public void onCardDraw(AbstractCard card) {
        // Aquí aplicas el efecto de suerte a la carta robada
        float suerte = ((Kio) owner).getSuertePorcentaje();
        if (Math.random() < suerte) {
            card.setCostForTurn(0);
        }
    }

}

package mymod.powers;

import java.util.UUID;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import mymod.characters.Kio;

public class AhoraVengoReturnNextTurnPower extends AbstractPower {
	
    public static final String POWER_ID = "AhoraVengoReturnNextTurn";
    private final UUID targetUuid = null;
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public AhoraVengoReturnNextTurnPower(AbstractCreature owner,UUID targetUuid) {
        this.owner = owner;
        this.name = NAME;
        this.ID = POWER_ID;
        this.isTurnBased = true;
        this.type = PowerType.BUFF;
        this.priority = 99; // para ejecutarse pronto
        
        Texture tex84 = new Texture("images/icon2.png");
        Texture tex32 = new Texture("images/icon.png");
        tex84.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        tex32.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 128, 128);
        this.region48  = new TextureAtlas.AtlasRegion(tex32, 0, 0, 48, 48);
        updateDescription();
        updateDescription();
    }
    private AbstractCard findInGroup(CardGroup g) {
        for (AbstractCard c : g.group) if (c.uuid.equals(targetUuid)) return c;
        return null;
    }
    
    @Override
    public void atStartOfTurn() {
        AbstractPlayer p = (AbstractPlayer) owner;

        // Si la carta está en el descarte y el draw está vacío, sácala al draw para evitar barajeo
        AbstractCard inDiscard = findInGroup(p.discardPile);
        if (inDiscard != null && p.drawPile.isEmpty()) {
            p.discardPile.moveToDeck(inDiscard, false); // false -> toTop
        }

        // Si está en el draw, súbela a la cima
        AbstractCard inDraw = findInGroup(p.drawPile);
        if (inDraw != null) {
            p.drawPile.removeCard(inDraw);
            p.drawPile.addToTop(inDraw);
        }
    }
    @Override
    public void atStartOfTurnPostDraw() {
        if (owner instanceof Kio) {
        	Kio kio = (Kio) owner;
            kio.state.clearTracks();
            kio.state.setAnimation(0, "pocketVuelta", false);
            kio.state.addAnimation(0, "IDLE", true, 0f);
        }
        addToTop(new RemoveSpecificPowerAction(owner, owner, this.ID));
    }

    @Override
    public void updateDescription() {
        this.description = "Volverás al inicio del siguiente turno.";
    }
}
	
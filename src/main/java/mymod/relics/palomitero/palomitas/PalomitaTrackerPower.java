package mymod.relics.palomitero.palomitas;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import mymod.MyMod;
import mymod.relics.palomitero.palomitas.mayores.PalomitaMayor;
import mymod.relics.palomitero.palomitas.mayores.PalomitaMayorFactory;

public class PalomitaTrackerPower extends AbstractPower {

    public static final String POWER_ID = MyMod.makeID("PalomitasTracker");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    private static final PowerType TYPE = PowerType.BUFF;
    private Map<String, Integer> palomitaContador = new HashMap<>();
    private int counter = 0;

    public PalomitaTrackerPower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.owner = owner;
        this.type = PowerType.BUFF;
       // this.img = ImageMaster.loadImage("images/icon.png");
        this.isTurnBased = false;
        Texture tex84 = new Texture("images/icon2.png");
        Texture tex32 = new Texture("images/icon.png");
        tex84.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        tex32.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 128, 128);
        this.region48  = new TextureAtlas.AtlasRegion(tex32, 0, 0, 48, 48);
        updateDescription();
    }
    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
 
    
    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card.cardID.startsWith("Palomita") && !card.cardID.contains("Mayor")) {
            counter++;
            palomitaContador.put(card.cardID,
                palomitaContador.getOrDefault(card.cardID, 0) + 1);

            if (counter >= 3) {
                counter = 0;
                this.flashWithoutSound();

                AbstractCard resultado;

                // Buscar si alguna palomita se ha jugado 3 veces
                String tipoFijo = palomitaContador.entrySet().stream()
                    .filter(e -> e.getValue() >= 3)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);

                palomitaContador.clear();

                if (tipoFijo != null) {
                    resultado = PalomitaMayorFactory.create(tipoFijo);
                } else {
                    resultado = new PalomitaMayor(); // versión aleatoria
                }

                AbstractDungeon.actionManager.addToBottom(
                    new MakeTempCardInHandAction(resultado)
                );
            }
        }
    }

}

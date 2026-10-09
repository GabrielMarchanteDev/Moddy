package mymod.relics.palomitero;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import mymod.relics.Kiobag;
import mymod.relics.palomitero.palomitas.PalomitaTrackerPower;
import mymod.relics.palomitero.palomitas.menores.PalomitaFactory;

public class PalomiteroDeKio extends CustomRelic {
    public static final String ID = "PalomiteroDeKio";

    private static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
  
    public PalomiteroDeKio() {
        super(ID, new Texture("images/kio/relics/palomitero/Palomitero128.png"), RelicTier.STARTER, LandingSound.MAGICAL);
  
    }

    @Override
    public void atBattleStart() {
    	 // Aplica el Power personalizado
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new PalomitaTrackerPower(AbstractDungeon.player))
        );
    }
    
    @Override
    public void atTurnStartPostDraw() {
        this.flash();

        // Añade 2 palomitas aleatorias a la mano
        AbstractDungeon.actionManager.addToBottom(
            new MakeTempCardInHandAction(PalomitaFactory.getRandomPalomita())
        );
        AbstractDungeon.actionManager.addToBottom(
            new MakeTempCardInHandAction(PalomitaFactory.getRandomPalomita())
        );
    }
    
	@Override
	public AbstractRelic makeCopy() {
		return new PalomiteroDeKio();
	}

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0]; // Ocúltale al jugador qué hace en realidad
    }
}

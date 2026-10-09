package mymod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class AhoraVengoThisTurnPower extends AbstractPower {
    public static final String POWER_ID = "AhoraVengoThisTurn";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public AhoraVengoThisTurnPower(AbstractCreature owner) {
        this.owner = owner;
        this.name = NAME;
        this.ID = POWER_ID;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        
        Texture tex84 = new Texture("images/icon2.png");
        Texture tex32 = new Texture("images/icon.png");
        tex84.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        tex32.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 128, 128);
        this.region48  = new TextureAtlas.AtlasRegion(tex32, 0, 0, 48, 48);
        updateDescription();
    }

    @Override public int onLoseHp(int damageAmount) { return 0; }
    @Override public int onAttacked(DamageInfo info, int damageAmount) { return 0; }
//    @Override
//    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
//        return 0;
//    }
    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        return 0f;
    }
    @Override
    public void atStartOfTurn() {
        addToTop(new RemoveSpecificPowerAction(owner, owner, this.ID));
    }
    

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
 
}

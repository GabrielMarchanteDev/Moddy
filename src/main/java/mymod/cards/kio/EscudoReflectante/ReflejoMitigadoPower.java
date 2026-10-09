package mymod.cards.kio.EscudoReflectante;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import mymod.MyMod;

public class ReflejoMitigadoPower extends AbstractPower {
    public static final String POWER_ID = MyMod.makeID("ReflejoMitigadoPower");
    private static final PowerStrings powerStrings = com.megacrit.cardcrawl.core.CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ReflejoMitigadoPower(AbstractCreature owner, int turns) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        this.amount = turns;

        // iconos
        Texture tex84 = new Texture("images/icon2.png");
        Texture tex32 = new Texture("images/icon.png");
        tex84.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        tex32.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 128, 128);
        this.region48  = new TextureAtlas.AtlasRegion(tex32, 0, 0, 48, 48);

        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount < info.output) {
            int blocked = info.output - damageAmount;
            int reflejo = Math.round(blocked * 0.5f);
            if (reflejo > 0) {
                AbstractDungeon.actionManager.addToTop(
                    new DamageRandomEnemyAction(
                        new DamageInfo(this.owner, reflejo, DamageInfo.DamageType.THORNS),
                        AbstractGameAction.AttackEffect.SLASH_DIAGONAL
                    )
                );
                this.flash();
            }
        }
        return damageAmount;
    }


    public void atStartOfTurn() {
        addToTop(new RemoveSpecificPowerAction(owner, owner, this.ID));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}

package mymod.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import basemod.abstracts.CustomMonster;
import mymod.MyMod;
public class KioEnemy extends CustomMonster {
	
  
    
    public static final String ID = MyMod.makeID(KioEnemy.class.getSimpleName());
    public static final String NAME = "KioEnemy";
    private static final int HP = 60;
    private static final byte ATTACK = 1;

    public KioEnemy(float x, float y) {
        super(NAME, ID, HP, 0.0F, -10.0F, 220.0F, 250.0F, null, x, y);

        // Cargar animación Spine como en el personaje
        this.loadAnimation(
            "images/kio/char/spine/NewProject_1.atlas",
            "images/kio/char/spine/NewProject_1.json",
            1.0f
        );
        AnimationState.TrackEntry e = this.state.setAnimation(0, "animtion0", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.flipHorizontal = true;

        // Configuración básica
        this.type = EnemyType.NORMAL;
        setHp(HP);
        damage.add(new DamageInfo(this, 7)); // 7 de daño
    }
    
    public KioEnemy() {
    	 super(NAME, ID, HP, 0.0F, -10.0F, 220.0F, 250.0F, null,0F,0F);
    }

    @Override
    public void takeTurn() {
        addToBot(new DamageAction(AbstractDungeon.player, damage.get(0)));
    }

    @Override
    protected void getMove(int roll) {
        setMove(ATTACK, Intent.ATTACK, damage.get(0).base);
    }
    
    @Override
    public void die() {
        super.die();
        // Finaliza la partida aquí
        AbstractDungeon.overlayMenu.endTurnButton.disable();
        AbstractDungeon.dungeonMapScreen.dismissable = false;
        AbstractDungeon.player.isDead = false;
        AbstractDungeon.isDungeonBeaten = true;
        CardCrawlGame.startOver();
    }
}

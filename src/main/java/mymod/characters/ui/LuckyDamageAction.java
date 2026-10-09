package mymod.characters.ui;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import mymod.characters.Kio;

public class LuckyDamageAction extends AbstractGameAction {

    private final DamageInfo info;
    private final int luckGain;
    private boolean hasDamaged = false;

    public LuckyDamageAction(AbstractMonster target, DamageInfo info, int luckGain) {
        this.target = target;
        this.info = info;
        this.luckGain = luckGain;
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = AttackEffect.SLASH_HORIZONTAL;
    }

    @Override
    public void update() {
        if (this.target == null || this.target.isDead || this.target.halfDead) {
            this.isDone = true;
            return;
        }

        if (!hasDamaged) {
            // Hace daño al objetivo
            this.target.damage(info);
            hasDamaged = true;

            // Si el objetivo ha muerto, da suerte
            if ((this.target.isDying || this.target.currentHealth <= 0) && info.owner instanceof Kio) {
                ((Kio) info.owner).addKioLuck(luckGain);
            }
        }

        this.isDone = true;
    }
}

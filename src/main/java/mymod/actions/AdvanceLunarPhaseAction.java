package mymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import mymod.characters.Poro;

public class AdvanceLunarPhaseAction extends AbstractGameAction {
    @Override public void update() {
        if (AbstractDungeon.player instanceof Poro) {
            ((Poro) AbstractDungeon.player).advanceLunarPhase();
            AbstractDungeon.player.hand.applyPowers();
        }
        isDone = true;
    }
}

package mymod.characters.ui;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import mymod.powers.AhoraVengoReturnNextTurnPower;

public class PickTopNextTurnFromDrawPileAction extends AbstractGameAction {
    public PickTopNextTurnFromDrawPileAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED) {
            if (AbstractDungeon.player.drawPile.isEmpty()) {
                this.isDone = true;
                return;
            }	
            // Abre el selector sobre el draw pile
            AbstractDungeon.gridSelectScreen.open(
                AbstractDungeon.player.drawPile,
                1,
                "Elige la carta que robarás primero el próximo turno",
                false, false, false, false
            );
            tickDuration();
            return;
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard chosen = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();

            // Sube esa carta a la CIMA ahora mismo (por si haces más cambios de orden)
            AbstractDungeon.player.drawPile.removeCard(chosen);
            AbstractDungeon.player.drawPile.addToTop(chosen);

            // Aplica el power que garantizará que sea la 1ª incluso si habría barajeo
            addToTop(new ApplyPowerAction(
                AbstractDungeon.player, AbstractDungeon.player,
                new AhoraVengoReturnNextTurnPower(AbstractDungeon.player, chosen.uuid)
            ));
            this.isDone = true;
            return;
        }
        tickDuration();
    }
}

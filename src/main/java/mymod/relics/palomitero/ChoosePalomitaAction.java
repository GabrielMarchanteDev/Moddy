package mymod.relics.palomitero;
//package mymod.cards;
//
//import java.util.ArrayList;
//
//import com.megacrit.cardcrawl.actions.AbstractGameAction;
//import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
//import com.megacrit.cardcrawl.cards.AbstractCard;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//
//import mymod.relics.palomitero.palomitas.menores.PalomitaFactory;
//public class ChoosePalomitaAction extends AbstractGameAction {
//    private boolean opened = false;
//
//    @Override
//    public void update() {
//        if (!opened) {
//            ArrayList<AbstractCard> options = PalomitaFactory.getRandomPalomitasUnique(3);
//            AbstractDungeon.cardRewardScreen.chooseOneOpen(options);
//            opened = true;
//            this.tickDuration();
//            return;
//        }
//
//        if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
//            AbstractCard selected = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
//            AbstractDungeon.cardRewardScreen.discoveryCard = null;
//            AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(selected, 1));
//            this.isDone = true;
//            return;
//        }
//
//        this.tickDuration(); // <- IMPORTANTE: esto hace que espere varios frames
//    }
//}

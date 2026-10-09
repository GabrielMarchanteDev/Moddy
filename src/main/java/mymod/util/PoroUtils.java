package mymod.util;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;

import mymod.cards.kio.Compas;
import mymod.cards.poro.Armonia;
import mymod.cards.poro.Impulso;

public class PoroUtils {

    private static boolean fusionEnProceso = false;

    public static void checkFusion(AbstractPlayer p) {

        if (fusionEnProceso) return;
        if (p == null || p.hand == null) return;

        fusionEnProceso = true;

        AbstractDungeon.actionManager.addToBottom(
            new AbstractGameAction() {

                @Override
                public void update() {

                    boolean fusionRealizada;

                    do {
                        fusionRealizada = false;

                        for (int i = 0; i < p.hand.size() - 1; i++) {

                            AbstractCard c1 = p.hand.group.get(i);
                            AbstractCard c2 = p.hand.group.get(i + 1);

                            boolean fusionPair =
                                    (c1 instanceof Compas && c2 instanceof Impulso) ||
                                    (c1 instanceof Impulso && c2 instanceof Compas);

                            if (fusionPair) {
                            
                            	AbstractDungeon.effectsQueue.add(
                            			 new UpgradeShineEffect(c1.current_x, c1.current_y)
                            		);
                            	
                                p.hand.moveToDiscardPile(c2);
                                p.hand.moveToDiscardPile(c1);

                                p.hand.addToTop(new Armonia());
                              
                                fusionRealizada = true;
                                break; // reinicia escaneo desde el principio
                            }
                        }

                    } while (fusionRealizada);

                    p.hand.refreshHandLayout();
                    p.hand.applyPowers();

                    fusionEnProceso = false;
                    isDone = true;
                }
            }
        );
    }

}

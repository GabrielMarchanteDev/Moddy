package mymod.util;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class HandWatcher {

    private static int previousHandSize = -1;

    public static void update() {

        if (AbstractDungeon.player == null) return;
        if (AbstractDungeon.player.hand == null) return;

        int currentSize = AbstractDungeon.player.hand.size();

        if (currentSize != previousHandSize) {
            previousHandSize = currentSize;
            PoroUtils.checkFusion(AbstractDungeon.player);
        }
    }
}

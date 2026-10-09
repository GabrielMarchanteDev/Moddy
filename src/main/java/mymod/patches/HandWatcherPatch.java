package mymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

import mymod.util.HandWatcher;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "update"
)
public class HandWatcherPatch {

    @SpirePostfixPatch
    public static void patch(AbstractPlayer __instance) {
        HandWatcher.update();
    }
}

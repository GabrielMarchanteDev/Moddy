package mymod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import mymod.memo.MemoScreen;

public class MemoMenuPatch {
    @SpirePatch(clz = MainMenuScreen.class, method = "update")
    public static class Update {
        @SpirePrefixPatch public static SpireReturn<Void> Prefix(MainMenuScreen __instance) {
            return MemoScreen.get().update(__instance) ? SpireReturn.Return(null) : SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = MainMenuScreen.class, method = "render")
    public static class Render {
        @SpirePostfixPatch public static void Postfix(MainMenuScreen __instance, SpriteBatch sb) {
            MemoScreen.get().render(__instance, sb);
        }
    }
}

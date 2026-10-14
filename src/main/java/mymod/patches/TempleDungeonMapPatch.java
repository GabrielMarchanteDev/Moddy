package mymod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import mymod.map.TempleDungeonMap;

public class TempleDungeonMapPatch {
    @SpirePatch(clz = DungeonMapScreen.class, method = "update")
    public static class Update {
        @SpirePrefixPatch public static SpireReturn<Void> Prefix(DungeonMapScreen __instance) {
            if (!TempleDungeonMap.active()) return SpireReturn.Continue();
            TempleDungeonMap.get().update(__instance);
            return SpireReturn.Return(null);
        }
    }
    @SpirePatch(clz = DungeonMapScreen.class, method = "render")
    public static class Render {
        @SpirePrefixPatch public static SpireReturn<Void> Prefix(DungeonMapScreen __instance, SpriteBatch sb) {
            if (!TempleDungeonMap.active()) return SpireReturn.Continue();
            TempleDungeonMap.get().render(sb, __instance);
            return SpireReturn.Return(null);
        }
    }
}

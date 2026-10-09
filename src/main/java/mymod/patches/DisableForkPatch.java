package mymod.patches;

import actlikeit.dungeons.CustomDungeon;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;

@SpirePatch(clz = CustomDungeon.class, method = "isForkNecessary")
public class DisableForkPatch {
    public static boolean Postfix(boolean result) {
        return false; // Nunca mostrar el evento GetForked
    }
}

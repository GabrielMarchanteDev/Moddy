package mymod.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.audio.Music;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.audio.MainMusic;
import com.megacrit.cardcrawl.audio.TempMusic;
import mymod.characters.ui.LunarMusicMix;

public class LunarMusicMixPatch {
    private static void wrap(Object owner, Class<?> type) {
        Music music = ReflectionHacks.getPrivate(owner, type, "music");
        if (music != null && !(music instanceof LunarMusicMix)) {
            ReflectionHacks.setPrivate(owner, type, "music", new LunarMusicMix(music));
        }
    }

    @SpirePatch(clz = MainMusic.class, method = "update")
    public static class Main {
        @SpirePrefixPatch
        public static void prefix(MainMusic __instance) { wrap(__instance, MainMusic.class); }
    }

    @SpirePatch(clz = TempMusic.class, method = "update")
    public static class Temporary {
        @SpirePrefixPatch
        public static void prefix(TempMusic __instance) { wrap(__instance, TempMusic.class); }
    }
}

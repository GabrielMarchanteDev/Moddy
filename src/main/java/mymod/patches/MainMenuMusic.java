package mymod.patches;

import com.badlogic.gdx.audio.Music;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.audio.MainMusic;

@SpirePatch(clz = MainMusic.class, method = "getSong")
public class MainMenuMusic {
    private static Music cachedMenuMusic = null;

    @SpirePostfixPatch
    public static Music Postfix(Music __result, MainMusic __instance, String key) {
        if ("MENU".equals(key)) {
            if (cachedMenuMusic == null) {
                cachedMenuMusic = MainMusic.newMusic("audio/music/FEZ.mp3");
                cachedMenuMusic.setLooping(true);
                cachedMenuMusic.setVolume(0.7f);
            }
            return cachedMenuMusic;
        }

        // En cualquier otro caso, no interferir
        return __result;
    }
}

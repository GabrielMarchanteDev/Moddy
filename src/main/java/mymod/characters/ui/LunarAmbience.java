package mymod.characters.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import mymod.MyMod;
import mymod.characters.Poro;

/** Ambiente lunar y atenuacion gradual de la musica existente. */
public final class LunarAmbience {
    public static final float VOLUME = 0.45F;
    public static final float BACKGROUND_VOLUME = 0.25F;
    public static final float FADE_SECONDS = 1.25F;
    private static Music track;
    private static float level;
    private static boolean failed;

    private LunarAmbience() { }

    public static float backgroundGain() {
        float fade = level * level * (3F - 2F * level);
        return 1F - (1F - BACKGROUND_VOLUME) * fade;
    }

    private static boolean active() {
        return CardCrawlGame.mode == CardCrawlGame.GameMode.GAMEPLAY
                && AbstractDungeon.player instanceof Poro
                && !AbstractDungeon.player.isDead && !AbstractDungeon.player.isDying
                && AbstractDungeon.getCurrMapNode() != null
                && AbstractDungeon.getCurrRoom() != null
                && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
                && ((Poro) AbstractDungeon.player).getLunarPhase() == LunarPhase.LLENA;
    }

    public static void update() {
        boolean wanted = active();
        if (!wanted) failed = false;
        if (wanted && track == null && !failed) {
            try {
                track = Gdx.audio.newMusic(Gdx.files.internal("audio/poro/MoonSound.mp3"));
                track.setLooping(true);
                track.setVolume(0F);
                track.play();
            } catch (RuntimeException e) {
                dispose();
                failed = true;
                MyMod.logger.error("[Luna Poro] No se pudo reproducir el ambiente", e);
            }
        }
        if (track == null) {
            LunarMusicMix.refresh();
            return;
        }
        float step = Gdx.graphics.getDeltaTime() / FADE_SECONDS;
        level = Math.max(0F, Math.min(1F, level + (wanted ? step : -step)));
        float fade = level * level * (3F - 2F * level);
        track.setVolume(VOLUME * fade * Settings.MASTER_VOLUME * Settings.MUSIC_VOLUME);
        if (!wanted && level == 0F) dispose();
        LunarMusicMix.refresh();
    }

    private static void dispose() {
        if (track != null) {
            track.stop();
            track.dispose();
            track = null;
        }
        level = 0F;
    }
}

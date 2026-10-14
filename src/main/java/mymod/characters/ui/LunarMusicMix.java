package mymod.characters.ui;

import com.badlogic.gdx.audio.Music;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/** Guarda el volumen logico para preservar los fundidos propios del juego. */
public final class LunarMusicMix implements Music {
    private static final Set<LunarMusicMix> tracks = Collections.newSetFromMap(
            new WeakHashMap<LunarMusicMix, Boolean>());
    private final Music source;
    private float volume;

    public LunarMusicMix(Music source) {
        this.source = source;
        volume = source.getVolume();
        tracks.add(this);
    }

    public static void refresh() {
        for (LunarMusicMix track : tracks) track.applyVolume();
    }

    private void applyVolume() { source.setVolume(volume * LunarAmbience.backgroundGain()); }
    public void setVolume(float value) { volume = value; applyVolume(); }
    public float getVolume() { return volume; }
    public void play() { source.play(); }
    public void pause() { source.pause(); }
    public void stop() { source.stop(); }
    public boolean isPlaying() { return source.isPlaying(); }
    public void setLooping(boolean looping) { source.setLooping(looping); }
    public boolean isLooping() { return source.isLooping(); }
    public void setPan(float pan, float value) {
        volume = value;
        source.setPan(pan, value * LunarAmbience.backgroundGain());
    }
    public void setPosition(float position) { source.setPosition(position); }
    public float getPosition() { return source.getPosition(); }
    public void dispose() { tracks.remove(this); source.dispose(); }
    public void setOnCompletionListener(final OnCompletionListener listener) {
        source.setOnCompletionListener(listener == null ? null : new OnCompletionListener() {
            public void onCompletion(Music music) { listener.onCompletion(LunarMusicMix.this); }
        });
    }
}

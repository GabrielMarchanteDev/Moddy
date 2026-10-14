import com.badlogic.gdx.audio.Music;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import mymod.characters.ui.LunarAmbience;
import mymod.characters.ui.LunarMusicMix;

/** Verifica restauracion y fundidos sin dispositivo de audio. */
public class LunarMusicMixCheck {
    private static void near(float actual, float expected) {
        if (Math.abs(actual - expected) > 0.0001F) throw new AssertionError(actual + " != " + expected);
    }
    public static void main(String[] args) throws Exception {
        final float[] output = {0.8F};
        Music source = (Music) Proxy.newProxyInstance(Music.class.getClassLoader(), new Class<?>[]{Music.class},
                (proxy, method, values) -> {
                    if (method.getName().equals("getVolume")) return output[0];
                    if (method.getName().equals("setVolume")) output[0] = (Float) values[0];
                    return null;
                });
        Field level = LunarAmbience.class.getDeclaredField("level");
        level.setAccessible(true);
        LunarMusicMix mix = new LunarMusicMix(source);
        level.setFloat(null, 1F);
        LunarMusicMix.refresh();
        near(output[0], 0.2F);
        near(mix.getVolume(), 0.8F);
        LunarMusicMix.refresh();
        near(output[0], 0.2F);
        mix.setVolume(0.4F);
        near(output[0], 0.1F);
        level.setFloat(null, 0.5F);
        LunarMusicMix.refresh();
        near(output[0], 0.25F);
        level.setFloat(null, 0F);
        LunarMusicMix.refresh();
        near(output[0], 0.4F);
        mix.dispose();
        LunarMusicMix.refresh();
        System.out.println("PASS: attenuation, native volume changes, midpoint, restoration and disposal");
    }
}

package mymod.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;

public class BaymaxHistory extends AbstractGameEffect {
    private final String[] frames = {
        "scenes/baymax/1.png", "scenes/baymax/2.png",
        "scenes/baymax/3.png", "scenes/baymax/4.png",
        "scenes/baymax/5.png", "scenes/baymax/6.png",
        "scenes/baymax/7.png", "scenes/baymax/8.png"
    };
    // Gestor propio: estas texturas no se comparten con otras introducciones.
    private final AssetManager assets = new AssetManager();
    private boolean disposed;
    private final ArrayList<String> sounds = new ArrayList<>();
    private int currentIndex = 0;
    private float frameTimer = 0f;
    private final float FRAME_DURATION = 4.5f; // duración por frame
    private final float FADE_TIME = 1.0f;      // tiempo de transición entre imágenes
    private boolean shaking = false;
    private int lastSoundPlayedIndex = -1;
    public BaymaxHistory() {
        // Solo la primera es necesaria inmediatamente; la segunda se prepara en update.
        try {
            queueFrame(0);
            assets.finishLoadingAsset(frames[0]);
            queueFrame(1);
        } catch (RuntimeException e) {
            dispose();
            throw e;
        }
        CardCrawlGame.music.playTempBgmInstantly("Satisfied.ogg", false); // false = no repetir


        // Sonido de fondo
        CardCrawlGame.sound.play("ATTACK_FIRE");

        // Duración total (por seguridad, aunque no se usa directamente)
        this.duration = frames.length * FRAME_DURATION;
    }

    private void queueFrame(int index) {
        if (index >= frames.length) return;
        TextureParameter parameter = new TextureParameter();
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        assets.load(frames[index], Texture.class, parameter);
    }

    @Override
    public void update() {
        if (disposed || isDone) return;
        assets.update();
        frameTimer += Gdx.graphics.getDeltaTime();
        if (frameTimer >= FRAME_DURATION) {
            // Si la lectura tarda, mantener la imagen actual en vez de mostrar negro.
            if (currentIndex + 1 < frames.length && !assets.isLoaded(frames[currentIndex + 1])) {
                frameTimer = FRAME_DURATION;
                return;
            }
            frameTimer = 0f;
            currentIndex++;
            if (currentIndex >= frames.length) {
                isDone = true;
                dispose();
                return;
            }
            // Liberar antes de pedir la siguiente: nunca conservar mas de tres texturas.
            if (currentIndex >= 2) assets.unload(frames[currentIndex - 2]);
            queueFrame(currentIndex + 1);
        }
        // Emitir sonido si aún no se ha hecho para el frame actual
        if (currentIndex < sounds.size() && currentIndex != lastSoundPlayedIndex) {
            String soundKey = sounds.get(currentIndex);
            if (soundKey != null) {
                CardCrawlGame.sound.play(soundKey);
            }
            lastSoundPlayedIndex = currentIndex;
        }
      
        // Activar sacudida si estás en un frame concreto
        shaking = (currentIndex == 3);
    }

    

    @Override
    public void render(SpriteBatch sb) {
        if (disposed || currentIndex >= frames.length) return;

        Texture current = assets.get(frames[currentIndex], Texture.class);
        Texture previous = (currentIndex > 0) ? assets.get(frames[currentIndex - 1], Texture.class) : null;

        float localTime = frameTimer;

        // Fondo negro por si acaso
        sb.setColor(Color.BLACK);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0, 0, Settings.WIDTH, Settings.HEIGHT);

        // Fundido entre imágenes
        if (localTime < FADE_TIME && previous != null) {
            float alpha = localTime / FADE_TIME;

            // Imagen anterior completamente visible
            renderTexture(sb, previous, 1f, false);
            // Imagen nueva superpuesta con opacidad creciente
            renderTexture(sb, current, alpha, shaking);
        } else {
            renderTexture(sb, current, 1f, shaking);
        }
    }

    private void renderTexture(SpriteBatch sb, Texture texture, float alpha, boolean shake) {
        float x = 0f;
        float y = 0f;

        if (shake) {
            x += MathUtils.random(-10f, 10f) * Settings.scale;
            y += MathUtils.random(-10f, 10f) * Settings.scale;
        }

        Color originalColor = sb.getColor().cpy();
        sb.setColor(1f, 1f, 1f, alpha);
        sb.draw(texture, x, y, Settings.WIDTH, Settings.HEIGHT);
        sb.setColor(originalColor);
    }

    @Override
    public void dispose() {
        if (disposed) return;
        disposed = true;
        isDone = true;
        assets.dispose();
    }
}

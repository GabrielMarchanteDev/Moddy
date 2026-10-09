package mymod.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;

public class KioScene extends AbstractGameEffect {
    private final ArrayList<Texture> frames = new ArrayList<>();
    private final ArrayList<String> sounds = new ArrayList<>();
    private int currentIndex = 0;
    private float frameTimer = 0f;
    private final float FRAME_DURATION = 2.5f; // duración por frame
    private final float FADE_TIME = 1.0f;      // tiempo de transición entre imágenes
    private boolean shaking = false;
    private int lastSoundPlayedIndex = -1;
    public KioScene() {
        // Carga tus imágenes, PUEDES AÑADIR MAS DE 3, VAN UNA DETRÁS DE OTRA
        frames.add(ImageMaster.loadImage("scenes/1/1.png"));
        sounds.add("KIO_SCENE1");
        frames.add(ImageMaster.loadImage("scenes/1/2.png"));
        sounds.add("KIO_SCENE2");
        frames.add(ImageMaster.loadImage("scenes/1/3.png"));
        sounds.add("KIO_SCENE3");

        // Sonido de fondo
        CardCrawlGame.sound.play("ATTACK_FIRE");

        // Duración total (por seguridad, aunque no se usa directamente)
        this.duration = frames.size() * FRAME_DURATION;
    }

    @Override
    public void update() {
        frameTimer += Gdx.graphics.getDeltaTime();
        if (frameTimer >= FRAME_DURATION) {
            frameTimer = 0f;
            currentIndex++;
            if (currentIndex >= frames.size()) {
                isDone = true;
            }
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
        shaking = (currentIndex == 1);
    }

    

    @Override
    public void render(SpriteBatch sb) {
        if (currentIndex >= frames.size()) return;

        Texture current = frames.get(currentIndex);
        Texture previous = (currentIndex > 0) ? frames.get(currentIndex - 1) : null;

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
        for (Texture t : frames) {
            t.dispose();
        }
    }
}

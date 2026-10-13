package mymod.characters.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import mymod.characters.Poro;

/** Indicador lunar visual; no aplica bonificaciones de combate. */
public final class PoroMoon {
    public static final float SIZE = 145F;
    public static final float HEIGHT_ABOVE_PORO = 95F;
    public static final float FLOAT_AMPLITUDE = 7F;
    public static final float FLOAT_PERIOD = 3F;
    public static final float FADE_DURATION = 0.45F;

    private final Texture[] textures = new Texture[LunarPhase.values().length];
    private Texture glow;
    private LunarPhase phase = LunarPhase.NUEVA;
    private LunarPhase previous = LunarPhase.NUEVA;
    private float elapsed;
    private float transition = FADE_DURATION;

    public LunarPhase getPhase() { return phase; }

    public void reset() {
        dispose();
        phase = LunarPhase.NUEVA;
        previous = phase;
        elapsed = 0F;
        transition = FADE_DURATION;
    }

    public void advance() {
        previous = phase;
        phase = phase.next();
        transition = 0F;
    }

    private boolean visible(Poro poro) {
        return AbstractDungeon.player == poro && AbstractDungeon.getCurrRoom() != null
                && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
                && !poro.isDead && !poro.isDying;
    }

    public void update(Poro poro) {
        if (!visible(poro)) { dispose(); return; }
        float delta = Gdx.graphics.getDeltaTime();
        elapsed = (elapsed + delta) % FLOAT_PERIOD;
        transition = Math.min(FADE_DURATION, transition + delta);
    }

    private Texture texture(LunarPhase value) {
        int index = value.ordinal();
        if (textures[index] == null) {
            textures[index] = new Texture(Gdx.files.internal("images/poro/moon/" + value.getImageName() + ".png"));
            textures[index].setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        return textures[index];
    }

    private Texture glowTexture() {
        if (glow == null) {
            Pixmap pixmap = new Pixmap(128, 128, Pixmap.Format.RGBA8888);
            pixmap.setBlending(Pixmap.Blending.None);
            try {
                for (int y = 0; y < 128; y++) {
                    for (int x = 0; x < 128; x++) {
                        float dx = (x - 63.5F) / 63.5F;
                        float dy = (y - 63.5F) / 63.5F;
                        float radius = (float) Math.sqrt(dx * dx + dy * dy);
                        // Banda amplia que se solapa con el borde del disco lunar.
                        float distance = (radius - 0.52F) / 0.25F;
                        float strength = (float) Math.exp(-distance * distance);
                        pixmap.setColor(1F, 1F, 1F, strength);
                        pixmap.drawPixel(x, y);
                    }
                }
                glow = new Texture(pixmap);
                glow.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            } finally {
                pixmap.dispose();
            }
        }
        return glow;
    }

    private void renderMoonGlow(SpriteBatch sb, float x, float y, float size, float weight, boolean full) {
        if (weight <= 0F) return;
        float wave = MathUtils.sin(elapsed * MathUtils.PI2 / FLOAT_PERIOD);
        float pulse = 0.5F + 0.5F * wave;
        int source = sb.getBlendSrcFunc();
        int destination = sb.getBlendDstFunc();
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, full ? GL20.GL_ONE : GL20.GL_ONE_MINUS_SRC_ALPHA);
        float red = full ? 0.82F : 0.015F;
        float green = full ? 0.91F : 0.04F;
        float blue = full ? 1F : 0.12F;
        // Corona luminosa exterior: se dibuja detras del PNG, sin tenir su superficie.
        float corona = size * (1.55F + 0.04F * pulse);
        sb.setColor(red, green, blue, (full ? 0.65F + 0.25F * pulse : 0.65F + 0.15F * pulse) * weight);
        sb.draw(glowTexture(), x + (size - corona) / 2F, y + (size - corona) / 2F, corona, corona);
        float aura = size * (2.25F + 0.12F * pulse);
        sb.setColor(red, green, blue, (0.18F + 0.12F * pulse) * weight);
        sb.draw(glowTexture(), x + (size - aura) / 2F, y + (size - aura) / 2F, aura, aura);
        // Destellos fuera de la silueta, con intensidad y posicion ondulantes.
        for (int i = 0; i < 8; i++) {
            float angle = i * MathUtils.PI2 / 8F + wave * 0.12F;
            float sparkle = 0.5F + 0.5F * MathUtils.sin(elapsed * MathUtils.PI2 / FLOAT_PERIOD + i);
            float radius = size * (0.65F + 0.06F * sparkle);
            float dot = (6F + 7F * sparkle) * Settings.scale;
            float dx = x + size / 2F + MathUtils.cos(angle) * radius - dot / 2F;
            float dy = y + size / 2F + MathUtils.sin(angle) * radius - dot / 2F;
            sb.setColor(red, green, blue, (0.2F + 0.6F * sparkle) * weight);
            sb.draw(glowTexture(), dx, dy, dot, dot);
        }
        sb.setBlendFunction(source, destination);
    }
    public void render(SpriteBatch sb, Poro poro) {
        if (!visible(poro)) return;
        float size = SIZE * Settings.scale;
        float x = poro.hb.cX - size / 2F;
        float bob = MathUtils.sin(elapsed * MathUtils.PI2 / FLOAT_PERIOD) * FLOAT_AMPLITUDE;
        float y = poro.hb.cY + poro.hb.height / 2F + (HEIGHT_ABOVE_PORO + bob) * Settings.scale;
        Color original = sb.getColor().cpy();
        float alpha = transition / FADE_DURATION;
        float fullMoonWeight = (phase == LunarPhase.LLENA ? alpha : 0F)
                + (previous == LunarPhase.LLENA ? 1F - alpha : 0F);
        float newMoonWeight = (phase == LunarPhase.NUEVA ? alpha : 0F)
                + (previous == LunarPhase.NUEVA ? 1F - alpha : 0F);
        renderMoonGlow(sb, x, y, size, fullMoonWeight, true);
        renderMoonGlow(sb, x, y, size, newMoonWeight, false);
        if (alpha < 1F) {
            sb.setColor(1F, 1F, 1F, 1F - alpha);
            sb.draw(texture(previous), x, y, size, size);
        }
        sb.setColor(1F, 1F, 1F, alpha);
        sb.draw(texture(phase), x, y, size, size);
        sb.setColor(original);
    }

    public void dispose() {
        if (glow != null) { glow.dispose(); glow = null; }
        for (int i = 0; i < textures.length; i++) {
            if (textures[i] != null) { textures[i].dispose(); textures[i] = null; }
        }
    }
}

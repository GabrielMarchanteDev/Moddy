package mymod.characters.ui;

import basemod.abstracts.CustomEnergyOrb;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;

/** Energia de Kio: aro y palomitas giran en sentidos opuestos; nucleo fijo. */
public class PopcornEnergyOrb extends CustomEnergyOrb {
    private static final String ROOT = "images/kio/ui/popcornEnergy/";
    private static final String[] LAYERS = {
        ROOT + "rim.png", ROOT + "shine.png", ROOT + "blank.png", ROOT + "blank.png", ROOT + "blank.png",
        ROOT + "core.png",
        ROOT + "rim.png", ROOT + "shine.png", ROOT + "blank.png", ROOT + "blank.png", ROOT + "blank.png"
    };
    private final Texture[] popcorn = new Texture[3];
    private float time;
    private final float[] popcornAngles = new float[3];
    private static final float[] POPCORN_SPEEDS = {-14F, -24F, -36F};

    public PopcornEnergyOrb() {
        super(LAYERS, ROOT + "energyVfx.png", new float[] {0F, 0F, 0F, 0F, 18F});
        for (int i = 0; i < popcorn.length; i++) {
            popcorn[i] = ImageMaster.loadImage(ROOT + "popcorn" + (i + 1) + ".png");
        }
    }

    @Override public void updateOrb(int energy) {
        super.updateOrb(energy);
        time = (time + Gdx.graphics.getDeltaTime()) % 3F;
        for (int i = 0; i < popcornAngles.length; i++) {
            popcornAngles[i] = (popcornAngles[i] + Gdx.graphics.getDeltaTime()
                    * POPCORN_SPEEDS[i] * (energy > 0 ? 1F : 0.4F)) % 360F;
        }
    }

    private void draw(SpriteBatch sb, Texture image, float x, float y, float angle) {
        float scale = 1.15F * Settings.scale;
        sb.draw(image, x - 64F, y - 64F, 64F, 64F, 128F, 128F,
                scale, scale, angle, 0, 0, 128, 128, false, false);
    }

    @Override public void renderOrb(SpriteBatch sb, boolean enabled, float x, float y) {
        Color previous = sb.getColor().cpy();
        sb.setColor(Color.WHITE);
        draw(sb, baseLayer, x, y, 0F);
        sb.setColor(enabled ? 1F : 0.48F, enabled ? 1F : 0.38F, enabled ? 1F : 0.3F, 1F);
        draw(sb, energyLayers[0], x, y, angles[0]);
        float pulse = 0.7F + 0.3F * MathUtils.sin(time * MathUtils.PI2 / 3F);
        sb.setColor(1F, 1F, 1F, enabled ? pulse : 0.15F * pulse);
        draw(sb, energyLayers[1], x, y, 0F);
        sb.setColor(enabled ? 1F : 0.65F, enabled ? 1F : 0.6F, enabled ? 1F : 0.5F, 1F);
        for (int i = 0; i < popcorn.length; i++) draw(sb, popcorn[i], x, y, popcornAngles[i]);
        sb.setColor(previous);
    }
}

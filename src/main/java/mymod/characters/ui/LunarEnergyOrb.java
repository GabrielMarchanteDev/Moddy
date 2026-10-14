package mymod.characters.ui;

import basemod.abstracts.CustomEnergyOrb;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;

/** Cinco capas rotatorias, nucleo fijo y variante atenuada sin energia. */
public class LunarEnergyOrb extends CustomEnergyOrb {
    private static final String ROOT = "images/poro/ui/lunarEnergy/";
    private static final String[] LAYERS = {
        ROOT + "aura.png", ROOT + "innerGlow.png", ROOT + "wisps.png", ROOT + "rim.png", ROOT + "sparkles.png",
        ROOT + "core.png",
        ROOT + "aura.png", ROOT + "innerGlow.png", ROOT + "wisps.png",
        ROOT + "rim.png", ROOT + "sparkles.png"
    };
    private float time;

    public LunarEnergyOrb() {
        super(LAYERS, ROOT + "energyVfx.png", new float[] {20F, -10F, 12F, -5F, 3F});
    }

    @Override public void updateOrb(int energy) {
        super.updateOrb(energy);
        time = (time + Gdx.graphics.getDeltaTime()) % 3F;
    }

    @Override public void renderOrb(SpriteBatch sb, boolean enabled, float x, float y) {
        Color previous = sb.getColor().cpy();
        float scale = 1.15F * Settings.scale;
        Texture[] layers = enabled ? energyLayers : noEnergyLayers;
        sb.setColor(Color.WHITE);
        sb.draw(baseLayer, x - 64F, y - 64F, 64F, 64F, 128F, 128F,
                scale, scale, 0F, 0, 0, 128, 128, false, false);
        for (int i = 0; i < layers.length; i++) {
            float alpha = i == 4 ? 0.65F + 0.35F * MathUtils.sin(time * MathUtils.PI2 / 3F) : 1F;
            sb.setColor(enabled ? 1F : 0.22F, enabled ? 1F : 0.35F,
                    enabled ? 1F : 0.6F, enabled ? alpha : alpha * 0.7F);
            sb.draw(layers[i], x - 64F, y - 64F, 64F, 64F, 128F, 128F,
                    scale, scale, angles[i], 0, 0, 128, 128, false, false);
        }
        sb.setColor(previous);
    }
}

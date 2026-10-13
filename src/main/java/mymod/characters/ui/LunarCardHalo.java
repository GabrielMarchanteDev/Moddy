package mymod.characters.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;

/** Destellos y barrido de transformacion; los haces luminosos usan superFlash nativo. */
public final class LunarCardHalo {
    private LunarCardHalo() { }

    public static void render(SpriteBatch sb, AbstractCard card, float time, float burst) {
        Color color = sb.getColor().cpy();
        int source = sb.getBlendSrcFunc();
        int destination = sb.getBlendDstFunc();
        float pulse = 0.5F + 0.5F * MathUtils.sin(time * MathUtils.PI2 / 2.8F);
        float scale = Settings.scale * card.drawScale;
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        for (int i = 0; i < 6; i++) {
            float a = i * MathUtils.PI2 / 6F;
            float localX = MathUtils.cos(a) * 170F;
            float localY = MathUtils.sin(a) * 230F;
            float rotation = card.angle * MathUtils.degreesToRadians;
            float x = card.current_x + (localX * MathUtils.cos(rotation) - localY * MathUtils.sin(rotation)) * scale;
            float y = card.current_y + (localX * MathUtils.sin(rotation) + localY * MathUtils.cos(rotation)) * scale;
            float shimmer = 0.5F + 0.5F * MathUtils.sin(time * 2F + i);
            float dot = (2F + 2F * shimmer) * scale;
            sb.setColor(0.82F, 0.91F, 1F, (0.12F + 0.35F * shimmer) * card.transparency);
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, x - dot, y - dot / 3F, dot * 2F, dot * 2F / 3F);
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, x - dot / 3F, y - dot, dot * 2F / 3F, dot * 2F);
        }
        sb.setBlendFunction(source, destination);
        sb.setColor(color);
    }

    public static void renderSweep(SpriteBatch sb, AbstractCard card, float progress) {
        Color color = sb.getColor().cpy();
        int source = sb.getBlendSrcFunc();
        int destination = sb.getBlendDstFunc();
        float scale = Settings.scale * card.drawScale;
        float localX = -120F + 240F * progress;
        float rotation = card.angle * MathUtils.degreesToRadians;
        float x = card.current_x + localX * scale * MathUtils.cos(rotation);
        float y = card.current_y + localX * scale * MathUtils.sin(rotation);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        sb.setColor(0.82F, 0.91F, 1F, 0.18F * MathUtils.sin(progress * MathUtils.PI) * card.transparency);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, x - 10F, y - 190F, 10F, 190F,
                20F, 380F, scale, scale, card.angle, 0, 0, 1, 1, false, false);
        sb.setBlendFunction(source, destination);
        sb.setColor(color);
    }
}

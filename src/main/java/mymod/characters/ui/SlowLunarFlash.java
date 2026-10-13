package mymod.characters.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.vfx.cardManip.CardFlashVfx;

/** SuperFlash nativo con entrada y salida de igual duracion. */
public class SlowLunarFlash extends CardFlashVfx {
    public static final float FADE_TIME = 1.25F;
    public static final float TOTAL_TIME = FADE_TIME * 2F;
    private float elapsed;

    public SlowLunarFlash(AbstractCard card, Color color) {
        super(card, color, true);
        duration = 0F;
    }

    @Override public void update() {
        float delta = Gdx.graphics.getDeltaTime();
        elapsed = Math.min(TOTAL_TIME, elapsed + delta);
        float strength = elapsed <= FADE_TIME ? elapsed / FADE_TIME
                : (TOTAL_TIME - elapsed) / FADE_TIME;
        // El render nativo usa duration como opacidad. Recorrer 0 -> 0,5 -> 0
        // produce una entrada que refleja exactamente el desvanecimiento de salida.
        // Compensar la resta de delta que hace update antes de calcular su escala.
        duration = 0.5F * strength + delta;
        super.update();
        isDone = elapsed >= TOTAL_TIME;
    }
}

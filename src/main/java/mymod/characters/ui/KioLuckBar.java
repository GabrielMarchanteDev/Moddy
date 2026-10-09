package mymod.characters.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import mymod.characters.Kio;

public class KioLuckBar {
	private static final Texture LUCK_BAR_TEXTURE_FILL  = new Texture("images/ui/barraLlena.png");
	private static final Texture LUCK_BAR_TEXTURE = new Texture("images/ui/barraVacia.png");
    public static void render(SpriteBatch sb) {
    	 if (!AbstractDungeon.isPlayerInDungeon() || 
    		        AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
    		        return;
    		    }
    	
        if (!(AbstractDungeon.player instanceof Kio)) return;
        Kio kio = (Kio) AbstractDungeon.player;

        float scale = Settings.scale;
        float barWidth = 230f * scale;
        float barHeight = 22f * scale;
        float padding = 4f * scale;

        float x = kio.hb.cX - barWidth / 2f;
        float y = kio.hb.cY - kio.hb.height / 2f - barHeight - padding + 18f * scale;


        float percent = (float) kio.getKioLuck() / Kio.MAX_LUCK;
        float filled = percent * barWidth;

        // Fondo
        sb.setColor(Color.WHITE);
        sb.draw(LUCK_BAR_TEXTURE, x, y, barWidth, barHeight);

        // Parte llena (suerte actual)
       // sb.setColor(Color.GOLD);
        sb.draw(LUCK_BAR_TEXTURE_FILL, x, y, filled, barHeight);

        // Texto encima (centrado)
        String text =  kio.getKioLuck() + " / " + Kio.MAX_LUCK;
        FontHelper.renderFontCentered(sb, FontHelper.healthInfoFont, text,
                kio.hb.cX, y + barHeight / 2f + 1f * scale, Color.WHITE);
    }
}

package mymod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.DiscardPileViewScreen;
import com.megacrit.cardcrawl.screens.DrawPileViewScreen;
import com.megacrit.cardcrawl.screens.ExhaustPileViewScreen;

import mymod.powers.AhoraVengoThisTurnPower;


public class KioImmuneOverlay {
 private static final Texture KIO = new Texture("images/kio/char/Rarete.png");

 private static void renderIfImmune(SpriteBatch sb) {
     if (!AbstractDungeon.isScreenUp) return;
     if (!AbstractDungeon.player.hasPower(AhoraVengoThisTurnPower.POWER_ID)) return;

     sb.setColor(Color.WHITE);
     
     float s = 0.40f; // 40% del tamaño natural
     float w = KIO.getWidth()  * Settings.scale * s;
     float h = KIO.getHeight() * Settings.scale * s;
     float x = Settings.WIDTH - w - 24f * Settings.scale;
     float y = (Settings.HEIGHT - h) / 2f - 350f;
     sb.draw(KIO, x, y, w, h);

 }

 @SpirePatch2(clz = DrawPileViewScreen.class, method = "render")
 public static class DrawOverlay {
     @SpirePostfixPatch
     public static void postfix(DrawPileViewScreen __instance, SpriteBatch sb) {
         renderIfImmune(sb);
     }
 }

 @SpirePatch2(clz = DiscardPileViewScreen.class, method = "render")
 public static class DiscardOverlay {
     @SpirePostfixPatch
     public static void postfix(DiscardPileViewScreen __instance, SpriteBatch sb) {
         renderIfImmune(sb);
     }
 }

 @SpirePatch2(clz = ExhaustPileViewScreen.class, method = "render")
 public static class ExhaustOverlay {
     @SpirePostfixPatch
     public static void postfix(ExhaustPileViewScreen __instance, SpriteBatch sb) {
         renderIfImmune(sb);
     }
 }
}

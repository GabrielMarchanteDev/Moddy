package mymod.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.map.DungeonMap;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import mymod.map.config.KioMapBuilder;
import mymod.map.config.KioMapConfig;

/** Desplaza solo el jefe final; el pergamino conserva su posicion original. */
public class KioFinalBossPositionPatch {
    private static final float RISE = 650F;

    public static float offset() {
        return "mymod:Kanto".equals(AbstractDungeon.id)
                && KioMapConfig.isKioRun() && KioMapBuilder.isFinalBossOutside()
                ? RISE * Settings.scale : 0F;
    }

    private static ExprEditor bossOffsetEditor() {
        return new ExprEditor() {
            @Override public void edit(FieldAccess field) throws CannotCompileException {
                if (field.isReader() && field.getClassName().equals(DungeonMap.class.getName())
                        && "mapOffsetY".equals(field.getFieldName())) {
                    field.replace("{ $_ = $proceed() + mymod.patches.KioFinalBossPositionPatch.offset(); }");
                }
            }
        };
    }

    @SpirePatch(clz = DungeonMap.class, method = "update")
    public static class MoveHitbox {
        @SpireInstrumentPatch public static ExprEditor Instrument() { return bossOffsetEditor(); }
    }

    @SpirePatch(clz = DungeonMap.class, method = "renderBossIcon")
    public static class MoveIcon {
        @SpireInstrumentPatch public static ExprEditor Instrument() { return bossOffsetEditor(); }

        @SpirePrefixPatch
        public static void Prefix(DungeonMap __instance, SpriteBatch sb) {
            float rise = offset();
            if (rise == 0F || ImageMaster.MAP_DOT_1 == null) return;
            Color previous = new Color(sb.getColor());
            Color tint = ReflectionHacks.getPrivate(__instance, DungeonMap.class, "bossNodeColor");
            sb.setColor(tint);
            float size = (Settings.isMobile ? 560F : 512F) * Settings.scale;
            float bottom = __instance.bossHb.cY - size / 2F;
            float dot = 12F * Settings.scale;
            for (float y = bottom - rise + dot; y < bottom - dot; y += 36F * Settings.scale) {
                sb.draw(ImageMaster.MAP_DOT_1, __instance.bossHb.cX - dot / 2F, y, dot, dot);
            }
            sb.setColor(previous);
        }
    }

    @SpirePatch(clz = DungeonMapScreen.class, method = "open")
    public static class ExtendScroll {
        @SpirePostfixPatch
        public static void Postfix(DungeonMapScreen __instance) {
            float rise = offset();
            if (rise == 0F) return;
            // Misma base de ActLikeIt, con espacio adicional para ver el icono completo.
            float limit = (AbstractDungeon.map.size() * -166.666F + 200F) * Settings.scale - rise;
            ReflectionHacks.setPrivate(__instance, DungeonMapScreen.class, "mapScrollUpperLimit", limit);
        }
    }
}

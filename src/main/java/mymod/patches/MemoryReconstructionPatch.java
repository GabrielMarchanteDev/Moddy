package mymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.core.AbstractCreature;
import java.util.ArrayDeque;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import javassist.CannotCompileException;
import mymod.memo.RebuildingMemory;

public class MemoryReconstructionPatch {
    @SpirePatch(clz = AbstractDungeon.class, method = "render")
    public static class Scene {
        @SpireInstrumentPatch public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override public void edit(MethodCall call) throws CannotCompileException {
                    if (call.getClassName().equals("com.megacrit.cardcrawl.scenes.AbstractScene")) {
                        if (call.getMethodName().equals("renderCombatRoomBg"))
                            call.replace("{ if (!mymod.memo.RebuildingMemory.renderBackground($1)) { $proceed($$); } }");
                        else if (call.getMethodName().equals("renderCombatRoomFg"))
                            call.replace("{ if (!mymod.memo.RebuildingMemory.visible()) { $proceed($$); } }");
                    }
                }
            };
        }
    }
    @SpirePatch(clz = AbstractCreature.class, method = "addBlock", paramtypez = {int.class})
    public static class Damage {
        // Se conserva el nombre de la clase para sustituir su bytecode en builds incrementales.
        private static final ThreadLocal<ArrayDeque<int[]>> before = new ThreadLocal<ArrayDeque<int[]>>() {
            @Override protected ArrayDeque<int[]> initialValue() { return new ArrayDeque<>(); }
        };
        @SpirePrefixPatch public static void Prefix(AbstractCreature __instance) {
            if (__instance == AbstractDungeon.player) before.get().push(new int[]{__instance.currentBlock, 0});
        }
        @SpirePostfixPatch public static void Postfix(AbstractCreature __instance) {
            if (__instance != AbstractDungeon.player || before.get().isEmpty()) return;
            int[] frame = before.get().pop();
            int increase = Math.max(0, __instance.currentBlock - frame[0]);
            // Observador antiguo desactivado; el progreso usa vida restante.
            if (!before.get().isEmpty()) before.get().peek()[1] += increase;
        }
    }
}

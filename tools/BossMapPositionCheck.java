import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import mymod.patches.KioFinalBossPositionPatch;

/** Compila los cambios de bytecode contra el juego instalado sin abrirlo. */
public class BossMapPositionCheck {
    public static void main(String[] args) throws Exception {
        ClassPool pool = new ClassPool(true);
        pool.insertClassPath("target/classes");
        for (String path : args) pool.appendClassPath(path);
        CtClass map = pool.get("com.megacrit.cardcrawl.map.DungeonMap");
        CtMethod update = map.getDeclaredMethod("update");
        // Reproduce tambien el parche Raw de ActLikeIt sobre el mismo metodo.
        actlikeit.patches.DungeonMapPatches.Update.Raw(update);
        requireOffsetRead(update);
        update.instrument(KioFinalBossPositionPatch.MoveHitbox.Instrument());
        CtMethod render = map.getDeclaredMethod("renderBossIcon");
        requireOffsetRead(render);
        render.instrument(KioFinalBossPositionPatch.MoveIcon.Instrument());
        render.insertBefore("mymod.patches.KioFinalBossPositionPatch.MoveIcon.Prefix(this, $1);");
        if (map.toBytecode().length == 0) throw new AssertionError("Empty patched map");
        CtClass screen = pool.get("com.megacrit.cardcrawl.screens.DungeonMapScreen");
        screen.getDeclaredMethod("open").insertAfter(
                "mymod.patches.KioFinalBossPositionPatch.ExtendScroll.Postfix(this);");
        if (screen.toBytecode().length == 0) throw new AssertionError("Empty patched screen");
        System.out.println("PASS: boss render, hitbox and scroll patches compile alongside ActLikeIt");
    }

    private static void requireOffsetRead(CtMethod method) throws Exception {
        final int[] reads = {0};
        method.instrument(new ExprEditor() {
            public void edit(FieldAccess field) {
                if (field.isReader() && "mapOffsetY".equals(field.getFieldName())) reads[0]++;
            }
        });
        if (reads[0] == 0) throw new AssertionError("Missing boss offset in " + method.getName());
    }
}

import javassist.ClassPool;
import javassist.CtClass;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.patcher.PrefixPatchInfo;
import com.evacipated.cardcrawl.modthespire.patcher.PostfixPatchInfo;

/** Verifica las firmas con los inyectores reales de ModTheSpire, sin abrir el juego. */
public class MemoMenuPatchCheck {
    public static void main(String[] args) throws Exception {
        ClassPool pool = new ClassPool(true);
        pool.insertClassPath("target/classes");
        for (String path : args) pool.appendClassPath(path);
        CtClass menu = pool.get("com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen");
        CtClass update = pool.get("mymod.patches.MemoMenuPatch$Update");
        PrefixPatchInfo prefix = new PrefixPatchInfo(menu.getDeclaredMethod("update"), update.getDeclaredMethod("Prefix"));
        prefix.setSpirePatch((SpirePatch) update.getAnnotation(SpirePatch.class));
        prefix.doPatch();
        CtClass render = pool.get("mymod.patches.MemoMenuPatch$Render");
        PostfixPatchInfo postfix = new PostfixPatchInfo(menu.getDeclaredMethod("render"), render.getDeclaredMethod("Postfix"));
        postfix.setSpirePatch((SpirePatch) render.getAnnotation(SpirePatch.class));
        postfix.doPatch();
        if (menu.toBytecode().length == 0) throw new AssertionError("Empty patched menu");
        System.out.println("PASS: M.E.M.O. update and render injections compile with installed ModTheSpire");
    }
}

import javassist.ClassPool;
import javassist.CtClass;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.patcher.PrefixPatchInfo;

/** Comprueba firmas reales de sustitucion del mapa sin iniciar el juego. */
public class TempleMapPatchCheck {
    public static void main(String[] args) throws Exception {
        ClassPool pool = new ClassPool(true);
        pool.insertClassPath("target/classes");
        for (String path : args) pool.appendClassPath(path);
        CtClass screen = pool.get("com.megacrit.cardcrawl.screens.DungeonMapScreen");
        for (String method : new String[]{"update", "render"}) {
            CtClass patch = pool.get("mymod.patches.TempleDungeonMapPatch$" + (method.equals("update") ? "Update" : "Render"));
            PrefixPatchInfo prefix = new PrefixPatchInfo(screen.getDeclaredMethod(method), patch.getDeclaredMethod("Prefix"));
            prefix.setSpirePatch((SpirePatch) patch.getAnnotation(SpirePatch.class));
            prefix.doPatch();
        }
        System.out.println("PASS: temple map update/render injections compile with ModTheSpire");
    }
}

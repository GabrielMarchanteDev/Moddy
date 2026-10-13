import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.patcher.PrefixPatchInfo;

/** Applies the real ModTheSpire prefix injector offline, without opening the game. */
public class StartingDungeonPatchCheck {
    public static void main(String[] args) throws Exception {
        ClassPool pool = new ClassPool(true);
        pool.insertClassPath("target/classes");
        for (String path : args) pool.appendClassPath(path);
        CtClass game = pool.get("com.megacrit.cardcrawl.core.CardCrawlGame");
        CtMethod target = game.getDeclaredMethod("getDungeon", new CtClass[] {
                pool.get("java.lang.String"),
                pool.get("com.megacrit.cardcrawl.characters.AbstractPlayer")});
        CtClass patchClass = pool.get("mymod.patches.KioStartingDungeonPatch");
        PrefixPatchInfo info = new PrefixPatchInfo(target, patchClass.getDeclaredMethod("Prefix"));
        info.setSpirePatch((SpirePatch) patchClass.getAnnotation(SpirePatch.class));
        info.doPatch();
        byte[] patched = game.toBytecode();
        if (patched.length == 0) throw new AssertionError("Empty patched class");
        System.out.println("PASS: real ModTheSpire prefix injection compiled (" + patched.length + " bytes)");
    }
}

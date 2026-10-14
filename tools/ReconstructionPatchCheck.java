import javassist.ClassPool;
import javassist.CtClass;
import javassist.expr.ExprEditor;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.patcher.PostfixPatchInfo;
import com.evacipated.cardcrawl.modthespire.patcher.PrefixPatchInfo;

public class ReconstructionPatchCheck {
    public static void main(String[] args) throws Exception {
        ClassPool pool = new ClassPool(true);
        pool.insertClassPath("target/classes");
        for (String path : args) pool.appendClassPath(path);
        CtClass dungeon = pool.get("com.megacrit.cardcrawl.dungeons.AbstractDungeon");
        Class<?> scene = Class.forName("mymod.patches.MemoryReconstructionPatch$Scene");
        dungeon.getDeclaredMethod("render").instrument((ExprEditor) scene.getMethod("Instrument").invoke(null));
        CtClass creature = pool.get("com.megacrit.cardcrawl.core.AbstractCreature");
        CtClass patch = pool.get("mymod.patches.MemoryReconstructionPatch$Damage");
        PrefixPatchInfo gain = new PrefixPatchInfo(creature.getDeclaredMethod("addBlock"), patch.getDeclaredMethod("Prefix"));
        gain.setSpirePatch((SpirePatch) patch.getAnnotation(SpirePatch.class)); gain.doPatch();
        PostfixPatchInfo damage = new PostfixPatchInfo(creature.getDeclaredMethod("addBlock"), patch.getDeclaredMethod("Postfix"));
        damage.setSpirePatch((SpirePatch) patch.getAnnotation(SpirePatch.class));
        damage.doPatch();
        CtClass rewardPatch = pool.get("mymod.patches.PoroRewardSafetyPatch");
        PrefixPatchInfo rewards = new PrefixPatchInfo(dungeon.getDeclaredMethod("getRewardCards"), rewardPatch.getDeclaredMethod("Prefix"));
        rewards.setSpirePatch((SpirePatch) rewardPatch.getAnnotation(SpirePatch.class));
        rewards.doPatch();
        System.out.println("PASS: scene instrumentation, legacy Block hooks and bounded reward injector");
    }
}

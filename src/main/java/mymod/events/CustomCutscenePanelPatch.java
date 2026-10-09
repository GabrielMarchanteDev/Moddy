//package mymod.events;
//
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.cutscenes.Cutscene;
//import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//
//import mymod.characters.Kio;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//
//@SpirePatch(clz = Cutscene.class, method = SpirePatch.CONSTRUCTOR)
//public class CustomCutscenePanelPatch {
//    public static void Postfix(Cutscene __instance, AbstractPlayer.PlayerClass playerClass) {
//        if (AbstractDungeon.player instanceof Kio) {
//            try {
//                Field panelsField = Cutscene.class.getDeclaredField("panels");
//                panelsField.setAccessible(true);
//                ArrayList<CutscenePanel> panels = (ArrayList<CutscenePanel>) panelsField.get(__instance);
//                for (CutscenePanel panel : panels) {
//                    panel.dispose();
//                }
//                panels.clear();
//
//                panels.add(new CutscenePanel("scenes/1/1.png", "ATTACK_FIRE"));
//                panels.add(new CutscenePanel("scenes/1/2.png"));
//                panels.add(new CutscenePanel("scenes/1/3.png"));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}

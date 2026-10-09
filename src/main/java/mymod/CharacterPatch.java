package mymod;

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;

import basemod.ReflectionHacks;
import mymod.characters.MyPlayerKioClassEnum;
import mymod.characters.MyPlayerPoroClassEnum;

@SpirePatch(clz = CharacterSelectScreen.class, method = "initialize")
public class CharacterPatch {
    private static final Logger logger = LogManager.getLogger(CharacterPatch.class.getName());

    @SpirePostfixPatch
    public static void patch(CharacterSelectScreen __instance) {
        logger.info(">>> Ocultando personajes en la selección...");

        Iterator<CharacterOption> iterator = __instance.options.iterator();
        
        while (iterator.hasNext()) {
            CharacterOption option = iterator.next();
            if (
                option.c.chosenClass != MyPlayerKioClassEnum.KIO_CLASS &&
                option.c.chosenClass != MyPlayerPoroClassEnum.PORO_CLASS
                      ) {
            	
            	
                logger.warn("❌ Eliminando visualmente personaje: " + option.c.chosenClass);
               
                iterator.remove();
            }
        }

        logger.info("✅ Solo Ironclad y Kio son visibles en la selección.");
    }
        

}

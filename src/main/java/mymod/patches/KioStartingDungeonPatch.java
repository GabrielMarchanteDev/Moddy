package mymod.patches;

import actlikeit.dungeons.CustomDungeon;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;

import mymod.MyMod;
import mymod.characters.MyPlayerKioClassEnum;
import mymod.dungeon.Kanto;
import mymod.events.BaymaxCheckEvent;
import mymod.events.MapTestEvent;

/** Start Kio in the custom act before Exordium's constructor generates a map. */
@SpirePatch(clz = CardCrawlGame.class, method = "getDungeon",
        paramtypez = {String.class, AbstractPlayer.class})
public class KioStartingDungeonPatch {
    @SpirePrefixPatch
    public static SpireReturn<AbstractDungeon> Prefix(CardCrawlGame __instance, String key, AbstractPlayer p) {
        if (!Exordium.ID.equals(key) || p == null
                || p.chosenClass != MyPlayerKioClassEnum.KIO_CLASS) {
            return SpireReturn.Continue();
        }
        CustomDungeon prototype = CustomDungeon.dungeons.get(Kanto.ID);
        if (prototype == null) {
            throw new IllegalStateException("[Mapa Kio] Kanto no esta registrado");
        }
        MyMod.logger.info("[Mapa Kio] Redirigiendo creacion inicial Exordium -> Kanto.");
        // BaymaxCheckEvent redirects non-Baymax players to Neow from its constructor.
        // Use an entry event without that side effect only for this Kio test.
        prototype.onEnterEvent(MapTestEvent.class);
        try {
            AbstractDungeon dungeon = prototype.fromProgression(p);
            MyMod.logger.info("[Mapa Kio] Acto inicial creado: " + dungeon.getClass().getName());
            return SpireReturn.Return(dungeon);
        } catch (Exception e) {
            throw new RuntimeException("[Mapa Kio] No se pudo crear el acto inicial", e);
        } finally {
            prototype.onEnterEvent(BaymaxCheckEvent.class);
        }
    }
}

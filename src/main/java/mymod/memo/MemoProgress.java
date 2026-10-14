package mymod.memo;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import mymod.MyMod;

/** Progreso del archivo, independiente de la partida activa. */
public final class MemoProgress {
    private static SpireConfig config;
    private static boolean loaded;
    private static boolean baymaxSeen;
    private static boolean kioPrivateUnlocked;

    private MemoProgress() { }

    public static boolean hasBaymax() {
        if (!loaded) {
            loaded = true;
            try {
                config = new SpireConfig("mymod", "memo-progress");
                baymaxSeen = config.has("baymaxSeen") && config.getBool("baymaxSeen");
                kioPrivateUnlocked = config.has("kioPrivateUnlocked") && config.getBool("kioPrivateUnlocked");
            } catch (Exception e) {
                MyMod.logger.error("[M.E.M.O.] No se pudo leer el progreso", e);
            }
            // Compatibilidad con quienes ya vieron la introduccion antes de este archivo.
            if (!baymaxSeen && UnlockTracker.unlockPref != null
                    && UnlockTracker.unlockPref.getInteger("Poro", 2) == 1) markBaymaxSeen();
        }
        return baymaxSeen;
    }

    public static void markBaymaxSeen() {
        if (!loaded) hasBaymax();
        baymaxSeen = true;
        try {
            if (config == null) config = new SpireConfig("mymod", "memo-progress");
            config.setBool("baymaxSeen", true);
            config.save();
        } catch (Exception e) {
            MyMod.logger.error("[M.E.M.O.] Progreso disponible en esta sesion; no se pudo guardar", e);
        }
    }

    public static boolean hasKioPrivate() {
        hasBaymax();
        return kioPrivateUnlocked;
    }

    public static void unlockKioPrivate() {
        hasBaymax();
        kioPrivateUnlocked = true;
        try {
            if (config == null) config = new SpireConfig("mymod", "memo-progress");
            config.setBool("kioPrivateUnlocked", true);
            config.save();
        } catch (Exception e) {
            MyMod.logger.error("[M.E.M.O.] No se pudo guardar el acceso privado", e);
        }
    }
}

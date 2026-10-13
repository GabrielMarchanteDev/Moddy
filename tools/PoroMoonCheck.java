import java.util.zip.ZipFile;
import mymod.characters.ui.LunarPhase;
import mymod.characters.ui.PoroMoon;

/** Comprueba ciclo, reinicio y recursos empaquetados sin contexto grafico. */
public class PoroMoonCheck {
    public static void main(String[] args) throws Exception {
        PoroMoon moon = new PoroMoon();
        if (moon.getPhase() != LunarPhase.NUEVA) throw new AssertionError("Initial phase");
        LunarPhase[] sequence = {LunarPhase.CRECIENTE, LunarPhase.LLENA, LunarPhase.MENGUANTE, LunarPhase.NUEVA};
        for (int turn = 0; turn < 12; turn++) {
            moon.advance();
            if (moon.getPhase() != sequence[turn % 4]) throw new AssertionError("Turn " + (turn + 1));
        }
        moon.advance();
        moon.reset();
        if (moon.getPhase() != LunarPhase.NUEVA) throw new AssertionError("Reset");
        moon.advance();
        if (moon.getPhase() != LunarPhase.CRECIENTE) throw new AssertionError("New battle turn 1");
        moon.dispose();
        moon.dispose();
        try (ZipFile jar = new ZipFile("target/mymod-1.0-mod.jar")) {
            for (LunarPhase phase : LunarPhase.values()) {
                if (jar.getEntry("images/poro/moon/" + phase.getImageName() + ".png") == null)
                    throw new AssertionError("Missing PNG: " + phase);
            }
        }
        System.out.println("PASS: 12 turns, combat reset, repeated cleanup and all four bundled PNGs");
    }
}

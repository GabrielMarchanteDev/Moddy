import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipFile;
import com.google.gson.Gson;
import mymod.memo.KioPrivateMemory;

/** Comprueba clave externa y contenido sin abrir una partida ni guardar progreso. */
public class KioPrivateMemoryCheck {
    public static void main(String[] args) throws Exception {
        for (String phrase : new String[]{"AUN HAY ALGUIEN", "aunhayalguien", "Aún hay alguien", " AUN-HAY-ALGUIEN "}) {
            if (!KioPrivateMemory.accepts(phrase)) throw new AssertionError(phrase);
        }
        for (String phrase : new String[]{"", "alguien", "AUN HAY ALGUIEN MAS"}) {
            if (KioPrivateMemory.accepts(phrase)) throw new AssertionError("Accepted: " + phrase);
        }
        String json = new String(Files.readAllBytes(Paths.get("src/main/resources/memo/kio-private.json")), StandardCharsets.UTF_8);
        String[][] pages = new Gson().fromJson(json, String[][].class);
        if (pages.length != 6) throw new AssertionError("Page count");
        for (String[] page : pages) {
            if (page.length == 0 || page.length > 6) throw new AssertionError("Page layout");
            for (String line : page) if (line == null || line.isEmpty() || line.length() > 90) throw new AssertionError("Line");
        }
        try (ZipFile jar = new ZipFile("target/mymod-1.0-mod.jar")) {
            for (String name : new String[]{"memo/kio-private.json", "memo/kio-private.png"}) {
                if (jar.getEntry(name) == null) throw new AssertionError(name);
            }
        }
        System.out.println("PASS: correct/incorrect keys, six pages and bundled private memory assets");
    }
}

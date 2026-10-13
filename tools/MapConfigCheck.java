import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import mymod.map.config.KioMapConfig;

/** Standalone checks: no game window or new test dependencies required. */
public class MapConfigCheck {
    private static int checks;

    public static void main(String[] args) throws Exception {
        String json = new String(Files.readAllBytes(Paths.get("config/kio-map.json")), StandardCharsets.UTF_8);
        KioMapConfig config = KioMapConfig.parse(json);
        require(config.nodes.size() == 8 && config.lastRow() == 6, "example topology");
        require(config.hasExplicitFinalBoss(), "explicit final boss");
        require(config.bossLayoutVersion == 0, "old snapshots retain original layout");
        require(KioMapConfig.parse(json.replace("\"enabled\": true", "\"enabled\": true, \"bossLayoutVersion\": 1"))
                .bossLayoutVersion == 1, "new snapshot retains boss layout version");
        require(config.nodes.stream().filter(n -> "boss".equals(n.type)).count() == 3, "three bosses");
        require(config.nodes.get(0).next.size() == 2, "two branches");
        String bundled = new String(Files.readAllBytes(Paths.get("src/main/resources/mymod/maps/kio-map.json")), StandardCharsets.UTF_8);
        require(json.equals(bundled), "editable example matches bundled fallback");
        invalid(json.replace("\"column\": 3", "\"column\": 7"), "column outside grid");
        invalid(json.replace("\"evento\", \"elite\"", "\"missing\", \"elite\""), "missing target");
        invalid(json.replace("\"evento\", \"elite\"", "\"evento\", \"evento\""), "duplicate edge");
        invalid(json.replace("\"evento\", \"elite\"", "\"evento\""), "unreachable elite");
        invalid(json.replace("\"next\": [\"tienda\"]", "\"next\": []"), "dead end");
        invalid(json.replace("\"id\": \"jefe3\", \"row\": 6, \"column\": 3, \"type\": \"boss\"",
                "\"id\": \"jefe3\", \"row\": 6, \"column\": 3, \"type\": \"shop\""), "final boss or rest required");
        invalid(json.replace("mymod:KioEnemy", "missing boss"), "unknown boss");
        invalid(json.replace("\"row\": 6, \"column\": 3", "\"row\": 6, \"column\": 2"), "final boss centered");
        require(!KioMapConfig.parse("{\"nodes\":[{\"id\":\"rest\",\"row\":0,\"column\":3,\"type\":\"rest\",\"next\":[]}]}")
                .hasExplicitFinalBoss(), "legacy automatic boss retained");
        invalid(json.replace("\"type\": \"event\"", "\"type\": \"unknown\""), "unknown room");
        invalid(json.replace("\"Cultist\"", "\"missing encounter\""), "unknown encounter");
        invalid(json.replace("\"id\": \"elite\"", "\"id\": \"evento\""), "duplicate ID");
        invalid("null", "empty configuration");
        require(!KioMapConfig.parse("{\"enabled\":false}").enabled, "disabled configuration");
        require(KioMapConfig.parse(json.replace("\"column\": 2", "\"column\": 1")).enabled,
                "valid repositioning");
        System.out.println("PASS: " + checks + " map configuration checks");
    }

    private static void invalid(String json, String label) {
        try { KioMapConfig.parse(json); }
        catch (IllegalArgumentException expected) { checks++; return; }
        throw new AssertionError("Accepted invalid map: " + label);
    }

    private static void require(boolean condition, String label) {
        if (!condition) throw new AssertionError(label);
        checks++;
    }
}

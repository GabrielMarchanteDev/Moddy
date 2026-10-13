package mymod.map.config;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import actlikeit.dungeons.CustomDungeon;
import mymod.MyMod;
import mymod.characters.MyPlayerKioClassEnum;

/** Editable topology. Game objects are created separately, after validation. */
public class KioMapConfig {
    private static final Gson GSON = new Gson();
    private static final String RESOURCE = "/mymod/maps/kio-map.json";
    // ID de registro, sin inicializar clases de monstruos durante la validacion.
    private static final String BOSS_ENCOUNTER = "mymod:KioEnemy";
    private static boolean loadingSave;
    private static boolean kioRun;

    public boolean enabled = true;
    // Los snapshots antiguos conservan sus coordenadas al continuar la partida.
    public int bossLayoutVersion;
    public List<Node> nodes = new ArrayList<>();

    public static class Node {
        public String id;
        public int row;
        public int column;
        public String type;
        public String encounter;
        public List<String> next = new ArrayList<>();
    }

    // Called before the superclass builds the map, including save reconstruction.
    public static CustomDungeon prepare(CustomDungeon dungeon, AbstractPlayer player, boolean loading) {
        loadingSave = loading;
        kioRun = player != null && player.chosenClass == MyPlayerKioClassEnum.KIO_CLASS;
        MyMod.logger.info("[Mapa Kio] Preparando acto: personaje="
                + (player == null ? "null" : player.getClass().getName())
                + ", mapaKio=" + kioRun + ", guardado=" + loading);
        return dungeon;
    }

    public static boolean isKioRun() {
        return kioRun;
    }

    public static KioMapConfig loadForRun() {
        String runKey = Settings.seed + ":" + AbstractDungeon.actNum;
        try {
            SpireConfig snapshot = new SpireConfig("mymod", "kio-map-snapshot");
            if (loadingSave) {
                if (runKey.equals(snapshot.getString("run")) && snapshot.has("json")) {
                    MyMod.logger.info("[Mapa Kio] Recuperando configuracion de la partida guardada.");
                    return parse(snapshot.getString("json"));
                }
                // Older saves used the procedural map; do not replace their topology.
                MyMod.logger.info("[Mapa Kio] Guardado sin mapa fijo: se conserva el mapa original.");
                return null;
            }
            KioMapConfig config = readEditable();
            snapshot.setString("run", runKey);
            snapshot.setString("json", GSON.toJson(config));
            snapshot.save();
            return config;
        } catch (Exception e) {
            // If a snapshot cannot be saved, do not start an unrecoverable fixed run.
            MyMod.logger.error("[Mapa Kio] No se pudo cargar/guardar la configuracion; mapa original.", e);
            return null;
        }
    }

    private static KioMapConfig readEditable() throws Exception {
        Path path = Paths.get(System.getProperty("mymod.kioMap", "mods/mymod/kio-map.json"));
        if (Files.isRegularFile(path)) {
            try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                KioMapConfig config = GSON.fromJson(reader, KioMapConfig.class);
                config.validate();
                config.bossLayoutVersion = 2;
                MyMod.logger.info("[Mapa Kio] Archivo externo: " + path.toAbsolutePath());
                return config;
            } catch (Exception e) {
                MyMod.logger.error("[Mapa Kio] Archivo externo invalido; usando ejemplo incluido.", e);
            }
        }
        try (InputStream input = KioMapConfig.class.getResourceAsStream(RESOURCE)) {
            if (input == null) throw new IllegalStateException("Falta " + RESOURCE);
            try (Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
                KioMapConfig config = GSON.fromJson(reader, KioMapConfig.class);
                config.validate();
                config.bossLayoutVersion = 2;
                MyMod.logger.info("[Mapa Kio] Usando ejemplo incluido en el JAR.");
                return config;
            }
        }
    }

    public static KioMapConfig parse(String json) {
        KioMapConfig config = GSON.fromJson(json, KioMapConfig.class);
        if (config == null) throw new IllegalArgumentException("Configuracion vacia");
        config.validate();
        return config;
    }

    public int lastRow() {
        int last = 0;
        for (Node node : nodes) last = Math.max(last, node.row);
        return last;
    }

    public boolean hasExplicitFinalBoss() {
        for (Node node : nodes) {
            if (node.row == lastRow() && "boss".equals(node.type)) return true;
        }
        return false;
    }

    public void validate() {
        if (!enabled) return;
        if (nodes == null || nodes.isEmpty()) throw new IllegalArgumentException("Faltan nodos");
        Map<String, Node> byId = new HashMap<>();
        Set<String> positions = new HashSet<>();
        for (Node node : nodes) {
            if (node == null || node.id == null || node.id.trim().isEmpty()
                    || byId.put(node.id, node) != null) {
                throw new IllegalArgumentException("Identificador vacio o repetido");
            }
            if (node.row < 0 || node.row > 13 || node.column < 0 || node.column > 6
                    || !positions.add(node.row + ":" + node.column)) {
                throw new IllegalArgumentException("Posicion invalida o repetida: " + node.id);
            }
            if (!"combat".equals(node.type) && !"elite".equals(node.type)
                    && !"event".equals(node.type) && !"shop".equals(node.type)
                    && !"rest".equals(node.type) && !"treasure".equals(node.type)
                    && !"boss".equals(node.type)) {
                throw new IllegalArgumentException("Tipo desconocido: " + node.type);
            }
            if ("boss".equals(node.type) && !BOSS_ENCOUNTER.equals(node.encounter)) {
                throw new IllegalArgumentException("Jefe permitido: " + BOSS_ENCOUNTER);
            }
            // This first example offers two known vanilla encounters, no arbitrary factory names.
            if ("combat".equals(node.type) || "elite".equals(node.type)) {
                if (!"Cultist".equals(node.encounter) && !"3 Sentries".equals(node.encounter)) {
                    throw new IllegalArgumentException("Encuentro permitido: Cultist o 3 Sentries");
                }
            }
            if (node.next == null) throw new IllegalArgumentException("Faltan conexiones: " + node.id);
        }
        int last = lastRow();
        Set<String> reached = new HashSet<>();
        for (int row = 0; row <= last; row++) {
            boolean found = false;
            for (Node node : nodes) {
                if (node.row != row) continue;
                found = true;
                if (row == 0) reached.add(node.id);
                if (!reached.contains(node.id)) throw new IllegalArgumentException("Nodo inaccesible: " + node.id);
                if (row == last) {
                    if ((!"rest".equals(node.type) && !"boss".equals(node.type)) || !node.next.isEmpty()) {
                        throw new IllegalArgumentException("La ultima fila debe ser descanso o jefe sin conexiones");
                    }
                    if (hasExplicitFinalBoss() && (nodes.stream().filter(n -> n.row == last).count() != 1
                            || node.column != 3)) {
                        throw new IllegalArgumentException("El jefe final debe estar solo y centrado");
                    }
                } else if (node.next.isEmpty()) {
                    throw new IllegalArgumentException("Ruta sin salida: " + node.id);
                }
                Set<String> unique = new HashSet<>();
                for (String targetId : node.next) {
                    Node target = byId.get(targetId);
                    if (target == null || target.row != row + 1 || !unique.add(targetId)) {
                        throw new IllegalArgumentException("Conexion invalida: " + node.id + " -> " + targetId);
                    }
                    reached.add(targetId);
                }
            }
            if (!found) throw new IllegalArgumentException("Fila vacia: " + row);
        }
    }
}

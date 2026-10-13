package mymod.map.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.map.DungeonMap;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.rooms.TreasureRoom;

import mymod.events.MapTestEvent;

public final class KioMapBuilder {
    private static boolean finalBossOutside;
    private KioMapBuilder() { }

    public static boolean isFinalBossOutside() { return finalBossOutside; }

    public static ArrayList<ArrayList<MapRoomNode>> build(KioMapConfig config) {
        config.validate();
        finalBossOutside = config.bossLayoutVersion >= 2;
        boolean explicitBoss = config.hasExplicitFinalBoss();
        Map<Integer, Integer> visualRows = new HashMap<>();
        int padding = 0;
        for (int row = 0; row <= config.lastRow(); row++) {
            for (KioMapConfig.Node node : config.nodes) {
                if (node.row == row && "boss".equals(node.type)) {
                    // Iconos de 512 px: cuatro espacios de 150 px entre jefes.
                    if (row > 0 && config.bossLayoutVersion >= 1) padding += 3;
                    break;
                }
            }
            visualRows.put(row, row + padding);
        }
        int bossRow = visualRows.get(config.lastRow()) + (explicitBoss ? 0 : 1);
        ArrayList<ArrayList<MapRoomNode>> result = new ArrayList<>();
        for (int y = 0; y <= bossRow; y++) {
            ArrayList<MapRoomNode> row = new ArrayList<>();
            for (int x = 0; x < 7; x++) {
                MapRoomNode node = new MapRoomNode(x, y);
                node.offsetX = 0;
                node.offsetY = 0;
                row.add(node);
            }
            result.add(row);
        }
        Map<String, MapRoomNode> byId = new HashMap<>();
        for (KioMapConfig.Node definition : config.nodes) {
            int visualRow = visualRows.get(definition.row);
            boolean finalBoss = "boss".equals(definition.type) && definition.row == config.lastRow();
            MapRoomNode node = result.get(visualRow).get(definition.column);
            if ("boss".equals(definition.type) && !finalBoss && config.bossLayoutVersion >= 1) {
                node = new ContinuingBossNode(definition.column, visualRow);
                result.get(visualRow).set(definition.column, node);
            }
            node.setRoom(finalBoss
                    ? new MonsterRoomBoss() : createRoom(definition));
            byId.put(definition.id, node);
        }
        // ActLikeIt's boss button detects a centered boss room in the next row.
        MapRoomNode boss = result.get(bossRow).get(3);
        if (!explicitBoss) boss.setRoom(new MonsterRoomBoss());
        for (KioMapConfig.Node definition : config.nodes) {
            MapRoomNode source = byId.get(definition.id);
            for (String target : definition.next) connect(source, byId.get(target));
            if (!explicitBoss && definition.row == config.lastRow()) connect(source, boss);
        }
        return result;
    }

    // Apariencia del jefe final con seleccion y conexiones de un nodo normal.
    private static class ContinuingBossNode extends MapRoomNode {
        ContinuingBossNode(int x, int y) {
            super(x, y);
            offsetX = 0;
            offsetY = 0;
            hb.resize(iconSize(), iconSize());
        }
        private float iconSize() {
            return (Settings.isMobile ? 560F : 512F) * Settings.scale;
        }
        @Override public void render(SpriteBatch sb) {
            if (DungeonMap.boss == null || DungeonMap.bossOutline == null) {
                super.render(sb);
                return;
            }
            for (MapEdge edge : getEdges()) edge.render(sb);
            float size = iconSize();
            float left = hb.cX - size / 2F;
            float bottom = hb.cY - size / 2F;
            Color previous = new Color(sb.getColor());
            sb.setColor(1F, 1F, 1F, color.a);
            sb.draw(DungeonMap.bossOutline, left, bottom, size, size);
            sb.setColor(taken ? AVAILABLE_COLOR : color);
            sb.draw(DungeonMap.boss, left, bottom, size, size);
            hb.render(sb);
            sb.setColor(previous);
        }
    }

    private static void connect(MapRoomNode from, MapRoomNode to) {
        from.addEdge(new MapEdge(from.x, from.y, from.offsetX, from.offsetY,
                to.x, to.y, to.offsetX, to.offsetY, false));
        to.addParent(from);
    }

    private static AbstractRoom createRoom(KioMapConfig.Node node) {
        switch (node.type) {
            case "combat": return new FixedCombatRoom(node.encounter);
            case "elite": return new FixedEliteRoom(node.encounter);
            case "boss": return new ContinuingBossRoom(node.encounter);
            case "event": return new FixedEventRoom();
            case "shop": return new ShopRoom();
            case "rest": return new RestRoom();
            case "treasure": return new TreasureRoom();
            default: throw new IllegalArgumentException(node.type);
        }
    }

    // No hereda de MonsterRoomBoss: esa clase activa la salida de fin de acto.
    private static class ContinuingBossRoom extends FixedCombatRoom {
        ContinuingBossRoom(String encounter) {
            super(encounter);
            mapSymbol = "B";
            mapImg = ImageMaster.MAP_NODE_ELITE;
            mapImgOutline = ImageMaster.MAP_NODE_ELITE_OUTLINE;
        }
        @Override public CardRarity getCardRarity(int roll) { return CardRarity.RARE; }
    }

    private static class FixedCombatRoom extends MonsterRoom {
        private final String encounter;
        FixedCombatRoom(String encounter) { this.encounter = encounter; }
        @Override public void onPlayerEntry() {
            monsters = MonsterHelper.getEncounter(encounter);
            monsters.init();
            super.onPlayerEntry();
        }
    }

    private static class FixedEliteRoom extends MonsterRoomElite {
        private final String encounter;
        FixedEliteRoom(String encounter) { this.encounter = encounter; }
        @Override public void onPlayerEntry() {
            monsters = MonsterHelper.getEncounter(encounter);
            monsters.init();
            super.onPlayerEntry();
        }
    }

    private static class FixedEventRoom extends EventRoom {
        @Override public void onPlayerEntry() {
            AbstractDungeon.overlayMenu.proceedButton.hide();
            event = new MapTestEvent();
            event.onEnterRoom();
        }
    }
}

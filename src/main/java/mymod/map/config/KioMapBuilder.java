package mymod.map.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
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
    private KioMapBuilder() { }

    public static ArrayList<ArrayList<MapRoomNode>> build(KioMapConfig config) {
        config.validate();
        int bossRow = config.lastRow() + 1;
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
            MapRoomNode node = result.get(definition.row).get(definition.column);
            node.setRoom(createRoom(definition));
            byId.put(definition.id, node);
        }
        // ActLikeIt's boss button detects a centered boss room in the next row.
        MapRoomNode boss = result.get(bossRow).get(3);
        boss.setRoom(new MonsterRoomBoss());
        for (KioMapConfig.Node definition : config.nodes) {
            MapRoomNode source = byId.get(definition.id);
            for (String target : definition.next) connect(source, byId.get(target));
            if (definition.row == config.lastRow()) connect(source, boss);
        }
        return result;
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
            case "event": return new FixedEventRoom();
            case "shop": return new ShopRoom();
            case "rest": return new RestRoom();
            case "treasure": return new TreasureRoom();
            default: throw new IllegalArgumentException(node.type);
        }
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

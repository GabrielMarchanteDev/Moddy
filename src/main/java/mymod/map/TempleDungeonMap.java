package mymod.map;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import mymod.characters.Kio;
import mymod.characters.Poro;
import mymod.characters.Baymax;
import mymod.MyMod;

/** Las puertas representan exactamente las conexiones del mapa existente. */
public final class TempleDungeonMap {
    public static boolean ENABLED = false;
    private static final TempleDungeonMap INSTANCE = new TempleDungeonMap();
    private final Hitbox[] doors = {new Hitbox(1, 1), new Hitbox(1, 1), new Hitbox(1, 1)};
    private final Hitbox more = new Hitbox(280F * Settings.scale, 55F * Settings.scale);
    private final Hitbox back = new Hitbox(260F * Settings.scale, 55F * Settings.scale);
    private Texture hall;
    private boolean imageFailed;
    private int page;
    private int focus;
    private MapRoomNode lastCurrent;

    public static TempleDungeonMap get() { return INSTANCE; }
    public static boolean active() {
        return ENABLED && AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP
                && AbstractDungeon.map != null
                && (AbstractDungeon.player instanceof Kio || AbstractDungeon.player instanceof Poro
                    || AbstractDungeon.player instanceof Baymax);
    }

    public static ArrayList<MapRoomNode> available(ArrayList<ArrayList<MapRoomNode>> map,
            MapRoomNode current, boolean firstChosen, boolean flight) {
        ArrayList<MapRoomNode> result = new ArrayList<>();
        if (map == null) return result;
        for (ArrayList<MapRoomNode> row : map) for (MapRoomNode node : row) {
            if (node == null || node.getRoom() == null) continue;
            boolean allowed = !firstChosen ? node.y == 0 && node.hasEdges()
                    : current != null && (current.isConnectedTo(node)
                        || (flight && current.wingedIsConnectedTo(node)));
            if (allowed) result.add(node);
        }
        // El jefe vanilla se encuentra fuera de la matriz del mapa.
        if (firstChosen && current != null && result.isEmpty()
                && !(current.getRoom() instanceof MonsterRoomBoss)
                && current.y == map.size() - 1) {
            MapRoomNode boss = new MapRoomNode(-1, 15);
            boss.setRoom(new MonsterRoomBoss());
            result.add(boss);
        }
        return result;
    }

    private ArrayList<MapRoomNode> routes() {
        MapRoomNode current = AbstractDungeon.getCurrMapNode();
        if (current != lastCurrent) { lastCurrent = current; page = 0; focus = 0; }
        boolean flight = AbstractDungeon.player.hasRelic("WingedGreaves")
                && AbstractDungeon.player.getRelic("WingedGreaves").counter > 0;
        ArrayList<MapRoomNode> options = available(AbstractDungeon.map, current, AbstractDungeon.firstRoomChosen, flight);
        if (page * 3 >= options.size()) page = 0;
        return options;
    }

    private boolean canEnter() {
        return !AbstractDungeon.waitingOnFadeOut && !AbstractDungeon.isDungeonBeaten
                && AbstractDungeon.getCurrMapNode() != null
                && AbstractDungeon.getCurrRoom() != null
                && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMPLETE;
    }

    private boolean clicked(Hitbox hb) {
        if (!hb.hovered || !InputHelper.justClickedLeft) return false;
        InputHelper.justClickedLeft = false;
        return true;
    }

    public void update(DungeonMapScreen screen) {
        ArrayList<MapRoomNode> options = routes();
        for (int i = 0; i < 3; i++) {
            doors[i].resize(0.22F * Settings.WIDTH, 0.48F * Settings.HEIGHT);
            doors[i].move((0.17F + i * 0.33F) * Settings.WIDTH, 0.51F * Settings.HEIGHT);
            doors[i].update();
        }
        more.move(Settings.WIDTH * 0.66F, 100F * Settings.scale);
        back.move(Settings.WIDTH * 0.34F, 100F * Settings.scale);
        more.update(); back.update();
        if ((InputHelper.pressedEscape || clicked(back)) && screen.dismissable) {
            InputHelper.pressedEscape = false;
            screen.close(); return;
        }
        InputHelper.pressedEscape = false;
        if (options.size() > 3 && clicked(more)) { page = (page + 1) % ((options.size() + 2) / 3); focus = 0; }
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) focus = (focus + 1) % Math.max(1, Math.min(3, options.size() - page * 3));
        if (!canEnter()) return;
        for (int i = 0; i < 3 && page * 3 + i < options.size(); i++) {
            if (clicked(doors[i]) || (i == focus && Gdx.input.isKeyJustPressed(Input.Keys.ENTER))) {
                enter(options.get(page * 3 + i), screen); return;
            }
        }
    }

    private void enter(MapRoomNode node, DungeonMapScreen screen) {
        MapRoomNode current = AbstractDungeon.getCurrMapNode();
        if (AbstractDungeon.firstRoomChosen && current != null) {
            current.taken = true;
            MapEdge edge = current.getEdgeConnectedTo(node);
            if (edge != null) edge.markAsTaken();
            else if (node.x >= 0 && AbstractDungeon.player.hasRelic("WingedGreaves")) {
                com.megacrit.cardcrawl.relics.AbstractRelic wings = AbstractDungeon.player.getRelic("WingedGreaves");
                wings.flash(); wings.counter--;
                if (wings.counter == 0) wings.usedUp();
            }
            if (node.x < 0) for (MapEdge link : current.getEdges()) if (link != null) link.markAsTaken();
        }
        AbstractDungeon.firstRoomChosen = true;
        screen.dismissable = true; screen.clicked = false; screen.clickTimer = 0F;
        AbstractDungeon.nextRoom = node;
        AbstractDungeon.pathX.add(node.x < 0 && current != null ? current.x : node.x);
        AbstractDungeon.pathY.add(node.y);
        if (node.x >= 0) CardCrawlGame.metricData.path_taken.add(node.getRoom().getMapSymbol());
        CardCrawlGame.music.fadeOutTempBGM();
        AbstractDungeon.nextRoomTransitionStart();
    }

    public static String label(AbstractRoom room) {
        if (room instanceof MonsterRoomBoss) return "Umbral del guardián";
        if (room instanceof MonsterRoomElite) return "Presencia peligrosa";
        if (room instanceof MonsterRoom) return "Recuerdo en conflicto";
        if (room instanceof RestRoom) return "Lugar de descanso";
        if (room instanceof ShopRoom) return "Estancia de intercambio";
        if (room instanceof TreasureRoom) return "Objeto conservado";
        if (room instanceof EventRoom) return "Encuentro del templo";
        return "Estancia del templo";
    }

    public void render(SpriteBatch sb, DungeonMapScreen screen) {
        Color old = sb.getColor().cpy();
        if (hall == null && !imageFailed) {
            try {
                hall = new Texture(Gdx.files.internal("memo/temple-hall.png"));
                hall.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            } catch (RuntimeException e) { imageFailed = true; MyMod.logger.error("[Templo] Fondo no disponible", e); }
        }
        sb.setColor(Color.WHITE);
        if (hall != null) sb.draw(hall, 0, 0, Settings.WIDTH, Settings.HEIGHT);
        ArrayList<MapRoomNode> options = routes();
        Color ink = new Color(0.91F, 0.86F, 0.76F, 1F);
        for (int i = 0; i < 3; i++) {
            Hitbox door = doors[i];
            int index = page * 3 + i;
            boolean exists = index < options.size();
            boolean chosen = exists && canEnter() && (door.hovered || i == focus);
            sb.setColor(chosen ? new Color(0.85F, 0.77F, 0.55F, 0.14F) : new Color(0F, 0F, 0F, exists ? 0.12F : 0.65F));
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, door.x, door.y, door.width, door.height);
            sb.setColor(0.025F, 0.03F, 0.04F, 0.88F);
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, door.x, Settings.HEIGHT * 0.28F, door.width, 60F * Settings.scale);
            FontHelper.renderFontCentered(sb, FontHelper.tipBodyFont, exists ? label(options.get(index).getRoom()) : "Paso cerrado",
                    door.cX, Settings.HEIGHT * 0.28F + 30F * Settings.scale, ink);
            if (exists && options.get(index).hasEmeraldKey) FontHelper.renderFontCentered(sb, FontHelper.tipBodyFont,
                    "Llave esmeralda", door.cX, Settings.HEIGHT * 0.28F - 25F * Settings.scale, Settings.GREEN_TEXT_COLOR);
        }
        sb.setColor(0.02F, 0.025F, 0.03F, 0.85F);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0, 0, Settings.WIDTH, 165F * Settings.scale);
        FontHelper.renderFontCentered(sb, FontHelper.tipBodyFont, canEnter()
                ? "Elige una estancia · Tab y Enter o ratón" : "Puedes observar el templo. Termina la sala actual para avanzar.",
                Settings.WIDTH / 2F, 145F * Settings.scale, ink);
        if (screen.dismissable) FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, "Volver", back.cX, back.cY, ink);
        if (options.size() > 3) FontHelper.renderFontCentered(sb, FontHelper.tipBodyFont,
                "Más pasos · " + (page + 1) + "/" + ((options.size() + 2) / 3), more.cX, more.cY, ink);
        sb.setColor(old);
    }
}

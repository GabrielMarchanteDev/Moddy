package mymod.dungeon;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;

import actlikeit.dungeons.CustomDungeon;
import mymod.MyMod;
import mymod.events.BaymaxCheckEvent;
import mymod.monsters.Demon;
import mymod.monsters.Demon2;
import mymod.monsters.Demon3;
import mymod.monsters.KioEnemy;

public class Kanto extends AbstractPokemonRegionDungeon {

    public static String ID = MyMod.makeID(Kanto.class.getSimpleName());
    
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
	public static final Logger logger = LogManager.getLogger(MyMod.class.getName());
	
    public static final String[] TEXT = uiStrings.TEXT;
    public static final String NAME = TEXT[0];

    public Kanto() {
        super(NAME, ID, "images/ui/event/panel.png", true, 3, 12, 10);
        logger.error(uiStrings);
        this.setMainMusic("audio/music/NoLonger.ogg");
        this.onEnterEvent(BaymaxCheckEvent.class);
        
    }

    public Kanto(CustomDungeon cd, AbstractPlayer p, ArrayList<String> emptyList) {
        super(cd, p, emptyList);
    }

    public Kanto(CustomDungeon cd, AbstractPlayer p, SaveFile saveFile) {
        super(cd, p, saveFile);
    }

    protected void initializeLevelSpecificChances() {
        shopRoomChance = 0.05F;
        restRoomChance = 0.12F;
        treasureRoomChance = 0.0F;
        eventRoomChance = 0.22F;
        eliteRoomChance = 0.08F;
        smallChestChance = 50;
        mediumChestChance = 33;
        largeChestChance = 17;
        commonRelicChance = 50;
        uncommonRelicChance = 33;
        rareRelicChance = 17;
        colorlessRareChance = 0.3F;
        cardUpgradedChance = 0.0F;
    }

    @Override
    public String getBodyText() {
        return TEXT[2];
    }

    @Override
    public String getOptionText() {
        return TEXT[3];
    }

    @Override
    protected void generateMonsters() {
        generateWeakEnemies(weakpreset);
        generateStrongEnemies(strongpreset);
        generateElites(elitepreset);
    }
    
//    @Override
//    public void makeMap() {
//        logger.info(">>> Generando mapa de KioDungeon...");
//
//        map = new ArrayList<>();
//        int rows = 5; // aumentamos a 5 para incluir sala de victoria
//
//        for (int y = 0; y < rows; y++) {
//            ArrayList<MapRoomNode> row = new ArrayList<>();
//            for (int x = 0; x < 7; x++) {
//                MapRoomNode node = new MapRoomNode(x, y);
//                if (x == 3) {
//                    if (y == 0) {
//                        node.room = new MonsterRoom(); // Inicio
//                    } else if (y == rows - 2) {
//                        node.room = new MonsterRoomBoss(); // Jefe final
//                    } else if (y == rows - 1) {
//                        node.room = new VictoryRoom(EventType.HEART); // Fin
//                    } else {
//                        node.room = new MonsterRoom(); // Intermedios
//                    }
//                }
//                row.add(node);
//            }
//            map.add(row);
//        }
//
//        // Conectar nodos del centro verticalmente (columna 3)
//        for (int y = 0; y < rows - 1; y++) {
//            MapRoomNode current = map.get(y).get(3);
//            MapRoomNode next = map.get(y + 1).get(3);
//            current.addEdge(new MapEdge(current.x, current.y, next.x, next.y));
//        }
//
//        logger.info(">>> Mapa generado con " + map.size() + " filas");
//       
//    }

    @Override
    protected void generateWeakEnemies(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo(KioEnemy.ID, 2.0F));
        monsters.add(new MonsterInfo(Demon.ID, 2.0F));
        monsters.add(new MonsterInfo(Demon2.ID, 2.0F));
        monsters.add(new MonsterInfo(Demon3.ID, 2.0F));
        MonsterInfo.normalizeWeights(monsters);
        this.populateMonsterList(monsters, count, false);
    }

    @Override
    protected void generateStrongEnemies(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo(KioEnemy.ID, 2.0F));
        //monsters.add(new MonsterInfo("Gremlin Gang", 1.0F));
        //monsters.add(new MonsterInfo("Looter", 2.0F));
        monsters.add(new MonsterInfo(Demon.ID, 2.0F));
        //monsters.add(new MonsterInfo("Lots of Slimes", 1.0F));
        monsters.add(new MonsterInfo(Demon2.ID, 1.5F));
        monsters.add(new MonsterInfo(Demon3.ID, 1.5F));
        //monsters.add(new MonsterInfo("3 Louse", 2.0F));
        //monsters.add(new MonsterInfo("2 Fungi Beasts", 2.0F)); // 2 tentacruel maybe
        MonsterInfo.normalizeWeights(monsters);
        this.populateFirstStrongEnemy(monsters, this.generateExclusions());
        this.populateMonsterList(monsters, count, false);
    }

    @Override
    protected void generateElites(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo(KioEnemy.ID, 1.0F));
        monsters.add(new MonsterInfo(Demon.ID, 1.0F));
        monsters.add(new MonsterInfo(Demon2.ID, 1.0F));
        MonsterInfo.normalizeWeights(monsters);
        this.populateMonsterList(monsters, count, true);
    }
    @Override
    public String getAfterSelectText() {
        return "Sientes una mirada tras de ti.";
    }

    @Override
    public String getActNumberText() {
        return "Acto K";
    }

  
}
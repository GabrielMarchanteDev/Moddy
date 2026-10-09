//package mymod.map;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.core.CardCrawlGame;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.helpers.RelicLibrary;
//import com.megacrit.cardcrawl.localization.UIStrings;
//import com.megacrit.cardcrawl.map.MapEdge;
//import com.megacrit.cardcrawl.map.MapGenerator;
//import com.megacrit.cardcrawl.map.MapRoomNode;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import com.megacrit.cardcrawl.monsters.MonsterInfo;
//import com.megacrit.cardcrawl.powers.AbstractPower;
//import com.megacrit.cardcrawl.relics.AbstractRelic;
//import com.megacrit.cardcrawl.relics.Circlet;
//import com.megacrit.cardcrawl.rewards.RewardItem;
//import com.megacrit.cardcrawl.rooms.AbstractRoom;
//import com.megacrit.cardcrawl.rooms.EventRoom;
//import com.megacrit.cardcrawl.rooms.MonsterRoom;
//import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
//import com.megacrit.cardcrawl.rooms.RestRoom;
//import com.megacrit.cardcrawl.rooms.ShopRoom;
//import com.megacrit.cardcrawl.rooms.TreasureRoom;
//import com.megacrit.cardcrawl.rooms.VictoryRoom;
//import com.megacrit.cardcrawl.rooms.VictoryRoom.EventType;
//import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
//import com.megacrit.cardcrawl.scenes.AbstractScene;
//import com.megacrit.cardcrawl.scenes.TheBottomScene;
//import com.megacrit.cardcrawl.scenes.TheCityScene;
//
//import actlikeit.dungeons.CustomDungeon;
//import basemod.BaseMod;
//import mymod.MyMod;
//import mymod.events.MyFirstEvent;
//import mymod.helpers.PaleRewardItem;
//import mymod.monsters.KioEnemy;
//import mymod.rooms.FixedMonsterRoom;
//
//public class KioDungeon2 extends CustomDungeon {
//    public static final String ID = "mymod:KioDungeon";
//    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
//    public static final String[] TEXT = uiStrings.TEXT;
//    public static final String NAME = TEXT[0];
//
//    public KioDungeon2() {
//    	
//    	super(NAME, ID);
//        logger.info(">>> Entrando a constructor de KioDungeon");
//     //   this.addTempMusic("NoLonger","audio/music/NoLonger.ogg");
//       // this.setMainMusic("audio/music/NoLonger.ogg");
//       this.onEnterEvent(MyFirstEvent.class); // Iniciar con un evento
//       
//    }
//
//    public KioDungeon2(CustomDungeon cd, AbstractPlayer p, ArrayList<String> emptyList) {
//        super(cd, p, emptyList);
//    }
//
//    public KioDungeon2(CustomDungeon cd, AbstractPlayer p, SaveFile sf) {
//        super(cd, p, sf);
//    }
//
//    @Override
//    public AbstractScene DungeonScene() {
//        logger.info(">>> ENTRANDO EN KIOSCENE");
//        return new TheBottomScene(); // usa escena vanilla por ahora
//    }
//    /*IMPORTANTE DEFINE LAS OPCIONES INICIALES PARA PICKEAR
//     *     @Override
//    public String getAfterSelectText() {
//        return forkStrings.DESCRIPTIONS[forkStrings.DESCRIPTIONS.length - 1];
//    }
//    @Override
//    public String getOptionText() {
//        return forkStrings.OPTIONS[0];
//    }
//    @Override
//    public String getBodyText() {
//        return forkStrings.DESCRIPTIONS[forkStrings.DESCRIPTIONS.length - 2].replace(" NL", "");
//    }
//     */
//    
//    /* IMPORTANTE DEFINE EL FONDO DE TU INTERFAZ DE EVENTOS
//     * 
//    @Override
//    protected void initializeEventImg() {
//        if (eventBackgroundImg != null) {
//            eventBackgroundImg.dispose();
//            eventBackgroundImg = null;
//        }
//        eventBackgroundImg = ImageMaster.loadImage("images/ui/event/panel.png");
//    }
//
//     */
//    @Override
//    protected void initializeLevelSpecificChances() {
//        shopRoomChance = 0.05F;
//        restRoomChance = 0.12F;
//        treasureRoomChance = 0.0F;
//        eventRoomChance = 0.22F;
//        eliteRoomChance = 0.08F;
//        smallChestChance = 50;
//        mediumChestChance = 33;
//        largeChestChance = 17;
//        commonRelicChance = 50;
//        uncommonRelicChance = 33;
//        rareRelicChance = 17;
//        colorlessRareChance = 0.3F;
//        if (AbstractDungeon.ascensionLevel >= 12) {
//            cardUpgradedChance = 0.25F;
//        } else {
//            cardUpgradedChance = 0.5F;
//        }
//
//    }//PUEDES AÑADIR TUS SHRINE PERSONALIZADOS
//    @Override
//    protected void initializeShrineList() {
//        shrineList.add("Golden Shrine");
//        shrineList.add("Transmorgrifier");
//        shrineList.add("Purifier");
//        shrineList.add("Upgrade Shrine");
//    }
//    
//    
////    @Override
////    protected void makeMap() {
////
////        ArrayList<MonsterRoomCreator> toughEncounters = new ArrayList();
////        ArrayList<MonsterRoomCreator> easyishEncounters = new ArrayList();
////
////        easyishEncounters.add(new MonsterRoomCreator(MyMod.assetPath("images/ui/map/wokeone.png"), MyMod.assetPath("images/ui/map/wokeoneOutline.png"), KioEnemy.ID));
////        easyishEncounters.add(new MonsterRoomCreator(MyMod.assetPath("images/ui/map/guardian.png"), MyMod.assetPath("images/ui/map/guardianOutline.png"), KioEnemy.ID));
////        easyishEncounters.add(new MonsterRoomCreator(MyMod.assetPath("images/ui/map/collector.png"), MyMod.assetPath("images/ui/map/collectorOutline.png"), KioEnemy.ID));
////        easyishEncounters.add(new MonsterRoomCreator(MyMod.assetPath("images/ui/map/ironcluck.png"), MyMod.assetPath("images/ui/map/ironcluckOutline.png"), KioEnemy.ID));
////        easyishEncounters.add(new MonsterRoomCreator(MyMod.assetPath("images/ui/map/donudeca.png"), MyMod.assetPath("images/ui/map/donudecaOutline.png"), KioEnemy.ID));
////        easyishEncounters.add(new MonsterRoomCreator(MyMod.assetPath("images/ui/map/bard.png"), MyMod.assetPath("images/ui/map/bardOutline.png"), KioEnemy.ID));
////        easyishEncounters.add(new MonsterRoomCreator(MyMod.assetPath("images/ui/map/showman.png"), MyMod.assetPath("images/ui/map/showmanOutline.png"), KioEnemy.ID));
////        easyishEncounters.add(new MonsterRoomCreator(MyMod.assetPath("images/ui/map/silent.png"), MyMod.assetPath("images/ui/map/silentOutline.png"), KioEnemy.ID));
////        easyishEncounters.add(new MonsterRoomCreator(MyMod.assetPath("images/ui/map/watcher.png"), MyMod.assetPath("images/ui/map/watcherOutline.png"), KioEnemy.ID));
////        easyishEncounters.add(new MonsterRoomCreator(MyMod.assetPath("images/ui/map/timeeater.png"), MyMod.assetPath("images/ui/map/timeeaterOutline.png"), KioEnemy.ID));
////
////   
////        toughEncounters.add(new MonsterRoomCreator(MyMod.assetPath("images/ui/map/defect.png"), MyMod.assetPath("images/ui/map/defectOutline.png"), KioEnemy.ID));
////        //toughEncounters.add(new MonsterRoomCreator(MyMod.assetPath("images/ui/map/slime.png"), MyMod.assetPath("images/ui/map/slimeOutline.png"), SlimeBossest.ID));
////        toughEncounters.add(new MonsterRoomCreator(MyMod.assetPath("images/ui/map/hexaghost.png"), MyMod.assetPath("images/ui/map/hexaghostOutline.png"), KioEnemy.ID));
////        toughEncounters.add(new MonsterRoomCreator(MyMod.assetPath("images/ui/map/vixen.png"), MyMod.assetPath("images/ui/map/vixenOutline.png"), KioEnemy.ID));
////        toughEncounters.add(new MonsterRoomCreator(MyMod.assetPath("images/ui/map/reimu.png"), MyMod.assetPath("images/ui/map/reimuOutline.png"), KioEnemy.ID));
////
////        map = new ArrayList();
////
////        int index = 0;
////        map.add(populate(easyishEncounters, index++));
////        map.add(populate(easyishEncounters, index++));
////        map.add(singleNodeArea(new EventRoom(), index++));
////        map.add(doubleNodeArea(new TreasureRoom(), new ShopRoom(), index++));
////        map.add(singleNodeArea(new RestRoom(), index++));
////
////        //map.add(populate(easyishEncounters, index++));
////        map.add(populate(toughEncounters, index++));
////
////        map.add(singleNodeArea(new RestRoom(), index++));
////     //   map.add(doubleNodeArea(new PreloadTreasureRoom(), new PreloadShopRoom(), index++));
////      //  map.add(singleNodeArea(new DejaVuRoom(), index++));
////        map.add(singleNodeArea(new MonsterRoomBoss(), index++));
////     //   map.add(singleNodeArea(new PaleVictoryRoom(), index++, false));
////
////        logger.info("Generated the following dungeon map:");
////        logger.info(MapGenerator.toString(map, true));
////
////        firstRoomChosen = false;
////        fadeIn();
////    }
////    
////
////    @Override
////    public void Ending() {
////        CardCrawlGame.music.fadeOutBGM();
////        MapRoomNode node = new MapRoomNode(3, 4);
////        node.room = new VictoryRoom(EventType.HEART);
////        AbstractDungeon.nextRoom = node;
////        AbstractDungeon.closeCurrentScreen();
////        AbstractDungeon.nextRoomTransitionStart();
////    }
////    
////
////    private void connectNode(MapRoomNode src, MapRoomNode dst) {
////        src.addEdge(new MapEdge(src.x, src.y, src.offsetX, src.offsetY, dst.x, dst.y, dst.offsetX, dst.offsetY, false));
////    }
////    
////    
////    private ArrayList<MapRoomNode> populate(ArrayList<MonsterRoomCreator> possibilities, int index) {
////        ArrayList<MapRoomNode> result = new ArrayList<>();
////        for (int i = 0; i < 7; i++) {
////            MapRoomNode mrn = new MapRoomNode(i, index);
////            if (i % 2 == 1) {
////                int rnd = AbstractDungeon.mapRng.random(possibilities.size() - 1);
////                mrn.room = possibilities.get(rnd).get();
////                possibilities.remove(rnd);
////            }
////            result.add(mrn);
////        }
////
////        if (index > 0) {
////            ArrayList<MapRoomNode> mapcontent = map.get(index - 1);
////            for (int i = 0; i < mapcontent.size(); i++) {
////                if (mapcontent.get(i).room != null) {
////                    for (int j = 0; j < result.size(); j++) {
////                        if (result.get(j).room != null) {
////                            if (Math.abs(i - j) < 4) {
////                                this.connectNode(mapcontent.get(i), result.get(j));
////                            }
////                        }
////                    }
////                }
////            }
////        }
////
////        return result;
////    }
//    
////    
////    private ArrayList<MapRoomNode> doubleNodeArea(AbstractRoom roomOne, AbstractRoom roomTwo, int index) {
////        ArrayList<MapRoomNode> result = new ArrayList<>();
////        MapRoomNode mrn;
////        result.add(new MapRoomNode(0, index));
////        result.add(new MapRoomNode(1, index));
////        mrn = new MapRoomNode(2, index);
////        mrn.room = roomOne;
////        result.add(mrn);
////        result.add(new MapRoomNode(3, index));
////        mrn = new MapRoomNode(4, index);
////        mrn.room = roomTwo;
////        result.add(mrn);
////        result.add(new MapRoomNode(5, index));
////        result.add(new MapRoomNode(6, index));
////
////        linkNonMonsterAreas(result);
////
////        return result;
////    }
////    
////    
////    private ArrayList<MapRoomNode> singleNodeArea(AbstractRoom room, int index) {
////        return singleNodeArea(room, index, true);
////    }
////    
////
////    private ArrayList<MapRoomNode> singleNodeArea(AbstractRoom room, int index, boolean connected) {
////        ArrayList<MapRoomNode> result = new ArrayList<>();
////        MapRoomNode mrn;
////        result.add(new MapRoomNode(0, index));
////        result.add(new MapRoomNode(1, index));
////        result.add(new MapRoomNode(2, index));
////        mrn = new MapRoomNode(3, index);
////        mrn.room = room;
////        result.add(mrn);
////        result.add(new MapRoomNode(4, index));
////        result.add(new MapRoomNode(5, index));
////        result.add(new MapRoomNode(6, index));
////
////        if (connected) {
////            linkNonMonsterAreas(result);
////        }
////
////        return result;
////    }
////    
////    
////    private void linkNonMonsterAreas(ArrayList<MapRoomNode> result) {
////        if (!map.isEmpty()) {
////            ArrayList<MapRoomNode> mapcontent = map.get(map.size() - 1);
////            for (int i = 0; i < mapcontent.size(); i++) {
////                if (mapcontent.get(i).room != null) {
////                    for (int j = 0; j < result.size(); j++) {
////                        if (result.get(j).room != null) {
////                            this.connectNode(mapcontent.get(i), result.get(j));
////                        }
////                    }
////                }
////            }
////        }
////    }
////    
////    
////    class MonsterRoomCreator {
////        String image, outline, encounterID;
////
////        public MonsterRoomCreator(String image, String outline, String encounterID) {
////            this.image = image;
////            this.outline = outline;
////            this.encounterID = encounterID;
////        }
////
////        public MonsterRoom get() {
////            return new FixedMonsterRoom(encounterID, image, outline);
////        }
////    }
////    
////    
////    public static void deathTriggers(AbstractMonster mo) {
////        for(int i = mo.powers.size() - 1; i >= 0; i--) {
////            AbstractPower pow = mo.powers.get(i);
////            pow.onDeath();
////        }
////        for(final AbstractRelic relic : AbstractDungeon.player.relics) {
////            relic.onMonsterDeath(mo);
////        }
////    }
////    
////    public static void addEventMonster(String id, AbstractMonster mo) {
////        for(int i = 1; i <= 3; i++) {
////            BaseMod.addMonster(id + i, () -> mo);
////            BaseMod.addMonsterEncounter(ID, new MonsterInfo(id + i, 1F));
////            BaseMod.addStrongMonsterEncounter(ID, new MonsterInfo(id + i, 1F));
////            BaseMod.addEliteEncounter(ID, new MonsterInfo(id + i, 1F));
////        }
////    }
////    
////    public static void addRelicReward(String relicID) {
////        if (AbstractDungeon.player.hasRelic(relicID) && !relicID.equals(Circlet.ID)) {
////            AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(RelicLibrary.getRelic(Circlet.ID).makeCopy()));
////        } else {
////            boolean found = false;
////
////            Iterator var2 = AbstractDungeon.getCurrRoom().rewards.iterator();
////
////            while (var2.hasNext()) {
////                RewardItem ri = (RewardItem) var2.next();
////                if (ri.type == RewardItem.RewardType.RELIC && ri.relic != null && ri.relic.relicId.equals(relicID)) {
////                    found = true;
////                    break;
////                }
////            }
////
////            if (!found) {
////                AbstractDungeon.getCurrRoom().rewards.add(new PaleRewardItem(RelicLibrary.getRelic(relicID).makeCopy()));
////            }
////        }
////    }
//    
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
//
//
//    private void printMapToLog() {
//        logger.info(">>> Mapa de KioDungeon:");
//        for (int y = map.size() - 1; y >= 0; y--) {
//            StringBuilder row = new StringBuilder(String.format("%2d ", y));
//            for (int x = 0; x < 7; x++) {
//                MapRoomNode node = map.get(y).get(x);
//                if (node.room != null) {
//                    String letter = "?";
//                    if (node.room.getClass().getSimpleName().toLowerCase().contains("boss")) {
//                        letter = "B";
//                    } else if (node.room.getClass().getSimpleName().toLowerCase().contains("elite")) {
//                        letter = "E";
//                    } else if (node.room.getClass().getSimpleName().toLowerCase().contains("event")) {
//                        letter = "?";
//                    } else {
//                        letter = "M"; // MonsterRoom u otros
//                    }
//                    row.append(" ").append(letter).append(" ");
//                } else {
//                    row.append(" . ");
//                }
//            }
//            logger.info(row.toString());
//        }
//    }
//  
// 
//    
//
//    
// 
//   
//
//
//    @Override
//    public String getBodyText() {
//        return "Un camino solitario hacia un alma olvidada.";
//    }
//
//    @Override
//    public String getOptionText() {
//        return "Entrar al Santuario de Kio";
//    }
//
//    @Override
//    public String getAfterSelectText() {
//        return "Sientes una mirada tras de ti.";
//    }
//
//    @Override
//    public String getActNumberText() {
//        return "Acto K";
//    }
//} 

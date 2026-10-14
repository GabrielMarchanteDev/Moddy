package mymod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import actlikeit.dungeons.CustomDungeon;
import basemod.BaseMod;
import basemod.devcommands.ConsoleCommand;
import basemod.interfaces.AddAudioSubscriber;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostBattleSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import mymod.characters.ui.LunarAmbience;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.StartGameSubscriber;
import mymod.cards.kio.AhoraVengo;
import mymod.cards.kio.ComerPalomitas;
import mymod.cards.kio.Compas;
import mymod.cards.kio.ContarDobles;
import mymod.cards.kio.EchoFromOthers;
import mymod.cards.kio.GolpeDeSuerte;
import mymod.cards.kio.HiloDeTiempo;
import mymod.cards.kio.KioAttack;
import mymod.cards.kio.KioAttack2;
import mymod.cards.kio.KioAttack3;
import mymod.cards.kio.KioAttack4;
import mymod.cards.kio.KioCardColor;
import mymod.cards.kio.KioDefend;
import mymod.cards.kio.Peppazo;
import mymod.cards.kio.PurificarMaldicion;
import mymod.cards.kio.RacionDoble;
import mymod.cards.kio.SuerteImparable;
import mymod.cards.kio.Transformacion;
import mymod.cards.kio.VisionDoble;
import mymod.cards.kio.EscudoReflectante.EscudoReflectante;
import mymod.cards.poro.Armonia;
import mymod.cards.poro.Impulso;
import mymod.cards.poro.PoroAttack;
import mymod.cards.poro.PoroCardColor;
import mymod.cards.poro.PoroDefend;
import mymod.cards.poro.CobijoLunar;
import mymod.cards.poro.GiroLunar;
import mymod.characters.Baymax;
import mymod.characters.Kio;
import mymod.characters.MyPlayerBaymaxClassEnum;
import mymod.characters.MyPlayerKioClassEnum;
import mymod.characters.MyPlayerPoroClassEnum;
import mymod.characters.Poro;
import mymod.commands.AddGoldCommand;
import mymod.commands.KioSuerteCommand;
import mymod.dungeon.Kanto;
import mymod.events.MyFirstEvent;
import mymod.map.KioDungeon;
import mymod.monsters.Demon;
import mymod.monsters.Demon2;
import mymod.monsters.KioEnemy;
import mymod.relics.Kiobag;
import mymod.relics.palomitero.PalomiteroDeKio;

@SpireInitializer
public class MyMod implements EditCharactersSubscriber, EditCardsSubscriber, EditRelicsSubscriber,
		EditStringsSubscriber, EditKeywordsSubscriber, AddAudioSubscriber, PostInitializeSubscriber,
		StartGameSubscriber, PostBattleSubscriber, PostUpdateSubscriber {

	public static final String modID = "mymod";

    @Override public void receivePostUpdate() {
        LunarAmbience.update();
    }

	public static String makeID(String idText) {
		return modID + ":" + idText;
	}

	public static SpireConfig pokemonRegionConfig;
	public static final Logger logger = LogManager.getLogger(MyMod.class.getName());

	public static Color KIO_COLOR = new Color(1.0f, 0.5f, 0.0f, 1f);
	public static Color PORO_COLOR = new Color(0.6f, 0.3f, 0.1f, 1f);

	public MyMod() {

		BaseMod.subscribe(this);
		BaseMod.addColor(KioCardColor.KIO_COLOR, KIO_COLOR, KIO_COLOR, KIO_COLOR, KIO_COLOR, KIO_COLOR, KIO_COLOR,
				KIO_COLOR, "images/kio/cards/bg_attack512.png", "images/kio/cards/bg_skill512.png",
				"images/kio/cards/bg_power512.png", "images/kio/cards/orb512.png", "images/kio/cards/bg_attack1024.png",
				"images/kio/cards/bg_skill1024.png", "images/kio/cards/bg_power1024.png",
				"images/kio/cards/orb1024.png", "images/kio/cards/orb64.png");

		BaseMod.addColor(PoroCardColor.PORO_COLOR, PORO_COLOR, PORO_COLOR, PORO_COLOR, PORO_COLOR, PORO_COLOR,
				PORO_COLOR, PORO_COLOR, "images/poro/cards/bg_attack512.png", "images/poro/cards/bg_skill512.png",
				"images/poro/cards/bg_power512.png", "images/poro/cards/orb512.png",
				"images/poro/cards/bg_attack1024.png", "images/poro/cards/bg_skill1024.png",
				"images/poro/cards/bg_power1024.png", "images/poro/cards/orb1024.png", "images/poro/cards/orb64.png");

		Properties pokemonRegionDefaults = new Properties();
		pokemonRegionDefaults.setProperty("Tutorial Seen", "FALSE");
		try {
			pokemonRegionConfig = new SpireConfig("POKIMON", "PokemonRegionsMod", pokemonRegionDefaults);
		} catch (IOException e) {
			logger.error("PokemonRegionMod SpireConfig initialization failed:");
			e.printStackTrace();
		}

	}

	public static void initialize() {

		MyMod mod = new MyMod();
	}

	@Override
	public void receivePostInitialize() {
		logger.info(">>> MiMod se ha inicializado correctamente!");

		logger.info(">>> REGISTRANDO ENEMIGOS");
		ConsoleCommand.addCommand("suerte", KioSuerteCommand.class);
		ConsoleCommand.addCommand("añadirOro", AddGoldCommand.class);
		// ✅ Instanciar tu dungeon y registrarla en el mapa global
		Kanto kanto = new Kanto();
		CustomDungeon.dungeons.put(Kanto.ID, kanto);

		// Bosses
		kanto.addBoss(KioEnemy.ID, (BaseMod.GetMonster) KioEnemy::new, "images/monsters/Boss/BossNegro.png",
				"images/monsters/Boss/BossRosa.png");

		// Elites
		BaseMod.addMonster("Demon2", () -> new Demon2(0, 0));
		BaseMod.addEliteEncounter(KioDungeon.ID, new MonsterInfo("Demon2", 1.0f));

		for (int i = 1; i <= 4; i++) {
			String encounterId = "KioEnemy" + i;
			BaseMod.addMonster(encounterId, () -> new KioEnemy(0, 0));
			BaseMod.addMonsterEncounter(KioDungeon.ID, new MonsterInfo(encounterId, 1.0f));
			logger.info(">>> Generando encuentro " + encounterId);
		}

		BaseMod.addMonster("Demon1", () -> new Demon(0, 0));
		BaseMod.addMonsterEncounter(KioDungeon.ID, new MonsterInfo("Demon", 1.0f));

		BaseMod.addMonster("DemonGroup",
				() -> new MonsterGroup(new AbstractMonster[] { new Demon(0, 0), new Demon(0, 0) }));
		BaseMod.addStrongMonsterEncounter(KioDungeon.ID, new MonsterInfo("KioGroup", 1.0f));

		BaseMod.addMonster(KioEnemy.ID, () -> new KioEnemy(0, 0));

		logger.info(">>> DUNGEON CREADA");
		BaseMod.addEvent(MyFirstEvent.ID, MyFirstEvent.class);
		//BaseMod.addEvent(BaymaxCheckEvent.ID, BaymaxCheckEvent.class);

		ConsoleCommand.addCommand("addgold", AddGoldCommand.class);

		// ✅ ACTO 1 – Reemplazar dungeons base por Kanto
		ArrayList<String> act1List = CustomDungeon.actnumbers.computeIfAbsent(1, k -> new ArrayList<>());
		act1List.removeIf(id -> id.equals(Exordium.ID) || id.equals(Kanto.ID));
		act1List.add(Kanto.ID);

		// 🧹 Quitar dungeons base del mapa global
		CustomDungeon.dungeons.remove(Exordium.ID);
		CustomDungeon.dungeons.remove(TheCity.ID);
		CustomDungeon.dungeons.remove(TheBeyond.ID);
		CustomDungeon.dungeons.remove(TheEnding.ID);
		logger.info("▶️ Dungeons registradas en el acto 1: " + CustomDungeon.actnumbers.get(1));
		logger.info("📦 Mapa completo de dungeons: " + CustomDungeon.dungeons.keySet());

	}

	@Override
	public void receiveEditCharacters() {
		// ✅ REGISTRAR KIO siempre
		BaseMod.addCharacter(new Kio("Kio"), "images/kio/ui/char_button.png", "images/kio/ui/char_portrait.png",
				MyPlayerKioClassEnum.KIO_CLASS);

		// ✅ REGISTRAR PORO SI NO ESTÁ BLOQUEADO

		BaseMod.addCharacter(new Poro("Poro"), "images/poro/ui/char_button.png", "images/poro/ui/PoroPortraitBG.png",
				MyPlayerPoroClassEnum.PORO_CLASS);

		// ✅ REGISTRAR PORO SI NO ESTÁ BLOQUEADO

		BaseMod.addCharacter(new Baymax("Baymax"), "images/poro/ui/baymax_button.png", "images/poro/ui/baymax.jpg",
				MyPlayerBaymaxClassEnum.BAYMAX_CLASS);

	}

	@Override
	public void receiveEditRelics() {
		logger.info(">>> Registrando reliquias...");
		BaseMod.addRelicToCustomPool(new Kiobag(), PoroCardColor.PORO_COLOR);
		logger.info("✅ Reliquia 'Kiobag' registrada correctamente.");
		BaseMod.addRelicToCustomPool(new PalomiteroDeKio(), KioCardColor.KIO_COLOR);
		logger.info("✅ Reliquia 'PalomiteroDeKio' registrada correctamente.");
	}

	@Override
	public void receiveEditCards() {
		logger.info(">>> Registrando cartas en BaseMod...");
		Peppazo card = new Peppazo();
		BaseMod.addCard(card);
		UnlockTracker.markCardAsSeen(card.cardID);
		UnlockTracker.unlockCard(card.cardID);

//		   // ✅ Registro de las palomitas
//	    BaseMod.addCard(new PalomitaDulce());
//	    UnlockTracker.markCardAsSeen(PalomitaDulce.ID);
//	    UnlockTracker.unlockCard(PalomitaDulce.ID);
//
//	    BaseMod.addCard(new PalomitaRancia());
//	    UnlockTracker.markCardAsSeen(PalomitaRancia.ID);
//	    UnlockTracker.unlockCard(PalomitaRancia.ID);
//
//	    BaseMod.addCard(new PalomitaHueca());
//	    UnlockTracker.markCardAsSeen(PalomitaHueca.ID);
//	    UnlockTracker.unlockCard(PalomitaHueca.ID);
//	    
//	    BaseMod.addCard(new PalomitaMayor());
//	    UnlockTracker.markCardAsSeen(PalomitaMayor.ID);
//	    UnlockTracker.unlockCard(PalomitaMayor.ID);

		BaseMod.addCard(new RacionDoble());
		UnlockTracker.markCardAsSeen(RacionDoble.ID);
		UnlockTracker.unlockCard(RacionDoble.ID);

		BaseMod.addCard(new ComerPalomitas());
		UnlockTracker.markCardAsSeen(ComerPalomitas.ID);
		UnlockTracker.unlockCard(ComerPalomitas.ID);

		BaseMod.addCard(new EchoFromOthers());
		UnlockTracker.markCardAsSeen(EchoFromOthers.ID);
		UnlockTracker.unlockCard(EchoFromOthers.ID);

		BaseMod.addCard(new KioDefend());
		UnlockTracker.markCardAsSeen(KioDefend.ID);
		UnlockTracker.unlockCard(KioDefend.ID);

		BaseMod.addCard(new KioAttack());
		UnlockTracker.markCardAsSeen(KioAttack.ID);
		UnlockTracker.unlockCard(KioAttack.ID);

		BaseMod.addCard(new KioAttack2());
		UnlockTracker.markCardAsSeen(KioAttack2.ID);
		UnlockTracker.unlockCard(KioAttack2.ID);

		BaseMod.addCard(new KioAttack3());
		UnlockTracker.markCardAsSeen(KioAttack3.ID);
		UnlockTracker.unlockCard(KioAttack3.ID);

		BaseMod.addCard(new KioAttack4());
		UnlockTracker.markCardAsSeen(KioAttack4.ID);
		UnlockTracker.unlockCard(KioAttack4.ID);

		BaseMod.addCard(new SuerteImparable());
		UnlockTracker.unlockCard(SuerteImparable.ID);

		BaseMod.addCard(new GolpeDeSuerte());
		UnlockTracker.unlockCard(GolpeDeSuerte.ID);
		UnlockTracker.markCardAsSeen(GolpeDeSuerte.ID);

		BaseMod.addCard(new Transformacion());
		UnlockTracker.unlockCard(Transformacion.ID);
		UnlockTracker.markCardAsSeen(Transformacion.ID);

		BaseMod.addCard(new PoroDefend());
		UnlockTracker.unlockCard(PoroDefend.ID);
		UnlockTracker.markCardAsSeen(PoroDefend.ID);

		BaseMod.addCard(new PoroAttack());
        BaseMod.addCard(new CobijoLunar());
        UnlockTracker.unlockCard(CobijoLunar.ID);
        UnlockTracker.markCardAsSeen(CobijoLunar.ID);
        BaseMod.addCard(new GiroLunar());
        UnlockTracker.unlockCard(GiroLunar.ID);
        UnlockTracker.markCardAsSeen(GiroLunar.ID);
		UnlockTracker.unlockCard(PoroAttack.ID);
		UnlockTracker.markCardAsSeen(PoroAttack.ID);
		
		BaseMod.addCard(new AhoraVengo());
		UnlockTracker.unlockCard(AhoraVengo.ID);
		UnlockTracker.markCardAsSeen(AhoraVengo.ID);
		
		BaseMod.addCard(new HiloDeTiempo());
		UnlockTracker.unlockCard(HiloDeTiempo.ID);
		UnlockTracker.markCardAsSeen(HiloDeTiempo.ID);
		
		BaseMod.addCard(new Compas());
		UnlockTracker.unlockCard(Compas.ID);
		UnlockTracker.markCardAsSeen(Compas.ID);
		
		BaseMod.addCard(new ContarDobles());
		UnlockTracker.unlockCard(ContarDobles.ID);
		UnlockTracker.markCardAsSeen(ContarDobles.ID);
		
		BaseMod.addCard(new VisionDoble());
		UnlockTracker.unlockCard(VisionDoble.ID);
		UnlockTracker.markCardAsSeen(VisionDoble.ID);
		
		BaseMod.addCard(new PurificarMaldicion());
		UnlockTracker.unlockCard(PurificarMaldicion.ID);
		UnlockTracker.markCardAsSeen(PurificarMaldicion.ID);
		
		BaseMod.addCard(new EscudoReflectante());
		UnlockTracker.unlockCard(EscudoReflectante.ID);
		UnlockTracker.markCardAsSeen(EscudoReflectante.ID);
		

		BaseMod.addCard(new Impulso());
		UnlockTracker.unlockCard(Impulso.ID);
		UnlockTracker.markCardAsSeen(Impulso.ID);
		
		BaseMod.addCard(new Armonia());
		UnlockTracker.unlockCard(Armonia.ID);
		UnlockTracker.markCardAsSeen(Armonia.ID);
		logger.info(">>> CARTAS REGISTRADAS");

	}

	@Override
	public void receiveEditStrings() {
		logger.info(">>> Cargando archivos de localización...");

		String lang = "spa"; // Define un único idioma base para todas las rutas

		try {
			BaseMod.loadCustomStringsFile(CardStrings.class, "mymod/localization/" + lang + "/CardStrings.json");
			logger.info("✅ CardStrings cargado correctamente.");

			BaseMod.loadCustomStringsFile(RelicStrings.class, "mymod/localization/" + lang + "/RelicStrings.json");
			logger.info("✅ RelicStrings cargado correctamente.");

			BaseMod.loadCustomStringsFile(CharacterStrings.class,
					"mymod/localization/" + lang + "/CharacterStrings.json");
			logger.info("✅ CharacterStrings cargado correctamente.");

			BaseMod.loadCustomStringsFile(EventStrings.class, "mymod/localization/" + lang + "/EventStrings.json");
			logger.info("✅ EventStrings cargado correctamente.");

			BaseMod.loadCustomStringsFile(UIStrings.class, "mymod/localization/" + lang + "/UIStrings.json");
			logger.info("✅ UIStrings cargado correctamente.");
			
			BaseMod.loadCustomStringsFile(PowerStrings.class, "mymod/localization/" + lang + "/PowerStrings.json");
			logger.info("✅ PowerStrings cargado correctamente.");

		} catch (Exception e) {
			logger.error("🚨 Error al cargar alguna localización: ", e);
		}
	}

	public static String assetPath(String path) {
		return modID + "/" + path;
	}

	@Override
	public void receivePostBattle(AbstractRoom arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveStartGame() {
		logger.info("🛠 Forzando inicio en mymod:Kanto...");
		com.megacrit.cardcrawl.core.CardCrawlGame.nextDungeon = Kanto.ID;

	}

	@Override
	public void receiveAddAudio() {
		// En tu inicializador del mod
		BaseMod.addAudio("Satisfied.ogg", "audio/music/Satisfied.ogg");
		// BaseMod.addAudio("Menu", "audio/music/FEZ.mp3");
		BaseMod.addAudio("KIO_SCENE1", "audio/vfx/1.ogg");
		BaseMod.addAudio("KIO_SCENE2", "audio/vfx/2.ogg");
		BaseMod.addAudio("KIO_SCENE3", "audio/vfx/3.ogg");

	}

	@Override
	public void receiveEditKeywords() {
		// TODO Auto-generated method stub

	}
}

package mymod.characters;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import basemod.abstracts.CustomEnergyOrb;
import basemod.abstracts.CustomPlayer;
import mymod.MyMod;
import mymod.cards.kio.KioAttack;
import mymod.cards.kio.KioCardColor;
import mymod.cards.kio.KioDefend;
import mymod.cards.kio.Peppazo;
import mymod.cards.poro.PoroAttack;
import mymod.cards.poro.PoroCardColor;
import mymod.cards.poro.PoroDefend;
import mymod.relics.Kiobag;
import mymod.characters.ui.LunarPhase;
import mymod.characters.ui.PoroMoon;

public class Poro extends CustomPlayer {

	public static final String ID = "Poro";
    private final PoroMoon moon = new PoroMoon();

    public LunarPhase getLunarPhase() { return moon.getPhase(); }
    public void advanceLunarPhase() { moon.advance(); }

    @Override public void preBattlePrep() {
        moon.reset();
        super.preBattlePrep();
    }

    @Override public void applyStartOfTurnRelics() {
        advanceLunarPhase();
        super.applyStartOfTurnRelics();
    }

    @Override public void update() {
        super.update();
        moon.update(this);
    }

    @Override public void render(SpriteBatch sb) {
        super.render(sb);
        moon.render(sb, this);
    }

	private static final String SHOULDER1 = "images/kio/char/shoulder1.png";
	private static final String SHOULDER2 = "images/kio/char/shoulder2.png";
	private static final String CORPSE = "images/kio/char/corpse.png";
    public static final String MY_CHARACTER_SKELETON_ATLAS = "images/poro/char/spine/Poroanimated.atlas"; // spine animation atlas
    public static final String MY_CHARACTER_SKELETON_JSON = "images/poro/char/spine/Poroanimated.json"; // spine animation json

	private static final String[] ORB_TEXTURES = new String[] { "images/kio/ui/topPanel/layer1.png",
			"images/kio/ui/topPanel/layer2.png", "images/kio/ui/topPanel/layer3.png",
			"images/kio/ui/topPanel/layer4.png", "images/kio/ui/topPanel/layer5.png",
			"images/kio/ui/topPanel/layer6.png", "images/kio/ui/topPanel/layer1.png",
			"images/kio/ui/topPanel/energyRedVFX.png", // orbVFXLayer1
			"images/kio/ui/topPanel/energyPurpleVFX.png" // orbVFXLayer2
	};

	private static final String ORB_VFX = "images/ui/topPanel/energyRedVFX.png";

	private static final int ENERGY_PER_TURN = 4;
	private static final int STARTING_HP = 80;
	private static final int MAX_HP = 80;
	private static final int STARTING_GOLD = 0;
	private static final int HAND_SIZE = 5;
	private static final int ORB_SLOTS = 0;
	private static final float VISUAL_OFFSET_Y = 50f * Settings.scale;
	
    public Poro(String name) {
        // ✅ Usamos el orb por defecto de Ironclad (EnergyOrbRed)
        super(name, MyPlayerPoroClassEnum.PORO_CLASS, new CustomEnergyOrb(ORB_TEXTURES, ORB_VFX, null),null,null );

        this.dialogX = this.drawX + 0.0F * Settings.scale;
        this.dialogY = this.drawY + 220.0F * Settings.scale;

        this.loadAnimation(
        		MY_CHARACTER_SKELETON_ATLAS,
        		MY_CHARACTER_SKELETON_JSON,
        		 2.0f
        );
        
        initializeClass(
            null,
            SHOULDER1, SHOULDER2, CORPSE,
            getLoadout(),
            -10.0F, -100.0F, 260.0F, 290.0F,
            new EnergyManager(ENERGY_PER_TURN)
        );

        // ✅ Cargamos animación Spine por código (esto te da acceso a `state`)
       

        // ✅ Activamos animación inicial
        AnimationState.TrackEntry e = this.state.setAnimation(0, "IDLE", true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }
   
   

	public static String getCharacterName() {
		return CardCrawlGame.languagePack.getCharacterString(ID).NAMES[0];
	}

	public static String getCharacterDescription() {
		return CardCrawlGame.languagePack.getCharacterString(ID).TEXT[0];
	}

	@Override
	public ArrayList<String> getStartingDeck() {
		ArrayList<String> retVal = new ArrayList<>();
		retVal.add(PoroAttack.ID); 
		retVal.add(PoroAttack.ID); 
		retVal.add(PoroAttack.ID); 
		retVal.add(PoroAttack.ID); 
		retVal.add(PoroAttack.ID); 
		
		retVal.add(PoroDefend.ID);
		retVal.add(PoroDefend.ID);
		retVal.add(PoroDefend.ID);
		retVal.add(PoroDefend.ID);
		return retVal;
	}

	@Override
	public ArrayList<String> getStartingRelics() {
		ArrayList<String> retVal = new ArrayList<>();
		retVal.add(Kiobag.ID);
		UnlockTracker.markRelicAsSeen(Kiobag.ID);
		return retVal;
	}
	
	
	@Override
	public CharSelectInfo getLoadout() {
		CharacterStrings charStrings = CardCrawlGame.languagePack.getCharacterString(ID);
		return new CharSelectInfo(getCharacterName(), getCharacterDescription(), STARTING_HP, MAX_HP, ORB_SLOTS,
				STARTING_GOLD, HAND_SIZE, this, getStartingRelics(), getStartingDeck(), false);
	}
	
//	@Override
//	public void renderPlayerImage(SpriteBatch sb) {
//	    sb.draw(
//	        KIO_TEXTURE,
//	        this.drawX - KIO_TEXTURE.getWidth() / 2f * Settings.scale,
//	        this.drawY,
//	        KIO_TEXTURE.getWidth() * Settings.scale,
//	        KIO_TEXTURE.getHeight() * Settings.scale
//	    );
//	}

	@Override
	public void doCharSelectScreenSelectEffect() {
		CardCrawlGame.sound.play("ATTACK_MAGIC_FAST_1");
	}

	@Override
	public int getAscensionMaxHPLoss() {
		return 4;
	}

	@Override
	public CardColor getCardColor() {
		return PoroCardColor.PORO_COLOR;
	}

	@Override
	public Color getCardRenderColor() {
		return Color.valueOf("6aff91");
	}

	@Override
	public Color getCardTrailColor() {
		return Color.valueOf("6aff91");
	}

	@Override
	public String getCustomModeCharacterButtonSoundKey() {
		return "ATTACK_MAGIC_FAST_1";
	}

	@Override
	public BitmapFont getEnergyNumFont() {
		return FontHelper.energyNumFontRed;
	}

	@Override
	public String getLocalizedCharacterName() {
		return CardCrawlGame.languagePack.getCharacterString(ID).NAMES[0];
	}

	@Override
	public Color getSlashAttackColor() {
		return Color.valueOf("6aff91"); // O el color que prefieras para los ataques
	}

	@Override
	public AttackEffect[] getSpireHeartSlashEffect() {
		return new AttackEffect[] { AttackEffect.SLASH_HEAVY, AttackEffect.SLASH_DIAGONAL, AttackEffect.SLASH_VERTICAL,
				AttackEffect.SLASH_HORIZONTAL };
	}

	@Override
	public String getSpireHeartText() {
		return "Poro no duda al enfrentar el corazón. Su determinación brilla más allá del velo.";
	}

	@Override
	public AbstractCard getStartCardForEvent() {
		return null; // Puedes devolver una carta si quieres que se muestre en ciertos eventos
	}

	@Override
	public String getTitle(PlayerClass playerClass) {
		return "El corazón del santuario";
	}

	@Override
	public String getVampireText() {
		return "Eres uno con los antiguos. Aceptas la sangre sin temor.";
	}

	@Override
	public AbstractPlayer newInstance() {
		return new Poro("Poro");
	}
}

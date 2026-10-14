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
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;


import basemod.abstracts.CustomPlayer;
import mymod.MyMod;
import mymod.cards.kio.ComerPalomitas;
import mymod.cards.kio.GolpeDeSuerte;
import mymod.cards.kio.KioAttack;
import mymod.cards.kio.KioCardColor;
import mymod.cards.kio.KioDefend;
import mymod.relics.palomitero.PalomiteroDeKio;
import mymod.characters.ui.PopcornEnergyOrb;

public class Kio extends CustomPlayer {

	public static final String ID = MyMod.makeID("Kio");

	private static final String SHOULDER1 = "images/kio/char/shoulder1.png";
	private static final String SHOULDER2 = "images/kio/char/shoulder2.png";
	private static final String CORPSE = "images/kio/char/corpse.png";
    public static final String MY_CHARACTER_SKELETON_ATLAS = "images/kio/char/spine/KioAnimated.atlas"; // spine animation atlas
    public static final String MY_CHARACTER_SKELETON_JSON = "images/kio/char/spine/KioAnimated.json"; // spine animation json

	private static final int ENERGY_PER_TURN = 3;
	private static final int STARTING_HP = 75;
	private static final int MAX_HP = 75;
	private static final int STARTING_GOLD = 99;
	private static final int HAND_SIZE = 5;
	private static final int ORB_SLOTS = 0;
	private boolean enModo85 = false;
	
    public Kio(String name) {
        // Indicador de energia con aro animado y palomitas fijas.
        super(name, MyPlayerKioClassEnum.KIO_CLASS, new PopcornEnergyOrb(),null,null );

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
            20.0F, -50.0F, 220.0F, 290.0F,
            new EnergyManager(ENERGY_PER_TURN)
        );

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
		//MAZO INCIAL
		retVal.add(KioDefend.ID);
		retVal.add(KioDefend.ID);
		retVal.add(KioDefend.ID);
		retVal.add(KioDefend.ID);
		

		retVal.add(KioAttack.ID); 
		retVal.add(KioAttack.ID); 
		retVal.add(KioAttack.ID); 
		retVal.add(KioAttack.ID); 
		
		retVal.add(GolpeDeSuerte.ID);
		retVal.add(ComerPalomitas.ID);
		return retVal;
	}

	@Override
	public ArrayList<String> getStartingRelics() {
		ArrayList<String> retVal = new ArrayList<>();
		retVal.add(PalomiteroDeKio.ID);
		UnlockTracker.markRelicAsSeen(PalomiteroDeKio.ID);
		return retVal;
	}
	
	  public void cambiarModeloSpine(String atlasPath, String jsonPath, float scale, String animInicial, boolean loop) {
	        this.loadAnimation(atlasPath, jsonPath, scale);
	        this.state.setAnimation(0, animInicial, loop);
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
	
	public void aplicarEfectoPorSuerte() {
	    float suerte = (float) this.kioLuck / Kio.MAX_LUCK;

	    if (this.kioLuck >= Kio.MAX_LUCK) {
	       
	        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.GainEnergyAction(1));
	        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.DrawCardAction(this, 1));
	        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.utility.SFXAction("POWER_POISON"));
	        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
	            AbstractDungeon.actionManager.addToBottom(
	                new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(m, this,
	                    new com.megacrit.cardcrawl.powers.VulnerablePower(m, 1, false), 1)
	            );
	        }
	        return;
	    }

	    if (suerte >= 0.82f) {
	        if (MathUtils.randomBoolean(0.25f)) {
	            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
	                AbstractDungeon.actionManager.addToBottom(
	                    new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(m, this,
	                        new com.megacrit.cardcrawl.powers.VulnerablePower(m, 1, false), 1)
	                );
	            }
	        }
	    } else if (suerte >= 0.58f) {
	        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.DrawCardAction(this, 1));
	    } else if (suerte >= 0.35f) {
	        if (MathUtils.randomBoolean(0.20f)) {
	            AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.GainEnergyAction(1));
	        }
	    }
	}
	



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
		return KioCardColor.KIO_COLOR;
	}

	@Override
	public Color getCardRenderColor() {
		return Color.ORANGE;
	}

	@Override
	public Color getCardTrailColor() {
		return Color.ORANGE;
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
		return Color.FIREBRICK; // O el color que prefieras para los ataques
	}

	@Override
	public AttackEffect[] getSpireHeartSlashEffect() {
		return new AttackEffect[] { AttackEffect.SLASH_HEAVY, AttackEffect.SLASH_DIAGONAL, AttackEffect.SLASH_VERTICAL,
				AttackEffect.SLASH_HORIZONTAL };
	}

	@Override
	public String getSpireHeartText() {
		return "Kio no duda al enfrentar el corazón. Su determinación brilla más allá del velo.";
	}

	@Override
	public AbstractCard getStartCardForEvent() {
		return null; // Puedes devolver una carta si quieres que se muestre en ciertos eventos
	}

	@Override
	public String getTitle(PlayerClass playerClass) {
		return "El Primer Guardián";
	}

	@Override
	public String getVampireText() {
		return "Eres uno con los antiguos. Aceptas la sangre sin temor.";
	}

	@Override
	public AbstractPlayer newInstance() {
		return new Kio("Kio");
	}
	public int kioLuck = 0;
	public static final int MAX_LUCK = 85;

	public void addKioLuck(int amount) {
	    this.kioLuck = Math.min(this.kioLuck + amount, MAX_LUCK);
	    // 👈 AÑADIDO AQUÍ
	}


	public void resetKioLuck() {
	    this.kioLuck = 0;
	}

	public int getKioLuck() {
	    return this.kioLuck;
	}
	
	public float getSuertePorcentaje() {
		return (float) this.kioLuck / MAX_LUCK;
	}

	@Override
	public void applyStartOfCombatLogic() {
		 super.applyStartOfCombatLogic();
	    AbstractDungeon.actionManager.addToBottom(
	        new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(
	            this, this, new mymod.powers.SuertePasivaPower(this)
	        )
	    );
	  
	}


	

	@Override
	public void render(SpriteBatch sb) {
	    super.render(sb);
	    mymod.characters.ui.KioLuckBar.render(sb);
	}

}

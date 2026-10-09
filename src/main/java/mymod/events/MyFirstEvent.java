package mymod.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.core.Settings;

import mymod.monsters.KioEnemy;

public class MyFirstEvent extends AbstractImageEvent {
    public static final String ID = "MyFirstEvent";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;

    private boolean combatPending = false;

    public MyFirstEvent() {
        super(NAME, DESCRIPTIONS[0], "images/events/Captura.JPG");

        this.imageEventText.setDialogOption(OPTIONS[0]);
        this.imageEventText.setDialogOption(OPTIONS[1]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                if (buttonPressed == 0) {
                    this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                    this.imageEventText.clearAllDialogs();
                    imageEventText.clearRemainingOptions();
                    screenNum = 1;
                    combatPending = true;
                } else {
                    this.imageEventText.clearAllDialogs();
                    
                    this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                    this.imageEventText.setDialogOption(OPTIONS[2]);
                    imageEventText.clearRemainingOptions();
                    screenNum = 2;
                }
                break;
            case 1:
            case 2:
            	AbstractDungeon.topLevelEffects.add(new BaymaxHistory());
                openMap();
                break;
        }
    }

    @Override
    public void update() {
        super.update();

        if (combatPending && !AbstractDungeon.isScreenUp) {
            combatPending = false;

            AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(new KioEnemy(-300.0f, 0.0f));
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMBAT;
            AbstractDungeon.getCurrRoom().rewards.clear();
            AbstractDungeon.getCurrRoom().addGoldToRewards(50);
            AbstractDungeon.getCurrRoom().addRelicToRewards(RelicLibrary.getRelic("Anchor").makeCopy());

            enterCombat();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);

        // Render del texto tembloroso solo en la pantalla de combate
        if (screenNum == 1) {
            float dx = MathUtils.random(-2.0f, 2.0f) * Settings.scale;
            float dy = MathUtils.random(-2.0f, 2.0f) * Settings.scale;

            FontHelper.renderSmartText(
                sb,
                FontHelper.panelNameFont,
                "Sientes la mirada de Kio...",
                80.0f * Settings.scale + dx,
                900.0f * Settings.scale + dy,
                9999.0f,
                9999.0f,
                Settings.RED_TEXT_COLOR
            );
        }
    }
}

package mymod.events;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import mymod.memo.MemoProgress;


public class BaymaxCheckEvent extends AbstractImageEvent {
    public static final String ID = "mymod:BaymaxCheckEvent";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private float timer = 0f;
    private final float DURATION_BEFORE_RETURN = 34f;
    private boolean triggered = false;
    private BaymaxHistory history;

    public BaymaxCheckEvent() {
        super(eventStrings.NAME, eventStrings.DESCRIPTIONS[0], "images/events/Captura.JPG");

        AbstractPlayer p = AbstractDungeon.player;
        if (p.getClass().getSimpleName().equalsIgnoreCase("Baymax")) {
            history = new BaymaxHistory();
            AbstractDungeon.effectList.add(history);
        }else {
            // Redirigir a la sala de Neow
            AbstractDungeon.currMapNode.room = new NeowRoom(true); // true = permite elección
            AbstractDungeon.getCurrRoom().onPlayerEntry(); // Fuerza entrada al nuevo evento
        }

        // No se muestran opciones, solo reproduce el efecto
    }

    @Override
    public void update() {
        super.update();

        timer += com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        if (timer >= DURATION_BEFORE_RETURN && !triggered) {
            triggered = true;
          
            UnlockTracker.unlockPref.putInteger("Poro", 1);
            if (history != null) MemoProgress.markBaymaxSeen();
            // startOver puede vaciar los efectos sin llamar a su dispose.
            if (history != null) history.dispose();
            // Cierra la partida y vuelve al menú principal
            CardCrawlGame.startOver();
        }
    }

    @Override
    protected void buttonEffect(int i) {
        // Nada que hacer aquí, no hay botones visibles
    }
}

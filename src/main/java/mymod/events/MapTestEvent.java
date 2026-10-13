package mymod.events;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;

/** A deterministic, harmless event for testing the editable map. */
public class MapTestEvent extends AbstractImageEvent {
    private boolean finished;

    public MapTestEvent() {
        super("Un descanso entre caminos", "Este es el evento del mapa de prueba de Kio."
                + " NL Elige una recompensa para continuar.", "images/ui/event/panel.png");
        imageEventText.setDialogOption("Recuperar 10 de vida.");
        imageEventText.setDialogOption("Obtener 30 de oro.");
    }

    @Override protected void buttonEffect(int buttonPressed) {
        if (finished) {
            openMap();
            return;
        }
        if (buttonPressed == 0) AbstractDungeon.player.heal(10);
        else AbstractDungeon.player.gainGold(30);
        finished = true;
        imageEventText.updateBodyText("La eleccion queda registrada. Puedes seguir el camino.");
        imageEventText.clearAllDialogs();
        imageEventText.setDialogOption("Continuar.");
    }
}

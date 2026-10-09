package mymod.commands;

import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import java.util.ArrayList;

public class AddGoldCommand extends ConsoleCommand {

    public AddGoldCommand() {
        this.maxExtraTokens = 1;  // Solo permite 1 número como argumento
        this.minExtraTokens = 1;  // El comando requiere un argumento
    }

    @Override
    public void execute(String[] tokens, int depth) {
        // El primer token después del comando es la cantidad de oro a añadir
        try {
            int amount = Integer.parseInt(tokens[depth]);

            // Añadir el oro al jugador
            AbstractDungeon.player.gainGold(amount);
            CardCrawlGame.sound.play("GOLD_GAIN");
            System.out.println("Añadido " + amount + " de oro a tu personaje.");
        } catch (NumberFormatException e) {
            // Si el número no es válido
            errorMsg(tokens);
        }
    }

    @Override
    public void errorMsg(String[] tokens) {
        // Mensaje de error en caso de que el formato no sea correcto
        System.out.println("Uso del comando: addgold [cantidad]");
    }

    @Override
    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        return null;
    }
}

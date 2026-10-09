package mymod.commands;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import mymod.characters.Kio;

public class KioSuerteCommand extends ConsoleCommand {

    public KioSuerteCommand() {
        maxExtraTokens = 1;
    }

    @Override
    public void execute(String[] tokens, int depth) {
        if (tokens.length <= depth) {
            DevConsole.log("❗ Uso incorrecto. Escribe: LuckyCharm <0-85>");
            return;
        }

        try {
            int valor = Integer.parseInt(tokens[depth]);
            AbstractPlayer p = AbstractDungeon.player;

            if (p instanceof Kio) {
                Kio kio = (Kio) p;
                kio.kioLuck = Math.max(0, Math.min(valor, Kio.MAX_LUCK));
                DevConsole.log("🌟 Suerte de Kio seteada a " + kio.kioLuck + " (" +
                        String.format("%.0f", kio.getSuertePorcentaje() * 100) + "%)");
            } else {
                DevConsole.log("❌ Este comando solo funciona si estás usando a Kio.");
            }

        } catch (NumberFormatException e) {
            DevConsole.log("❌ Valor inválido. Debes escribir un número entre 0 y 85.");
        }
    }

    
}
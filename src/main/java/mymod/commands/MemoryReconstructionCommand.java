package mymod.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import mymod.memo.RebuildingMemory;

public class MemoryReconstructionCommand extends ConsoleCommand {
    public MemoryReconstructionCommand() { minExtraTokens = 0; maxExtraTokens = 1; }
    @Override public void execute(String[] tokens, int depth) {
        if (tokens.length > depth) {
            if ("off".equalsIgnoreCase(tokens[depth])) {
                RebuildingMemory.cancel(); DevConsole.log("Prueba de recuerdo desactivada.");
            } else DevConsole.log("Uso: recuerdo | recuerdo off");
        } else DevConsole.log(RebuildingMemory.arm());
    }
}

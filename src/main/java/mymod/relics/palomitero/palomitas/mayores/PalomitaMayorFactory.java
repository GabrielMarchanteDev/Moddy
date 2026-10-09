package mymod.relics.palomitero.palomitas.mayores;

import com.megacrit.cardcrawl.cards.AbstractCard;

public class PalomitaMayorFactory {

    public static AbstractCard create(String tipoBaseID) {
        switch (tipoBaseID) {
            case "PalomitaRancia":
                return new PalomitaMayorRancia(); // Aplica 4 débil, por ejemplo
            case "PalomitaDulce":
                return new PalomitaMayorDulce(); // Da 3 energía el próximo turno
            case "PalomitaExplosiva":
                return new PalomitaMayorExplosiva(); // 3 hits en área de 2 daño
            case "PalomitaEnergetica":
                return new PalomitaMayorEnergetica(); 
            default:
                return new PalomitaMayor(); // carta aleatoria si no coincide
        }
    }
}

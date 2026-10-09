package mymod.relics.palomitero.palomitas.menores;

import java.util.ArrayList;
import java.util.Collections;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class PalomitaFactory {

    public static AbstractCard getRandomPalomita() {
        ArrayList<AbstractCard> pool = new ArrayList<>();

        // Añade aquí todas las variantes de palomitas
        pool.add(new PalomitaDulce());
        pool.add(new PalomitaRancia());
        pool.add(new PalomitaHueca());
        pool.add(new PalomitaEnergetica());
        pool.add(new PalomitaExplosiva());
        // pool.add(new PalomitaPicante()); // Ejemplo extra
        // pool.add(new PalomitaSagrada());

        // Elegir una carta aleatoria del pool y devolver una copia nueva
        return pool.get(AbstractDungeon.cardRandomRng.random(pool.size() - 1)).makeCopy();
    }
    
//    public static ArrayList<AbstractCard> getRandomPalomitasWithRepeats(int count) {
//    	  ArrayList<AbstractCard> pool = new ArrayList<>();
//
//    	    // Mismo pool base, 1 de cada tipo
//    	    pool.add(new PalomitaDulce());
//    	    pool.add(new PalomitaRancia());
//    	    pool.add(new PalomitaHueca());
//    	    pool.add(new PalomitaEnergetica());
//    	    pool.add(new PalomitaExplosiva());
//
//    	    ArrayList<AbstractCard> result = new ArrayList<>();
//    	    for (int i = 0; i < count; i++) {
//    	        AbstractCard random = pool.get(AbstractDungeon.cardRandomRng.random(pool.size() - 1));
//    	        result.add(random.makeCopy());
//    	    }
//    	    return result;
//    }
//}
    
    public static ArrayList<AbstractCard> getRandomPalomitasUnique(int count) {
        ArrayList<AbstractCard> pool = new ArrayList<>();

        pool.add(new PalomitaDulce());
        pool.add(new PalomitaRancia());
        pool.add(new PalomitaHueca());
        pool.add(new PalomitaEnergetica());
        pool.add(new PalomitaExplosiva());

        // Mezclar y coger las primeras 'count'
        Collections.shuffle(pool, AbstractDungeon.cardRandomRng.random);

        // Asegúrate de no pedir más de los que hay
        return new ArrayList<>(pool.subList(0, Math.min(count, pool.size())));
    }


}

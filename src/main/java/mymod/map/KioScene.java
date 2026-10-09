//package mymod.map;
//
//
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.megacrit.cardcrawl.helpers.ImageMaster;
//import com.megacrit.cardcrawl.scenes.AbstractScene;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//public class KioScene extends AbstractScene {
//    public static final Logger logger = LogManager.getLogger(KioScene.class.getName());
//
//    public KioScene() {
//        super("scenes/atlas.atlas");
//        logger.info("✅ Constructor de KioScene ejecutado correctamente");
//    }
//
//    @Override
//    public void renderCombatRoomBg(SpriteBatch sb) {
//    	 sb.draw(ImageMaster.loadImage("scenes/Arena.png"), 0, 0);
//        logger.info("🎨 renderCombatRoomBg ejecutado");
//    }
//
//
//
//    @Override
//    public void renderCombatRoomFg(SpriteBatch sb) {
//        // Dibujar primer plano de combate, opcional
//    }
//
//    @Override
//    public void renderCampfireRoom(SpriteBatch sb) {
//        // Dibujar fondo de hoguera, opcional
//    }
//
//    @Override
//    public void randomizeScene() {
//        // Puedes poner lógica para cambiar el fondo aleatoriamente, si lo deseas
//    }
//}
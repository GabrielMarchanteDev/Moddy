package mymod.memo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import mymod.MyMod;

/** Habitacion explorable del archivo. Coordenadas normalizadas sobre el fondo. */
public final class MemoTemple {
    public static final int NONE = 0, BAYMAX = 1, KIO = 2, EXIT = 3;
    private final Hitbox[] objects = new Hitbox[4];
    private Texture hall;
    private Texture garden;
    private boolean inGarden;
    private int focus = -1;
    private String description;
    private float fade;

    public MemoTemple() {
        for (int i = 0; i < objects.length; i++) objects[i] = new Hitbox(1F, 1F);
    }

    public void open() {
        inGarden = false;
        focus = -1;
        description = null;
        fade = 0F;
        try {
            if (hall == null) {
                hall = new Texture(Gdx.files.internal("memo/temple-hall.png"));
                hall.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            }
        } catch (RuntimeException e) {
            MyMod.logger.error("[M.E.M.O.] No se pudo cargar el templo", e);
            description = "No se ha podido cargar la imagen. Puedes volver al menú.";
        }
    }

    private void place(int i, float x, float y, float w, float h) {
        objects[i].resize(w * Settings.WIDTH, h * Settings.HEIGHT);
        objects[i].move(x * Settings.WIDTH, y * Settings.HEIGHT);
        objects[i].update();
    }

    private boolean clicked(Hitbox hb) {
        if (!hb.hovered || !InputHelper.justClickedLeft) return false;
        InputHelper.justClickedLeft = false;
        return true;
    }

    public int update() {
        fade = Math.min(1F, fade + Gdx.graphics.getDeltaTime() / 0.7F);
        place(0, 0.16F, 0.46F, 0.27F, 0.30F);
        place(1, 0.83F, 0.52F, 0.15F, 0.44F);
        place(2, 0.50F, 0.51F, 0.26F, 0.48F);
        place(3, 0.50F, 0.08F, 0.20F, 0.065F);
        if (InputHelper.pressedEscape) {
            InputHelper.pressedEscape = false;
            if (inGarden) { inGarden = false; description = null; fade = 0F; return NONE; }
            return EXIT;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) focus = inGarden ? 3 : (focus + 1) % 4;
        boolean enter = Gdx.input.isKeyJustPressed(Input.Keys.ENTER);
        if (clicked(objects[3]) || (enter && focus == 3)) {
            if (inGarden) { inGarden = false; description = null; fade = 0F; return NONE; }
            return EXIT;
        }
        if (inGarden) return NONE;
        if (clicked(objects[0]) || (enter && focus == 0)) {
            if (MemoProgress.hasBaymax()) return BAYMAX;
            description = "El escritorio está en silencio. Observa primero la introducción de Baymax.";
        }
        if (clicked(objects[1]) || (enter && focus == 1)) return KIO;
        if (clicked(objects[2]) || (enter && focus == 2)) {
            inGarden = true; description = null; fade = 0F;
            if (garden == null) {
                try {
                    garden = new Texture(Gdx.files.internal("memo/temple-garden.png"));
                    garden.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                } catch (RuntimeException e) {
                    MyMod.logger.error("[M.E.M.O.] No se pudo cargar el jardín", e);
                    inGarden = false;
                    description = "No se ha podido abrir esta estancia.";
                }
            }
        }
        return NONE;
    }

    public void render(SpriteBatch sb) {
        Color old = sb.getColor().cpy();
        Texture image = inGarden ? garden : hall;
        if (image != null) {
            sb.setColor(Color.WHITE);
            sb.draw(image, 0, 0, Settings.WIDTH, Settings.HEIGHT);
        }
        if (fade < 1F) {
            sb.setColor(0F, 0F, 0F, 1F - fade);
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0, 0, Settings.WIDTH, Settings.HEIGHT);
        }
        String label = inGarden ? "Jardín · La piedra permanece bajo la luna." : "Templo · Explora los objetos y las puertas";
        if (!inGarden) {
            if (objects[0].hovered || focus == 0) label = "Escritorio de Baymax · Examinar recuerdo";
            if (objects[1].hovered || focus == 1) label = MemoProgress.hasKioPrivate()
                    ? "Puerta de Kio · Entrar al recuerdo" : "Puerta de Kio · Abrir con una pista externa";
            if (objects[2].hovered || focus == 2) label = "Arco del jardín · Cruzar";
        }
        sb.setColor(0.02F, 0.025F, 0.03F, 0.8F);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0, 0, Settings.WIDTH, 180F * Settings.scale);
        Color ink = new Color(0.9F, 0.85F, 0.75F, 1F);
        FontHelper.renderFontCentered(sb, FontHelper.tipBodyFont, description == null ? label : description,
                Settings.WIDTH / 2F, 140F * Settings.scale, ink);
        Hitbox back = objects[3];
        sb.setColor(back.hovered || focus == 3 ? new Color(0.27F, 0.3F, 0.32F, 1F) : new Color(0.12F, 0.15F, 0.17F, 1F));
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, back.x, back.y, back.width, back.height);
        FontHelper.renderFontCentered(sb, FontHelper.tipBodyFont, inGarden ? "Volver al templo" : "Volver al menú", back.cX, back.cY, ink);
        sb.setColor(old);
    }

    public void dispose() {
        if (hall != null) { hall.dispose(); hall = null; }
        if (garden != null) { garden.dispose(); garden = null; }
    }
}

package mymod.memo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import java.text.Normalizer;
import java.util.Locale;
import mymod.MyMod;

/** Prototipo de pista externa y recuerdo privado; no modifica partidas. */
public final class KioPrivateMemory {
    private final Hitbox back = new Hitbox(250F * Settings.scale, 60F * Settings.scale);
    private final Hitbox next = new Hitbox(300F * Settings.scale, 60F * Settings.scale);
    private final Hitbox previous = new Hitbox(230F * Settings.scale, 60F * Settings.scale);
    private final StringBuilder input = new StringBuilder();
    private Texture background;
    private String[][] pages;
    private int page;
    private float fade;
    private boolean open;
    private boolean reading;
    private String message;
    private final Color cream = new Color(0.9F, 0.85F, 0.75F, 1F);

    public static boolean accepts(String phrase) {
        String clean = Normalizer.normalize(phrase, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").replaceAll("[^a-zA-Z]", "").toUpperCase(Locale.ROOT);
        return "AUNHAYALGUIEN".equals(clean);
    }

    public boolean isOpen() { return open; }

    public void open() {
        open = true;
        reading = MemoProgress.hasKioPrivate();
        input.setLength(0);
        page = 0;
        fade = 0F;
        message = null;
        if (reading) load();
    }

    private void load() {
        try {
            pages = new Gson().fromJson(Gdx.files.internal("memo/kio-private.json").readString("UTF-8"), String[][].class);
            if (pages == null || pages.length == 0) throw new IllegalStateException("Recuerdo vacio");
            background = new Texture(Gdx.files.internal("memo/kio-private.png"));
            background.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        } catch (RuntimeException e) {
            MyMod.logger.error("[M.E.M.O.] Error en el recuerdo privado", e);
            reading = false;
            message = "No se ha podido abrir el recuerdo. Puedes volver al archivo.";
        }
    }

    private boolean clicked(Hitbox hb) {
        if (!hb.hovered || !InputHelper.justClickedLeft) return false;
        InputHelper.justClickedLeft = false;
        return true;
    }

    public void update() {
        float cx = Settings.WIDTH / 2F;
        back.move(cx, 90F * Settings.scale);
        next.move(cx + 280F * Settings.scale, 180F * Settings.scale);
        previous.move(cx - 280F * Settings.scale, 180F * Settings.scale);
        back.update(); next.update(); previous.update();
        if (InputHelper.pressedEscape || clicked(back)) {
            InputHelper.pressedEscape = false;
            close();
            return;
        }
        fade = Math.min(1F, fade + Gdx.graphics.getDeltaTime() / 0.8F);
        if (reading) {
            if (page > 0 && (clicked(previous) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT))) {
                page--; fade = 0F;
            }
            if (clicked(next) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)
                    || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                if (page + 1 < pages.length) { page++; fade = 0F; }
                else close();
            }
            return;
        }
        for (int key = Input.Keys.A; key <= Input.Keys.Z; key++) {
            if (input.length() < 40 && Gdx.input.isKeyJustPressed(key)) input.append(Input.Keys.toString(key));
        }
        if (input.length() < 40 && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) input.append(' ');
        if (input.length() > 0 && Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) input.deleteCharAt(input.length() - 1);
        if (clicked(next) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (accepts(input.toString())) {
                MemoProgress.unlockKioPrivate();
                reading = true; fade = 0F; message = null; load();
            } else message = "El fragmento no coincide. Revisa la pista que encontraste.";
        }
    }

    private void close() {
        if (background != null) { background.dispose(); background = null; }
        pages = null;
        open = false;
        InputHelper.justClickedLeft = false;
    }

    public void render(SpriteBatch sb) {
        Color old = sb.getColor().cpy();
        float cx = Settings.WIDTH / 2F, cy = Settings.HEIGHT / 2F;
        sb.setColor(0.025F, 0.035F, 0.045F, 1F);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0, 0, Settings.WIDTH, Settings.HEIGHT);
        if (reading && background != null) {
            sb.setColor(1F, 1F, 1F, 0.65F);
            sb.draw(background, 0, 0, Settings.WIDTH, Settings.HEIGHT);
            sb.setColor(0.015F, 0.02F, 0.025F, 0.85F);
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, cx - 650F * Settings.scale, cy - 130F * Settings.scale,
                    1300F * Settings.scale, 430F * Settings.scale);
        }
        text(sb, "KIO · ARCHIVO PRIVADO", cx, cy + 330F * Settings.scale, cream);
        if (reading) {
            Color ink = cream.cpy(); ink.a = fade;
            String[] lines = pages[page];
            for (int i = 0; i < lines.length; i++) text(sb, lines[i], cx,
                    cy + (210F - i * 52F) * Settings.scale, ink);
            text(sb, (page + 1) + " / " + pages.length, cx, 180F * Settings.scale, cream);
            if (page > 0) button(sb, previous, "Anterior");
            button(sb, next, page + 1 < pages.length ? "Continuar" : "Volver al archivo");
        } else {
            text(sb, "Este fragmento necesita una clave externa.", cx, cy + 130F * Settings.scale, cream);
            text(sb, "Escribe la frase de la pista y pulsa Enter.", cx, cy + 70F * Settings.scale, Color.LIGHT_GRAY);
            text(sb, input.length() == 0 ? "_" : input.toString() + "_", cx, cy - 15F * Settings.scale, Settings.GOLD_COLOR);
            if (message != null) text(sb, message, cx, cy - 100F * Settings.scale, cream);
            button(sb, next, "Abrir fragmento");
        }
        button(sb, back, "Volver al archivo");
        sb.setColor(old);
    }

    private void text(SpriteBatch sb, String value, float x, float y, Color color) {
        FontHelper.renderFontCentered(sb, FontHelper.tipBodyFont, value, x, y, color);
    }

    private void button(SpriteBatch sb, Hitbox hb, String label) {
        sb.setColor(hb.hovered ? new Color(0.28F, 0.31F, 0.32F, 1F) : new Color(0.12F, 0.15F, 0.18F, 1F));
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, hb.x, hb.y, hb.width, hb.height);
        text(sb, label, hb.cX, hb.cY, cream);
    }
}

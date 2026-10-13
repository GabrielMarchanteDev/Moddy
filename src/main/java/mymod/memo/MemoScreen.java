package mymod.memo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import mymod.MyMod;
import mymod.events.BaymaxHistory;

/** Visor de recuerdos sobre el menu: no crea ni abandona partidas. */
public final class MemoScreen {
    private static MemoScreen instance;
    private final Hitbox entry = new Hitbox(240F * Settings.scale, 64F * Settings.scale);
    private final Hitbox replay = new Hitbox(300F * Settings.scale, 64F * Settings.scale);
    private final Hitbox back = new Hitbox(240F * Settings.scale, 64F * Settings.scale);
    private final Color accent = new Color(0.35F, 0.9F, 0.85F, 1F);
    private boolean open;
    private Texture thumbnail;
    private BaymaxHistory playback;
    private String message;
    private int keyboardFocus;

    private MemoScreen() { }
    public static MemoScreen get() {
        if (instance == null) instance = new MemoScreen();
        return instance;
    }

    public boolean update(MainMenuScreen menu) {
        if (!open) {
            if (!available(menu)) return false;
            entry.move(Settings.WIDTH - 160F * Settings.scale, 140F * Settings.scale);
            entry.update();
            if (clicked(entry)) {
                open = true;
                message = null;
                keyboardFocus = 0;
                loadThumbnail();
                return true;
            }
            return false;
        }
        back.move(Settings.WIDTH / 2F, 110F * Settings.scale);
        back.update();
        if (InputHelper.pressedEscape || clicked(back)) {
            InputHelper.pressedEscape = false;
            if (playback != null) stopPlayback(); else close();
            return true;
        }
        if (playback != null) {
            try {
                playback.update();
                if (playback.isDone) stopPlayback();
            } catch (RuntimeException e) {
                MyMod.logger.error("[M.E.M.O.] Error al reproducir", e);
                stopPlayback();
                message = "No se ha podido reproducir este recuerdo.";
            }
            return true;
        }
        replay.move(Settings.WIDTH / 2F - 230F * Settings.scale, Settings.HEIGHT / 2F - 155F * Settings.scale);
        replay.update();
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) keyboardFocus = 1 - keyboardFocus;
        boolean enter = Gdx.input.isKeyJustPressed(Input.Keys.ENTER);
        if (enter && keyboardFocus == 1) close();
        else if (MemoProgress.hasBaymax() && (clicked(replay) || (enter && keyboardFocus == 0))) startPlayback();
        return true;
    }

    private boolean available(MainMenuScreen menu) {
        return menu.screen == MainMenuScreen.CurScreen.MAIN_MENU && !menu.isSettingsUp
                && !menu.isFadingOut && !menu.fadedOut;
    }

    private boolean clicked(Hitbox hb) {
        if (!hb.hovered || !InputHelper.justClickedLeft) return false;
        InputHelper.justClickedLeft = false;
        return true;
    }

    private void loadThumbnail() {
        if (!MemoProgress.hasBaymax() || thumbnail != null) return;
        try {
            thumbnail = new Texture(Gdx.files.internal("scenes/baymax/1.png"));
            thumbnail.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        } catch (RuntimeException e) {
            MyMod.logger.error("[M.E.M.O.] No se pudo cargar la miniatura", e);
        }
    }

    private void releaseThumbnail() {
        if (thumbnail != null) { thumbnail.dispose(); thumbnail = null; }
    }

    private void startPlayback() {
        message = null;
        releaseThumbnail();
        try {
            CardCrawlGame.music.silenceBGMInstantly();
            playback = new BaymaxHistory();
        } catch (RuntimeException e) {
            MyMod.logger.error("[M.E.M.O.] No se pudo abrir el recuerdo", e);
            restoreMenuMusic();
            loadThumbnail();
            message = "No se ha podido abrir este recuerdo.";
        }
    }

    private void restoreMenuMusic() {
        CardCrawlGame.music.silenceTempBgmInstantly();
        CardCrawlGame.music.unsilenceBGM();
    }

    private void stopPlayback() {
        if (playback != null) { playback.dispose(); playback = null; }
        restoreMenuMusic();
        loadThumbnail();
    }

    private void close() {
        releaseThumbnail();
        open = false;
        InputHelper.justClickedLeft = false;
    }

    public void render(MainMenuScreen menu, SpriteBatch sb) {
        Color previous = sb.getColor().cpy();
        if (!open) {
            // Acceso oculto: update conserva la zona de clic, sin texto ni resaltado.
            return;
        }
        rectangle(sb, 0, 0, Settings.WIDTH, Settings.HEIGHT, new Color(0.025F, 0.045F, 0.07F, 1F));
        if (playback != null) {
            playback.render(sb);
            button(sb, back, "Volver al archivo", true, false);
            sb.setColor(previous);
            return;
        }
        float cx = Settings.WIDTH / 2F;
        float cy = Settings.HEIGHT / 2F;
        text(sb, FontHelper.panelNameFont, "M.E.M.O.", cx, cy + 300F * Settings.scale, accent);
        text(sb, FontHelper.tipBodyFont, "Archivo de recuerdos", cx, cy + 250F * Settings.scale, Color.LIGHT_GRAY);
        boolean seen = MemoProgress.hasBaymax();
        card(sb, cx - 230F * Settings.scale, cy, seen);
        card(sb, cx + 230F * Settings.scale, cy, false);
        if (thumbnail != null) {
            sb.setColor(Color.WHITE);
            sb.draw(thumbnail, cx - 425F * Settings.scale, cy - 25F * Settings.scale,
                    390F * Settings.scale, 205F * Settings.scale);
        }
        text(sb, FontHelper.buttonLabelFont, seen ? "Baymax" : "Recuerdo bloqueado",
                cx - 230F * Settings.scale, cy + 215F * Settings.scale, seen ? accent : Color.LIGHT_GRAY);
        if (!seen) {
            text(sb, FontHelper.tipBodyFont, "Inicia una partida con Baymax",
                    cx - 230F * Settings.scale, cy + 75F * Settings.scale, Color.LIGHT_GRAY);
            text(sb, FontHelper.tipBodyFont, "y observa su introducción.",
                    cx - 230F * Settings.scale, cy + 30F * Settings.scale, Color.LIGHT_GRAY);
        }
        button(sb, replay, seen ? "Reproducir recuerdo" : "Bloqueado", seen, keyboardFocus == 0);
        text(sb, FontHelper.buttonLabelFont, "Archivo 02", cx + 230F * Settings.scale,
                cy + 215F * Settings.scale, Color.LIGHT_GRAY);
        text(sb, FontHelper.tipBodyFont, "Sin contenido disponible", cx + 230F * Settings.scale,
                cy + 50F * Settings.scale, Color.GRAY);
        text(sb, FontHelper.tipBodyFont, "Bloqueado", cx + 230F * Settings.scale,
                cy - 155F * Settings.scale, Color.GRAY);
        text(sb, FontHelper.tipBodyFont, seen ? "Recuerdo disponible · puedes volver a verlo" : "0 recuerdos disponibles",
                cx, cy - 255F * Settings.scale, Color.LIGHT_GRAY);
        if (message != null) text(sb, FontHelper.tipBodyFont, message, cx, cy - 295F * Settings.scale, Settings.GOLD_COLOR);
        button(sb, back, "Volver al menú", true, keyboardFocus == 1);
        text(sb, FontHelper.tipBodyFont, "Tab: cambiar botón · Enter: seleccionar · Esc: volver", cx,
                45F * Settings.scale, Color.GRAY);
        sb.setColor(previous);
    }

    private void card(SpriteBatch sb, float x, float y, boolean enabled) {
        float s = Settings.scale;
        rectangle(sb, x - 215F * s, y - 205F * s, 430F * s, 450F * s,
                enabled ? new Color(0.045F, 0.12F, 0.15F, 1F) : new Color(0.06F, 0.08F, 0.1F, 1F));
        rectangle(sb, x - 215F * s, y + 242F * s, 430F * s, 3F * s, enabled ? accent : Color.GRAY);
    }

    private void button(SpriteBatch sb, Hitbox hb, String label, boolean enabled, boolean focused) {
        Color color = enabled && (hb.hovered || focused) ? accent : new Color(0.1F, 0.2F, 0.25F, 1F);
        rectangle(sb, hb.x, hb.y, hb.width, hb.height, color);
        text(sb, FontHelper.buttonLabelFont, label, hb.cX, hb.cY,
                enabled && (hb.hovered || focused) ? Color.BLACK : Color.LIGHT_GRAY);
    }

    private void rectangle(SpriteBatch sb, float x, float y, float width, float height, Color color) {
        sb.setColor(color);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, x, y, width, height);
    }

    private void text(SpriteBatch sb, BitmapFont font, String value, float x, float y, Color color) {
        FontHelper.renderFontCentered(sb, font, value, x, y, color);
    }
}

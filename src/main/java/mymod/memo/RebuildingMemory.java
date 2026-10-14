package mymod.memo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.LouseNormal;
import com.megacrit.cardcrawl.monsters.exordium.LouseDefensive;
import mymod.characters.Poro;
import mymod.MyMod;

/** Prueba optativa: reconstruccion visual ligada a acciones reales del combate. */
public final class RebuildingMemory {
    private static boolean armed;
    private static AbstractRoom owner;
    private static MemoryReconstructionState state;
    private static Texture damaged, recovered;
    private static ShaderProgram shader;
    private static Music ambience;
    private static float clarity, detail;
    private static int lastFragments;
    private static boolean shaderWarning;
    private static AbstractRoom autoChecked;
    private static int initialLife;
    private RebuildingMemory() { }

    public static String arm() {
        dispose();
        armed = true;
        if (inCombat()) {
            begin(AbstractDungeon.getCurrRoom());
            return owner == null ? "No se pudo cargar la prueba; consulta el registro." : "Recuerdo activado: se restaura al debilitar a los enemigos.";
        }
        return "Recuerdo preparado para el siguiente combate.";
    }

    private static boolean inCombat() {
        return CardCrawlGame.mode == CardCrawlGame.GameMode.GAMEPLAY
                && AbstractDungeon.player != null && AbstractDungeon.getCurrMapNode() != null
                && AbstractDungeon.getCurrRoom() != null
                && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
    }

    public static void begin(AbstractRoom room) {
        if (!armed) return;
        armed = false;
        try {
            damaged = new Texture(Gdx.files.internal("memo/rebuilding-before.png"));
            recovered = new Texture(Gdx.files.internal("memo/rebuilding-after.png"));
            damaged.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            recovered.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            owner = room; state = new MemoryReconstructionState(); clarity = 0F; detail = 0F; lastFragments = 0;
            initialLife = 0;
            for (AbstractMonster monster : room.monsters.monsters) initialLife += Math.max(1, monster.maxHealth);
            try {
                ambience = Gdx.audio.newMusic(Gdx.files.internal("audio/sound/SOTE_SFX_WindAmb_v1.ogg"));
                ambience.setLooping(true); ambience.setVolume(0F); ambience.play();
            } catch (RuntimeException e) {
                MyMod.logger.error("[Recuerdo] Ambiente no disponible; prueba visual sigue activa", e);
            }
            MyMod.logger.info("[Recuerdo] Reconstruccion iniciada");
        } catch (RuntimeException e) {
            MyMod.logger.error("[Recuerdo] No se pudo cargar la reconstruccion", e);
            dispose();
        }
    }

    public static boolean visible() {
        return owner != null && state != null && CardCrawlGame.mode == CardCrawlGame.GameMode.GAMEPLAY
                && AbstractDungeon.getCurrMapNode() != null && owner == AbstractDungeon.getCurrRoom();
    }

    public static void update() {
        if (owner != null && !visible()) dispose();
        if (inCombat()) {
            AbstractRoom room = AbstractDungeon.getCurrRoom();
            if (room != autoChecked) {
                autoChecked = room;
                boolean selected = AbstractDungeon.player instanceof Poro && room.monsters != null && room.monsters.monsters.size() == 2;
                if (selected) for (AbstractMonster monster : room.monsters.monsters)
                    if (!(monster instanceof LouseNormal) && !(monster instanceof LouseDefensive)) selected = false;
                if (selected && owner == null) armed = true;
            }
        } else if (CardCrawlGame.mode != CardCrawlGame.GameMode.GAMEPLAY) autoChecked = null;
        if (armed && inCombat()) begin(AbstractDungeon.getCurrRoom());
        if (owner == null) return;
        if (!visible() || AbstractDungeon.player == null || AbstractDungeon.player.isDead || AbstractDungeon.player.isDying) {
            dispose(); return;
        }
        int remaining = 0;
        if (owner.monsters != null) for (AbstractMonster monster : owner.monsters.monsters)
            remaining += Math.max(0, monster.currentHealth);
        state.observeLife(remaining, initialLife);
        float delta = Gdx.graphics.getDeltaTime();
        float target = Math.min(1F, state.progress() / 0.75F);
        clarity += (target - clarity) * Math.min(1F, delta * 2F);
        detail += ((state.complete() ? 1F : 0F) - detail) * Math.min(1F, delta * 1.5F);
        if (ambience != null) ambience.setVolume(0.3F * clarity * Settings.MASTER_VOLUME * Settings.MUSIC_VOLUME);
    }

    private static ShaderProgram shader() {
        if (shader != null || shaderWarning) return shader;
        String vertex = "attribute vec4 a_position; attribute vec4 a_color; attribute vec2 a_texCoord0;"
                + "uniform mat4 u_projTrans; varying vec4 v_color; varying vec2 v_uv;"
                + "void main(){v_color=a_color;v_color.a*=255.0/254.0;v_uv=a_texCoord0;gl_Position=u_projTrans*a_position;}";
        String fragment = "#ifdef GL_ES\nprecision mediump float;\n#endif\n"
                + "varying vec4 v_color; varying vec2 v_uv; uniform sampler2D u_texture;"
                + "uniform float u_clarity; uniform vec2 u_texel;"
                + "void main(){vec2 d=u_texel*(1.0-u_clarity)*12.0;"
                + "vec4 c=texture2D(u_texture,v_uv)*0.2;"
                + "c+=texture2D(u_texture,v_uv+d)*0.2;c+=texture2D(u_texture,v_uv-d)*0.2;"
                + "c+=texture2D(u_texture,v_uv+vec2(d.x,-d.y))*0.2;"
                + "c+=texture2D(u_texture,v_uv+vec2(-d.x,d.y))*0.2;"
                + "float g=dot(c.rgb,vec3(0.299,0.587,0.114));"
                + "vec3 rgb=mix(vec3(g),c.rgb,u_clarity);gl_FragColor=vec4(rgb,c.a)*v_color;}";
        shader = new ShaderProgram(vertex, fragment);
        if (!shader.isCompiled()) {
            MyMod.logger.error("[Recuerdo] Shader no disponible: " + shader.getLog());
            shader.dispose(); shader = null; shaderWarning = true;
        }
        return shader;
    }

    public static boolean renderBackground(SpriteBatch sb) {
        if (!visible() || damaged == null) return false;
        Color color = sb.getColor().cpy(); ShaderProgram oldShader = sb.getShader();
        try {
            ShaderProgram filter = shader();
            if (filter != null) {
                sb.setShader(filter);
                filter.setUniformf("u_clarity", clarity);
                filter.setUniformf("u_texel", 1F / damaged.getWidth(), 1F / damaged.getHeight());
            }
            sb.setColor(Color.WHITE);
            sb.draw(damaged, 0, 0, Settings.WIDTH, Settings.HEIGHT);
            if (detail > 0.001F) {
                sb.setColor(1F, 1F, 1F, detail);
                sb.draw(recovered, 0, 0, Settings.WIDTH, Settings.HEIGHT);
            }
        } finally { sb.setShader(oldShader); sb.setColor(color); }
        return true;
    }

    public static void renderHud(SpriteBatch sb) {
        // Sin texto de guia: la reconstruccion se comunica solo mediante la escena.
    }

    public static void cancel() { armed = false; dispose(); }
    private static void dispose() {
        if (ambience != null) { ambience.stop(); ambience.dispose(); ambience = null; }
        if (damaged != null) { damaged.dispose(); damaged = null; }
        if (recovered != null) { recovered.dispose(); recovered = null; }
        if (shader != null) { shader.dispose(); shader = null; }
        owner = null; state = null; clarity = 0F; detail = 0F; lastFragments = 0; shaderWarning = false;
    }
}

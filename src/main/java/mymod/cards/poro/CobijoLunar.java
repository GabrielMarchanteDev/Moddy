package mymod.cards.poro;

import basemod.abstracts.CustomCard;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import mymod.characters.Poro;
import mymod.characters.ui.LunarPhase;
import mymod.characters.ui.LunarCardHalo;
import mymod.characters.ui.SlowLunarFlash;

public class CobijoLunar extends CustomCard {
    public static final String ID = "mymod:CobijoLunar";
    private static final CardStrings TEXT = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final Color MOON_GLOW = new Color(0.82F, 0.91F, 1F, 1F);
    private boolean lunarActive;
    private float haloTime;
    private float transformation;
    private float lunarFlashTimer;
    private static final float TRANSFORM_DURATION = 0.65F;
    private static final float LUNAR_FLASH_INTERVAL = SlowLunarFlash.TOTAL_TIME + 0.15F;

    private void startLunarFlash() {
        ReflectionHacks.setPrivate(this, AbstractCard.class, "flashVfx", new SlowLunarFlash(this, MOON_GLOW));
        lunarFlashTimer = LUNAR_FLASH_INTERVAL;
    }

    public CobijoLunar() {
        super(ID, TEXT.NAME, "images/cards/cobijoLunar.png", 1, TEXT.DESCRIPTION,
                CardType.SKILL, PoroCardColor.PORO_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = block = 6;
        magicNumber = baseMagicNumber = 6;
    }

    private boolean fullMoon() {
        return AbstractDungeon.player instanceof Poro && AbstractDungeon.getCurrRoom() != null
                && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
                && ((Poro) AbstractDungeon.player).getLunarPhase() == LunarPhase.LLENA;
    }

    private void syncMoon() {
        boolean full = fullMoon();
        baseBlock = (upgraded ? 9 : 6) + (full ? baseMagicNumber : 0);
        if (full != lunarActive) {
            lunarActive = full;
            name = (full ? TEXT.EXTENDED_DESCRIPTION[0] : TEXT.NAME) + (upgraded ? "+" : "");
            rawDescription = full ? TEXT.EXTENDED_DESCRIPTION[1] : TEXT.DESCRIPTION;
            loadCardImage(full ? "images/cards/cobijoLunarLlena.png" : "images/cards/cobijoLunar.png");
            if (full && inHand()) {
                transformation = TRANSFORM_DURATION;
                startLunarFlash();
            }
            initializeTitle();
            initializeDescription();
            stopGlowing();
        }
        glowColor = full ? MOON_GLOW.cpy() : BLUE_BORDER_GLOW_COLOR.cpy();
    }

    @Override public void update() {
        boolean changed = fullMoon() != lunarActive;
        if (changed) applyPowers();
        super.update();
        haloTime = (haloTime + Gdx.graphics.getDeltaTime()) % 28F;
        transformation = Math.max(0F, transformation - Gdx.graphics.getDeltaTime());
        if (lunarActive && inHand()) {
            lunarFlashTimer -= Gdx.graphics.getDeltaTime();
            if (lunarFlashTimer <= 0F) {
                startLunarFlash();
            }
        } else {
            lunarFlashTimer = 0F;
        }
    }

    private boolean inHand() {
        return AbstractDungeon.player != null && AbstractDungeon.player.hand.contains(this);
    }

    @Override public void render(SpriteBatch sb, boolean selected) {
        if (!lunarActive || !inHand() || com.megacrit.cardcrawl.core.Settings.hideCards) {
            super.render(sb, selected);
            return;
        }
        float originalScale = drawScale;
        float progress = 1F - transformation / TRANSFORM_DURATION;
        drawScale *= 1F + 0.0525F * MathUtils.sin(progress * MathUtils.PI);
        try {
            LunarCardHalo.render(sb, this, haloTime, transformation / TRANSFORM_DURATION);
            super.render(sb, selected);
            if (transformation > 0F) LunarCardHalo.renderSweep(sb, this, progress);
        } finally { drawScale = originalScale; }
    }

    @Override public void applyPowers() {
        syncMoon();
        super.applyPowers();
    }

    @Override public void calculateCardDamage(AbstractMonster monster) {
        syncMoon();
        super.calculateCardDamage(monster);
    }

    @Override public void triggerOnGlowCheck() {
        glowColor = fullMoon() ? MOON_GLOW.cpy() : BLUE_BORDER_GLOW_COLOR.cpy();
        if (fullMoon()) beginGlowing();
    }

    @Override public void use(AbstractPlayer player, AbstractMonster monster) {
        applyPowers();
        addToBot(new GainBlockAction(player, player, block));
        if (lunarActive) addToBot(new DrawCardAction(player, 1));
    }

    @Override public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradedBlock = true;
            syncMoon();
            block = baseBlock;
            initializeDescription();
        }
    }

    @Override public AbstractCard makeCopy() { return new CobijoLunar(); }
}

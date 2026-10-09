package mymod.cards.kio;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import basemod.abstracts.CustomCard;

public class Peppazo extends CustomCard {
    public static final String ID = "mymod:Peppazo";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "images/cards/Peppazo.png";

    private static final int COST = 0;
    private static final int ATTACK_DMG = 42;
    private static final int UPGRADE_PLUS_DMG = 5;
    private static final int VULNERABLE_AMT = 1;
    private static final int UPGRADE_PLUS_VULNERABLE = 1;

    public Peppazo() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.ATTACK, KioCardColor.KIO_COLOR,
                AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.ENEMY);
        this.magicNumber = this.baseMagicNumber = VULNERABLE_AMT;
        this.damage = this.baseDamage = ATTACK_DMG;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                        AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(m, p, new VulnerablePower(m, this.magicNumber, false),
                        this.magicNumber, true, AbstractGameAction.AttackEffect.NONE));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Peppazo();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DMG);
            this.upgradeMagicNumber(UPGRADE_PLUS_VULNERABLE);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);

        // Mostrar solo si la carta está visible en pantalla
        if (this.current_x < -1000f || this.isLocked || !this.isSeen) return;

        // Texto glitch generado dinámicamente
        String glitchText = generateGlitchText(8); // Cambia el 8 por la longitud que desees

        // Movimiento tembloroso
        float dx = MathUtils.random(-1.5f, 1.5f) * this.drawScale;
        float dy = MathUtils.random(-1.5f, 1.5f) * this.drawScale;

        FontHelper.renderRotatedText(
                sb,
                FontHelper.cardTitleFont,
                glitchText,
                this.current_x + dx,
                this.current_y + 120.0f * this.drawScale + dy,
                0.0f,
                0.0f,
                this.angle,
                true,
                Settings.RED_TEXT_COLOR
        );
    }

    // 🔣 Generador de texto glitch
    private String generateGlitchText(int length) {
        String symbols = "!@#$%^&*()_+=<>?ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = MathUtils.random(symbols.length() - 1);
            sb.append(symbols.charAt(index));
        }
        return sb.toString();
    }

}

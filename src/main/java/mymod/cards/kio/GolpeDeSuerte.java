package mymod.cards.kio;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import mymod.MyMod;
import mymod.characters.Kio;

public class GolpeDeSuerte extends CustomCard {
	public static boolean isIn85Mode = false;
    public static final String ID = MyMod.makeID("GolpeDeSuerte");
    public static final String NAME = "Golpe de Suerte";
    public static final String DESCRIPTION = "Inflige !D! de daño. Si mata al objetivo, obtienes 3 de Suerte. Agota";
    public static final String IMG_PATH = "images/cards/racionDoble.png";/// pon aquí la ruta a tu imagen

    private static final int COST = 1;
    private static final int DAMAGE = 10;
    private static final int LUCK_GAIN = 3;

    public GolpeDeSuerte() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, KioCardColor.KIO_COLOR,
                CardRarity.COMMON, CardTarget.ENEMY);
        this.exhaust = true;
        this.baseDamage = DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(
                    m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
            ));

            // Espera un momento para asegurarse de que el enemigo reciba el daño
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1F));

            // Luego chequea si el enemigo murió
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    if (m.isDying || m.currentHealth <= 0 && !m.halfDead) {
                        if (p instanceof Kio) {
                            ((Kio) p).addKioLuck(LUCK_GAIN);
                        }
                    }
                    this.isDone = true;
                }
            });
        }
    }
    /*
     * @Override
public void use(AbstractPlayer p, AbstractMonster m) {
    if (m != null) {
        AbstractDungeon.actionManager.addToBottom(
            new LuckyDamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), LUCK_GAIN)
        );
    }
}

     */

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(4); // sube a 14 de daño al mejorar
        }
    }

    @Override
    public CustomCard makeCopy() {
        return new GolpeDeSuerte();
    }
}
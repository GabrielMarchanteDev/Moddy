package mymod.relics.palomitero.palomitas.menores;

import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

import basemod.abstracts.CustomCard;
import mymod.cards.kio.KioCardColor;

public class PalomitaExplosiva extends CustomCard {
    public static final String ID = "PalomitaExplosiva";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "images/cards/palomitas/Palomita4.png";
    private static final int COST = 0;
    private static final int DAMAGE = 1;
    private static final int HITS = 2;
    private static final int UPGRADE_PLUS_HITS = 1;	
    

    public PalomitaExplosiva() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
              CardType.ATTACK, KioCardColor.KIO_COLOR,
              CardRarity.BASIC, CardTarget.ALL_ENEMY);
        this.baseDamage = DAMAGE;
        this.isMultiDamage = true;
        this.baseMagicNumber = HITS;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
        this.isEthereal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < this.magicNumber; i++) {
            AbstractDungeon.actionManager.addToBottom(
                new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, 
                                            AbstractGameAction.AttackEffect.FIRE)
            );
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_HITS); // Golpea una vez más
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new PalomitaExplosiva();
    }
}

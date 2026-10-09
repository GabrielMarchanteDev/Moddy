package mymod.relics.palomitero.palomitas.mayores;


import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import mymod.cards.kio.KioCardColor;

public class PalomitaMayorExplosiva extends CustomCard {
    public static final String ID = "PalomitaMayorExplosiva";
  	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "images/cards/palomitas/Palomita4.png";

    private static final int COST = 1;
    private static final int DAMAGE = 2;
    private static final int HITS = 3;
    private static final int UPGRADE_PLUS_HITS = 2;	

    public PalomitaMayorExplosiva() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
              CardType.ATTACK, KioCardColor.KIO_COLOR,
              CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.baseDamage = DAMAGE;
        this.isMultiDamage = true;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < HITS; i++) {
            AbstractDungeon.actionManager.addToBottom(
                new DamageAllEnemiesAction(
                    p,
                    this.multiDamage,
                    DamageInfo.DamageType.NORMAL,
                    AttackEffect.FIRE
                )
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
        return new PalomitaMayorExplosiva();
    }
}
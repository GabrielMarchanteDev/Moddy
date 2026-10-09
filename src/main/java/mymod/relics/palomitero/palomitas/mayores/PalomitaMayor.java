package mymod.relics.palomitero.palomitas.mayores;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.unique.ExhaustAllNonAttackAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.AbstractCard.CardTarget;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.DoubleDamagePower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import basemod.abstracts.CustomCard;
import mymod.cards.kio.KioCardColor;

public class PalomitaMayor extends CustomCard {
    public static final String ID = "PalomitaMayor";

  	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
      public static final String NAME = cardStrings.NAME;
      public static final String DESCRIPTION = cardStrings.DESCRIPTION;
      
      public static final String IMG_PATH = "images/cards/palomitas/Palomita4.png";
      private static final int COST = 1;
      
    public PalomitaMayor() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.SKILL, KioCardColor.KIO_COLOR, CardRarity.UNCOMMON, CardTarget.SELF);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int roll = AbstractDungeon.cardRandomRng.random(6);
        switch (roll) {
            case 0:
                AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(2));
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, DamageInfo.createDamageMatrix(20, true), DamageInfo.DamageType.THORNS, AttackEffect.FIRE));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, 3)));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DexterityPower(p, 3)));
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, 1)));
                break;
            case 4:
                AbstractDungeon.actionManager.addToBottom(new ExhaustAllNonAttackAction()); // ejemplo raro
                AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 5));
                break;
            case 5:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DoubleDamagePower(p, 1, false)));
                break;
            case 6:
                AbstractDungeon.actionManager.addToBottom(new HealAction(p, p, 10));
                break;
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new PalomitaMayor();
    }

    @Override
    public void upgrade() {}
}

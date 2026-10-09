package mymod.cards.poro;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;

public class Armonia extends CustomCard {

    public static final String ID = "Armonia";
    public static final String IMG_PATH = "images/cards/attack.png";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = PoroCardColor.PORO_COLOR;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int DAMAGE = 8;
    private static final int BLOCK = 8;

    public Armonia() {
        super(ID, "Armonía", IMG_PATH, COST,
                "Inflige !D! de daño. Gana !B! de Bloque.",
                TYPE, COLOR, RARITY, TARGET);

        this.baseDamage = DAMAGE;
        this.baseBlock = BLOCK;
        this.exhaust = true;
        this.isEthereal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        addToBot(new DamageAction(m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn)));

        addToBot(new GainBlockAction(p, p, this.block));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Armonia();
    }

	@Override
	public void upgrade() {
		// TODO Auto-generated method stub
		
	}
}

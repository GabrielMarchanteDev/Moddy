package mymod.cards.kio;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardColor.BLUE;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardColor.GREEN;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardColor.PURPLE;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardColor.RED;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardRarity.BASIC;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardRarity.RARE;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardRarity.SPECIAL;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.SKILL;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardType.STATUS;

import java.util.ArrayList;
import java.util.Collections;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import mymod.util.FlexibleDiscoveryAction;

public class EchoFromOthers extends CustomCard {
	public static boolean isIn85Mode = false;
    public static final String ID = "EchoFromOthers";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String IMG_PATH = "images/cards/echoes.png";
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardType TYPE = SKILL;
    private static final CardColor COLOR = KioCardColor.KIO_COLOR;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final int COST = 1;

    public EchoFromOthers() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> choices = new ArrayList<>();

        AbstractCard ironcladCard = getRandomCardFromColor(RED);
        AbstractCard silentCard   = getRandomCardFromColor(GREEN);
        AbstractCard defectCard   = getRandomCardFromColor(BLUE);
        AbstractCard watcherCard  = getRandomCardFromColor(PURPLE);

        if (ironcladCard != null) choices.add(ironcladCard);
        if (silentCard != null)   choices.add(silentCard);
        if (defectCard != null)   choices.add(defectCard);
        if (watcherCard != null)  choices.add(watcherCard);

        AbstractDungeon.actionManager.addToBottom(
        	    new FlexibleDiscoveryAction(choices, (AbstractCard chosen) -> {
        	        chosen.exhaust = true;
        	    }, true)
        	);

    }

    private AbstractCard getRandomCardFromColor(CardColor color) {
        ArrayList<AbstractCard> pool = new ArrayList<>();

        for (AbstractCard c : CardLibrary.getAllCards()) {
            if (c.color == color &&
                c.rarity != BASIC &&
                c.rarity != SPECIAL &&
                c.type != STATUS) {
                pool.add(c.makeCopy());
            }
        }

        if (pool.isEmpty()) return null;

        long seed = AbstractDungeon.cardRandomRng.randomLong();
        Collections.shuffle(pool, new java.util.Random(seed));
        return pool.get(0);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.exhaust = false; // mejora: no se agota
            this.rawDescription = "Elige 1 carta entre 4: una de cada héroe original. Cuesta 0 este turno.";
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new EchoFromOthers();
    }
}

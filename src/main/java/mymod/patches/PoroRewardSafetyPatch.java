package mymod.patches;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import mymod.characters.Poro;
import mymod.MyMod;

/** Evita reintentos infinitos cuando la reserva pequena no admite opciones distintas. */
@SpirePatch(clz = AbstractDungeon.class, method = "getRewardCards")
public class PoroRewardSafetyPatch {
    @SpirePrefixPatch public static SpireReturn<ArrayList<AbstractCard>> Prefix() {
        if (!(AbstractDungeon.player instanceof Poro) || AbstractDungeon.player.hasRelic("PrismaticShard")) return SpireReturn.Continue();
        int count = 3;
        for (AbstractRelic relic : AbstractDungeon.player.relics) count = relic.changeNumberOfCardsInReward(count);
        if (ModHelper.isModEnabled("Binary")) count--;
        CardGroup[] pools = {AbstractDungeon.commonCardPool, AbstractDungeon.uncommonCardPool, AbstractDungeon.rareCardPool};
        boolean small = false;
        LinkedHashMap<String, AbstractCard> unique = new LinkedHashMap<>();
        for (CardGroup pool : pools) {
            LinkedHashMap<String, AbstractCard> tier = new LinkedHashMap<>();
            if (pool != null) for (AbstractCard card : pool.group) {
                tier.put(card.cardID, card); unique.put(card.cardID, card);
            }
            if (tier.size() < count) small = true;
        }
        if (!small) return SpireReturn.Continue();
        ArrayList<AbstractCard> remaining = new ArrayList<>(unique.values());
        ArrayList<AbstractCard> result = new ArrayList<>();
        float upgradeChance = ReflectionHacks.getPrivateStatic(AbstractDungeon.class, "cardUpgradedChance");
        int requested = Math.max(0, count);
        for (int i = 0; i < requested && !remaining.isEmpty(); i++) {
            AbstractCard.CardRarity rarity = AbstractDungeon.rollRarity();
            ArrayList<AbstractCard> candidates = new ArrayList<>();
            for (AbstractCard card : remaining) if (card.rarity == rarity) candidates.add(card);
            if (candidates.isEmpty()) candidates.addAll(remaining);
            AbstractCard source = candidates.get(AbstractDungeon.cardRng.random(candidates.size() - 1));
            remaining.remove(source);
            AbstractCard copy = source.makeCopy();
            if (copy.rarity != AbstractCard.CardRarity.RARE && AbstractDungeon.cardRng.randomBoolean(upgradeChance) && copy.canUpgrade()) copy.upgrade();
            else for (AbstractRelic relic : AbstractDungeon.player.relics) relic.onPreviewObtainCard(copy);
            result.add(copy);
        }
        MyMod.logger.info("[Recompensas Poro] Reserva segura: " + unique.size() + " cartas distintas; " + result.size() + " opciones");
        return SpireReturn.Return(result);
    }
}

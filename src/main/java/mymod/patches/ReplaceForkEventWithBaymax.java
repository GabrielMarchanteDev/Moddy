package mymod.patches;


import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.EventRoom;

import actlikeit.events.GetForked;
import mymod.events.BaymaxCheckEvent;

@SpirePatch(clz = GetForked.class, method = SpirePatch.CONSTRUCTOR)
public class ReplaceForkEventWithBaymax {

    @SpirePrefixPatch
    public static void replaceForked(GetForked __instance, boolean afterdoor) {
        AbstractDungeon.currMapNode.room = new EventRoom();
        AbstractDungeon.currMapNode.room.event = new BaymaxCheckEvent(); // usa tu clase
        AbstractDungeon.currMapNode.room.event.onEnterRoom();
    }
}

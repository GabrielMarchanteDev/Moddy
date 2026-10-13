package mymod.patches;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import basemod.CustomCharacterSelectScreen;
import basemod.ReflectionHacks;
import javassist.CtBehavior;
import mymod.MyMod;
import mymod.characters.MyPlayerBaymaxClassEnum;
import mymod.characters.MyPlayerKioClassEnum;
import mymod.characters.MyPlayerPoroClassEnum;

//PARCHEA QUE NO APAREZCAN HASTA PREVIO AVISO
public class LockHiddenPoroPatch {

    // TEMPORAL: acceso para pruebas solicitado por el usuario. Revertir a false cuando lo pida.
    public static final boolean PORO_TEST_ACCESS = true;

	public static boolean evilMode = false;
	public static final Logger logger = LogManager.getLogger(MyMod.class.getName());
	private static int maxEvilSelectIndex = 0;
	private static final List<CharacterOption> villains = new ArrayList<>();
	private static final List<CharacterOption> standard = new ArrayList<>();

	@SpirePatch(clz = CustomCharacterSelectScreen.class, method = "initialize")

	public static class InitializeCharacterOptions {
		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(CustomCharacterSelectScreen __instance) {
			villains.clear();
			standard.clear();

			Iterator<CharacterOption> options = __instance.options.iterator();

			ArrayList<CharacterOption> basegameOptions = new ArrayList<>(), moddedOptions = new ArrayList<>();
			CharacterOption[] villainOptions = new CharacterOption[3];

			while (options.hasNext()) {
				CharacterOption o = options.next();

				switch (o.c.chosenClass) {
				case IRONCLAD:
				case THE_SILENT:
				case DEFECT:
				case WATCHER:
					// Ignoramos los vanilla: no los añadimos a nada
					break;

				default:
					if (o.c.chosenClass == MyPlayerBaymaxClassEnum.BAYMAX_CLASS) {
					    villainOptions[0] = o;
					    o.locked = false;
					} else if (o.c.chosenClass == MyPlayerKioClassEnum.KIO_CLASS) {
					    villainOptions[1] = o;
					    o.locked = false;
					} else if (o.c.chosenClass == MyPlayerPoroClassEnum.PORO_CLASS) {
						//bloquear el boton de poro
					//  UnlockTracker.unlockPref.putInteger("Poro", 2);
						if (PORO_TEST_ACCESS) {
                            o.locked = false;
                            ReflectionHacks.setPrivate(o, CharacterOption.class, "buttonImg",
                                    ImageMaster.loadImage("images/poro/ui/char_button.png"));
                        } else if (UnlockTracker.unlockPref.getInteger("Poro", 2) == 2) {
						    // Está bloqueado → lo ocultas visualmente
						    o.locked = true;
						    ReflectionHacks.setPrivate(o, CharacterOption.class, "buttonImg", ImageMaster.CHAR_SELECT_LOCKED);
						} else {
							
						    // Está desbloqueado → lo haces visible
						    o.locked = false;
						    ReflectionHacks.setPrivate(o, CharacterOption.class, "buttonImg","images/poro/ui/char_button.png"); 
						    
						}
					}
					// Ignora cualquier otro personaje modded que no sea Kio ni Poro
					break;
						
				}

			}

			villains.addAll(Arrays.asList(villainOptions));
			villains.addAll(moddedOptions);
			logger.info("Baymax locked? " + UnlockTracker.isCharacterLocked("Baymax"));

			maxEvilSelectIndex = (int) Math.ceil(((float) villains.size() / 4)) - 1;

			standard.addAll(__instance.options);

			/// To note: Should villains in the normal mode also be sorted? It looks a bit
			/// awkward when they're locked out of order.
		}

		// ⛔ Evitar selección si está bloqueado
		@SpirePatch(clz = CharacterOption.class, method = "update")
		public static class PreventSelection {
			@SpirePostfixPatch
			public static void cancelIfLocked(CharacterOption __instance) {
				if (!PORO_TEST_ACCESS && __instance.c != null && __instance.c.chosenClass == MyPlayerPoroClassEnum.PORO_CLASS
						&& UnlockTracker.isCharacterLocked("Poro")) {
					__instance.selected = false;
				}
			}
		}

		// 🙈 Ocultar retrato (opcional, puedes eliminarlo si quieres mostrarlo gris)
		@SpirePatch(clz = CharacterOption.class, method = "render")
		public static class HidePortrait {
			@SpirePrefixPatch
			public static SpireReturn<Void> skipPortrait(CharacterOption __instance, SpriteBatch sb) {
				if (__instance.c != null && __instance.c.chosenClass == MyPlayerPoroClassEnum.PORO_CLASS
						&& UnlockTracker.isCharacterLocked("Poro")) {
					return SpireReturn.Continue(); // locked=true ya oculta detalles sensibles
				}
				return SpireReturn.Continue();
			}
		}

		public static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws Exception {
				Matcher finalMatcher = new Matcher.FieldAccessMatcher(CustomCharacterSelectScreen.class, "options");
				ArrayList<Matcher> matchers = new ArrayList<>();
				matchers.add(finalMatcher);
				return LineFinder.findInOrder(ctBehavior, matchers, finalMatcher);
			}
		}
	}
}

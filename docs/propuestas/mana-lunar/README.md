# Simulacro del indicador lunar de energia

Propuesta visual, no integrada en el juego. Asset final: orbe-lunar.png, 128x128 pixeles, RGBA con transparencia (esquina alpha 0), igual al tamano de las capas existentes del indicador.

Diseno: nucleo azul noche, aro blanco azulado con luz lunar y borde circular. Centro libre para el texto dinamico de energia (por ejemplo 4/4 o 0/4), que debe seguir dibujando el juego. Esta imagen completa sirve para decidir el estilo; no sustituye automaticamente las capas giratorias del indicador actual.

Herramienta: imagegen integrada. Salida ajustada a 128x128 y comprobada. Original de generacion conservado en el directorio de generated_images.

Prompt final:
Asset type: game UI energy orb mockup for a Slay the Spire character with a lunar theme. Generate a single complete circular mana/energy orb, centered and isolated on genuinely transparent background, intended final size exactly 128x128 pixels matching existing game UI assets, square canvas. Painterly fantasy game UI aesthetic with readable shapes at tiny size. Deep midnight navy-blue circular core, ice-blue softly swirling lunar light around the outer edge, silver-white blue (#D1E8FF) ring, a subtle crescent-like curve incorporated in the rim, delicate lunar glints limited to the rim. Center has a clean dark area to overlay an energy number in the game. Keep the ring inside the canvas with adequate transparent padding, no cropped glow. This is one mockup, not a spritesheet. No text, no numbers, no letters, no UI panels, no card, no watermark. Cold blue palette only, no gold, no orange, no purple. If output resolution is greater than 128x128 preserve square composition and strong readability for downscaling. Save locally for the project.

## Capas generadas e integracion aprobada
Herramienta: imagegen integrada. Edit target: orbe-lunar.png aprobado. Destinos: src/main/resources/images/poro/ui/lunarEnergy/core.png, rim.png y wisps.png, 128x128.

Prompt comun para las tres llamadas:
Edit target: attached approved lunar game UI orb. Asset type: separated animation layer for this same orb. [Instruccion de capa abajo] Output square, final target 128x128 pixels matching original. Keep proportions aligned with the reference. No text/numbers/UI/watermark. Preserve cold white-blue palette; no purple, yellow or orange. Save locally.

Instruccion core:
Extract/reconstruct only the fixed circular dark midnight-blue core from the reference energy orb. Remove all bright outer rings and sparkles, replacing their inner area with a continuous dark navy disk, softly feathered transparent edge. Keep centered circle, same exact geometry and camera, clean dark center for number. No text.

Instruccion rim:
Extract/reconstruct only the silver white ice-blue circular lunar rim from the reference energy orb. Transparent center and outside; a complete seamless circular ring with gently swirling highlights, readable all around when rotated, same centered circle geometry. No opaque disk, no text.

Instruccion wisps:
Create only a thin outer swirl of ice-blue moonlight wisps and a few tiny glints following the circular rim of this reference energy orb. Transparent center and outside, faint light, centered same circle geometry, wide soft transparent margins to avoid clipping while rotating. No solid ring or disk, no text.

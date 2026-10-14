# Energia lunar de Poro

Integrada en LunarEnergyOrb y asignada solo a Poro. No cambia su energia maxima ni espacios de orbe.

Capas de src/main/resources/images/poro/ui/lunarEnergy:
- core.png: nucleo fijo.
- aura.png e innerGlow.png: luces suaves.
- rim.png: aro lunar rotatorio.
- wisps.png: estela exterior con giro opuesto al aro.
- sparkles.png: destellos que rotan y titilan.
Todas miden 128x128 y tienen transparencia. energyVfx.png mide 256x256, igual que las hojas de efectos originales.

Se usan cinco capas activas, una base y cinco referencias a las mismas capas para cero energia; el render vacio aplica un tono azul noche semitransparente. No se colocan hojas VFX en las capas. El numero lo dibuja el juego y no gira.

Nucleo, aro y estela derivados del simulacro aprobado mediante imagegen integrado, luego ajustados a 128x128. Las luces/destellos son pequenos recursos procedurales. Los PNG anteriores permanecen intactos. La vista docs/propuestas/mana-lunar/integrado-preview.png es un montaje estatico con numero ilustrativo.

## Prueba manual
1. En combate de Poro, comprobar indicador azul y legibilidad de 4/4.
2. Esperar: nucleo/numero fijos, aro y estela giran en sentidos opuestos y destellos titilan.
3. Gastar hasta 0: las capas deben atenuarse, sin rectangulos ni saltos.
4. Recuperar energia desde 0 y al iniciar turno: vuelve el brillo, numero correcto.
5. Comprobar destello al gastar energia, escala de pantalla y nuevos combates.
6. Kio/Baymax conservan sus indicadores.

Maven offline con JDK 8/UTF-8 correcto. Comprobadas siete imagenes en JAR con dimensiones y esquinas transparentes. No se ha iniciado el juego: rotacion, orden de capas y recuperacion de energia pendientes de prueba manual.

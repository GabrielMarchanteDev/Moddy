# Recuerdo reconstruido durante combate

## Revision vigente: automatica por vida enemiga

- Poro, encuentro de dos LouseNormal/LouseDefensive: se activa solo, sin consola. Otros encuentros no se activan automaticamente. El comando recuerdo sigue disponible para ensayos manuales.
- Usa suma de vida restante / suma de vida maxima inicial de todos los enemigos. Revelacion gradual a 75/50/25 % restante; completamente clara al 25 %. Una curacion no empeora lo ya recuperado.
- No hay HUD, instrucciones ni texto de cuaderno. La imagen recuperada es el salon del templo: escritorio medico, arco/jardin, puerta y palomitas. Pista visual provisional, no nuevo canon ni desbloqueo de otro contenido.
- Conserva fondo durante fin de combate y recompensas; libera recursos solo al abandonar sala/menu o cancelar. Una victoria inmediata tambien restaura sin requerir Bloqueo.
- Proteccion PoroRewardSafetyPatch: si las reservas por rareza no admiten las opciones distintas solicitadas, elige sin reemplazo y devuelve como maximo las disponibles. Dos cartas disponibles pueden producir dos opciones. Evita los reintentos ilimitados del juego; no anade cartas ficticias. Caso PrismaticShard usa el camino original.
- Compilacion y pruebas offline de progreso/curacion/overkill y parches reales correctos. No se ha probado en juego. Los logs de los bloqueos acaban en POTION CHANCE sin excepcion; el riesgo de bucle de recompensas se identifico leyendo el algoritmo y la reserva actual, sin captura de pila del proceso congelado.

Probar: entrar con Poro en 2 Louse, atacar poco a poco, observar claridad, vencer rapido sin defender, esperar recompensas y pasar a otra sala. El fondo debe mantenerse en la sala del recuerdo y volver al habitual en la siguiente. Verificar log [Recompensas Poro] y seleccion de las dos opciones disponibles. La revision siguiente es historica y queda sustituida.

## Version anterior: ensayo por Bloqueo

Prototipo optativo, sin nuevos hechos de historia ni cambios de encuentros/recompensas. Mapa original sigue activo; templo explorable permanece desactivado.

## Probar

1. Instalar JAR actualizado, iniciar partida con Poro o Kio y entrar en un combate.
2. Abrir consola de BaseMod, escribir `recuerdo` y cerrar consola. Tambien se puede escribir antes: se activa al entrar en el siguiente combate.
3. El fondo cambia a una escena del bosque gris/desenfocada; los objetivos se muestran arriba, sin tapar cartas.
4. Generar 10 de Bloqueo acumulado recupera la fase 1; 15, fase 2; 20, fase 3 y cuaderno. Por ejemplo, cuatro Defender de 5 completan todas las fases.
5. Se suma el aumento real positivo de Bloqueo desde activacion, incluso entre turnos. Perderlo no resta progreso; Bloqueo previo no cuenta. No exige absorber dano ni acumular 20 a la vez.
6. No hay condiciones de dano ni victoria. Las tres fases pueden completarse antes de ganar; esperar un momento para ver el fundido del cuaderno. Si una sola accion genera 20, completa las tres.
7. Cada fragmento aporta nitidez/color y aumenta un ambiente de viento independiente al 30 % maximo relativo a volumen musical. Conserva musica y efectos existentes. Transiciones visuales suaves; shader restaura estado antes de dibujar personajes/cartas.
8. El efecto se retira al terminar el combate, antes de presentar recompensas, y libera recursos. Progreso de esta prueba no se guarda ni desbloquea el archivo privado. No hay callback de victoria.
9. `recuerdo off` cancela la prueba y restaura fondo; `recuerdo` reinicia sus objetivos en el combate actual. Activarlo en medio de combate no cuenta Bloqueo anterior.

Si un shader no compila, se registra el error y se mantiene el fondo sin ese filtro. Comprobar GPU, sonido, umbrales y salida en juego. El usuario reporto una caida al generar recompensas; el log completo termina en POTION CHANCE sin excepcion, por lo que causa no confirmada. Revision elimina disparador de victoria y uso del efecto en recompensas.

## Recursos y ajustes

MemoryReconstructionState contiene umbrales/objetivos. RebuildingMemory contiene filtro, fundidos, ambiente y textos. Antes/despues: memo/rebuilding-before.png y memo/rebuilding-after.png. Fondo/cuaderno de prueba generados con image_gen integrado, no son arte de cartas 250x190. Prompts en RECUERDO_RECONSTRUCCION_ARTE.md.

Ambiente base reutilizado del juego: audio/sound/SOTE_SFX_WindAmb_v1.ogg. Esta prueba no cambia la musica lunar ni sus ajustes.

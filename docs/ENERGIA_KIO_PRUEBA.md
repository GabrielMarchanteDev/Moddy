# Energia de palomitas de Kio

Indicador PopcornEnergyOrb solo para Kio. No cambia energia maxima ni mecanicas de suerte/palomitas. Poro conserva su indicador lunar.

Recursos de images/kio/ui/popcornEnergy:
- core.png: centro marron oscuro fijo, derivado del simulacro aprobado.
- rim.png: aro naranja completo reconstruido con imagegen para poder rotar sin huecos.
- popcorn.png: tres palomitas fijas derivadas del simulacro aprobado.
- shine.png: luz calida pulsante discreta.
- blank.png: capa transparente de compatibilidad con las cinco capas de CustomEnergyOrb.
Capas 128x128 transparentes. energyVfx.png, 256x256, efecto calido al gastar/recuperar energia.

El aro gira a 6 grados/s con energia y mas lento a cero. A 0, aro/palomitas se atenuan y el pulso disminuye; el numero y el centro permanecen fijos. Los recursos anteriores permanecen intactos en images/kio/ui/topPanel.

## Prueba manual
1. Iniciar partida con Kio y comprobar aspecto sencillo de tres palomitas y numero legible.
2. Esperar: aro gira suavemente, palomitas/nucleo/numero fijos y luz pulsa.
3. Gastar toda la energia: aspecto apagado, sin hojas VFX dibujadas como capas ni rectangulos.
4. Recuperar energia y empezar otro turno: numero correcto y brillo de nuevo.
5. Comprobar distintos combates, indicadores de suerte y Poro intactos.

Maven offline package con Java 8 y UTF-8 correcto. Se verificaron seis PNG en JAR, dimensiones 128/256 y esquinas transparentes. Montaje estatico inspeccionado en docs/propuestas/mana-kio/palomitas-integrado-preview.png. No se ha instalado JAR ni iniciado juego; animacion y cero energia pendientes de prueba manual.
Revision vigente: palomitas rotan -24 grados/s (+ aro 18 grados/s); a cero palomitas -10 grados/s y aro atenuado/lento. Comprobar movimiento visible y ausencia de huecos cuando las palomitas cambian de posicion.
Revision vigente: popcorn1/2/3.png independientes, 128x128. Velocidades -14/-24/-36 grados/s, reducidas al 40 % a cero. Comprobar que cambian sus distancias entre si durante el giro.

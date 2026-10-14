# Templo explorable — prueba

La aclaracion del usuario pide sustituir el mapa DURANTE partidas. TempleDungeonMapPatch reemplaza update/render de DungeonMapScreen mientras MAP esta activo con Kio, Poro o Baymax. El mapa generado sigue siendo fuente de conexiones, salas y guardado: no hay caminos dibujados, se eligen estancias sobre el fondo del templo.

Tres zonas de acceso muestran el tipo de sala; pasos no disponibles aparecen cerrados. Mas pasos permite recorrer paginas si hay mas de tres salidas. Tab/Enter o raton para elegir, Volver/Esc cuando el juego permita cerrar el mapa. No permite entrar durante combate, sala incompleta o transicion. Respeta conexiones y gasto de Botas aladas; muestra llave esmeralda si procede. Jefes explicitos del mapa de Kio conservan sus salas; al final del mapa vanilla se ofrece el jefe externo.

Tambien queda una entrada explorable de M.E.M.O. en menu: escritorio de Baymax, puerta privada de Kio (misma clave), arco al jardin. El jardin es una estancia de prueba contemplativa, sin nuevo recuerdo de Poro ni recompensas. No constituye un nuevo hecho de historia.

## Prueba manual necesaria

1. Partida nueva de Poro: al abrir mapa, elegir primer combate por una estancia. Volver tras ganar: ofrecer solo salas conectadas.
2. Probar evento, elite, descanso, tienda y cofre. Confirmar recompensas y progreso normal de pisos.
3. Abrir mapa en combate: observar, sin entrar en otra sala; Volver regresa.
4. Guardar/continuar y comprobar opciones iguales. Cambiar entre mapas con mas de tres salidas usando Mas pasos.
5. Kio: comprobar combate -> evento/elite -> tienda -> descanso -> tres jefes, sin saltos; ultimo conserva transicion de acto.
6. Ruta vanilla de Poro: llegar a ultima fila y entrar al jefe. Verificar transicion al siguiente acto. Botas aladas y llave esmeralda requieren prueba especifica.
7. M.E.M.O. desde zona invisible del menu: escritorio reproduce Baymax si disponible; puerta solicita clave o reproduce Kio; arco abre jardin, Esc vuelve. Recuerdos regresan al templo.
8. Ratios/resoluciones: hitboxes y zonas sobre objetos deben coincidir. Mando pendiente.

Para revertir solo mapa de partidas: TempleDungeonMap.ENABLED = false y recompilar. No altera configuracion JSON de Kio ni guardados.

## Verificacion realizada

Maven offline Java 8/UTF-8 correcto. TempleMapPatchCheck aplica los inyectores reales de ModTheSpire y compila ambas firmas. Dos fondos presentes en el JAR. Un intento de ejecutar rutas con salas vanilla sin iniciar el juego no pudo inicializar AbstractRoom porque requiere languagePack; no constituye un fallo en juego ni confirma las transiciones. Recorrido, jefes, persistencia y reliquias deben probarse manualmente antes de considerar estable esta version.

Arte generado con image_gen integrado: memo/temple-hall.png. Jardin reutiliza una prueba visual anterior, memo/temple-garden.png. Son fondos, no arte de cartas 250x190. Prompt del salon en TEMPLO_EXPLORABLE_ARTE.md.

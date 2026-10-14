# Recuerdo privado de Kio — prototipo

Contenido narrativo de prueba, pendiente de revision del usuario. No fija nuevos hechos de origen ni explica el mecanismo de rescate. No implementa aun las cartas del jefe final.

## Simular la pista fisica

Leer MEMO_PISTA_FICTICIA.md fuera del juego, como si se hubiese encontrado en la habitacion del templo. No se muestra su solucion en el visor.

En menu principal, pulsar zona invisible: centro a 160 px del borde derecho y 140 px del borde inferior (escala base), area 240x64. Abrir Kio · Privado, escribir AUN HAY ALGUIEN y Enter. Mayusculas y espacios no afectan. Teclado latino A-Z, espacio y retroceso; no requiere acentos. La clave es un prototipo, no seguridad criptografica.

Tras acertar se guarda kioPrivateUnlocked en SpireConfig mymod/memo-progress; no cambia la partida ni desbloquea personajes. Repetir el recuerdo ya no pide clave. Baymax y el acceso invisible existentes se conservan.

## Pruebas

1. Abrir sin Baymax desbloqueado; Kio permite introducir clave.
2. Clave incorrecta: mensaje y posibilidad de corregir, sin desbloqueo.
3. Clave correcta: seis paginas, fondo propio y aparicion gradual del texto.
4. Flechas izquierda/derecha o botones cambian pagina; Enter avanza. No hay temporizador de lectura. Esc vuelve al archivo.
5. Ultima pagina: Continuar vuelve al archivo. Cerrar y abrir nuevamente reproduce desde el inicio.
6. Reiniciar juego: acceso privado persiste. Guardado de partida y Baymax se conservan.
7. Revisar fuentes y lineas a resolucion habitual; control por mando pendiente.

Texto editable en src/main/resources/memo/kio-private.json. Fondo en memo/kio-private.png, generado con image_gen integrado; prompt en MEMO_PRIVADO_ARTE.md. No hay voz sintetica ni musica nueva.

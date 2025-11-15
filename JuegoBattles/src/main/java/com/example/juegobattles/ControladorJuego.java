package com.example.juegobattles;

import java.io.IOException;

public class ControladorJuego {
    private JuegoBattleship juego;
    private InterfazRed conexion;
    private InterfazUsuario ui;
    private boolean esServidor;
    private String nombreJugador;

    public ControladorJuego(JuegoBattleship juego, InterfazRed conexion, InterfazUsuario ui) {
        this.juego = juego;
        this.conexion = conexion;
        this.ui = ui;
    }

    public void iniciar() {
        try {
            ui.mostrarMensaje("=== BATTLESHIP P2P ===");
            nombreJugador = ui.leerLinea("Ingresa tu nombre: ");

            String opcion = ui.leerOpcion("\nSelecciona modo:\n1. Crear partida (Esperar conexión)\n2. Unirse a partida (Conectar a otro jugador)", "1 , 2");
            esServidor = opcion.equals("1");

            if (esServidor) {
                conexion.iniciarServidor();
            } else {
                String ip = ui.leerLinea("Ingresa la IP del otro jugador: ");
                conexion.conectarCliente(ip);
            }

            conexion.intercambiarNombres(nombreJugador, esServidor);

            juego.colocarBarcosAutomaticamente();
            ui.mostrarMensaje("Tus barcos han sido colocados automáticamente.");
            juego.mostrarTableroPropio();

            boolean juegoActivo = true;
            boolean miTurno = esServidor;

            // Ambos jugadores envían LISTO
            conexion.enviarMensaje(ProtocoloBattleship.LISTO);
            String respuesta = conexion.recibirMensaje();

            if (!ProtocoloBattleship.LISTO.equals(respuesta)) {
                ui.mostrarError("Error al sincronizar estado LISTO.");
                return;
            }

            ui.mostrarMensaje("¡Ambos jugadores listos! El juego comienza.");

            if (miTurno) {
                ui.mostrarMensaje("\n¡Tú comienzas!");
            } else {
                ui.mostrarMensaje("\nEl oponente comienza...");
            }

            while (juegoActivo) {
                if (miTurno) {
                    juegoActivo = turnoLocal();
                    if (juegoActivo)
                        miTurno = false;
                } else {
                    juegoActivo = turnoRemoto();
                    if (juegoActivo)
                        miTurno = true;
                }
                Thread.sleep(100); // Pequeña pausa para sincronización
            }
        } catch (IOException e) {
            ui.mostrarError("Error de conexión: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            try {
                conexion.cerrar();
                ui.mostrarMensaje("Conexión cerrada.");
            } catch (IOException e) {
                ui.mostrarError("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }

    private boolean turnoLocal() throws IOException {
        ui.mostrarMensaje("\n=== TU TURNO ===");
        juego.mostrarTableroEnemigo();
        juego.mostrarTableroPropio();

        int[] disparo = obtenerDisparoJugador();
        conexion.enviarMensaje(ProtocoloBattleship.construirMensajeDisparo(disparo[0], disparo[1]));

        String respuesta = conexion.recibirMensaje();
        if (respuesta == null) {
            ui.mostrarError("El oponente se desconectó o hubo un error en la comunicación.");
            return false;
        }

        try {
            ProtocoloBattleship.Mensaje mensaje = ProtocoloBattleship.parsearMensaje(respuesta);

            switch (mensaje.comando) {
                case ProtocoloBattleship.IMPACTO:
                    ui.mostrarMensaje("¡IMPACTO en (" + mensaje.x + "," + mensaje.y + ")!");
                    juego.registrarImpacto(mensaje.x, mensaje.y);
                    return true;

                case ProtocoloBattleship.FALLO:
                    ui.mostrarMensaje("FALLO en (" + mensaje.x + "," + mensaje.y + ")");
                    juego.registrarFallo(mensaje.x, mensaje.y);
                    return true;

                case ProtocoloBattleship.HUNDIDO:
                    ui.mostrarMensaje("¡HUNDIDO! " + mensaje.tipoBarco + " en (" + mensaje.x + "," + mensaje.y + ")");
                    juego.registrarImpacto(mensaje.x, mensaje.y);
                    return true;

                case ProtocoloBattleship.JUEGO_TERMINADO:
                    ui.mostrarMensaje("¡FELICIDADES! ¡HAS GANADO!");
                    return false;

                default:
                    ui.mostrarMensaje("Respuesta inesperada: " + respuesta);
                    return true;
            }
        } catch (Exception e) {
            ui.mostrarError("Error procesando respuesta: " + e.getMessage());
            ui.mostrarError("Respuesta recibida: " + respuesta);
            return false;
        }
    }

    private boolean turnoRemoto() throws IOException {
        ui.mostrarMensaje("\n=== TURNO DEL OPONENTE ===");
        ui.mostrarMensaje("Esperando disparo del oponente...");

        String mensajeEntrante = conexion.recibirMensaje();

        if (mensajeEntrante == null) {
            ui.mostrarError("El oponente se desconectó.");
            return false;
        }

        try {
            ProtocoloBattleship.Mensaje mensaje = ProtocoloBattleship.parsearMensaje(mensajeEntrante);

            if (ProtocoloBattleship.DISPARAR.equals(mensaje.comando)) {
                boolean impacto = juego.recibirDisparo(mensaje.x, mensaje.y);

                if (impacto) {
                    String tipoBarco = juego.obtenerTipoBarcoEn(mensaje.x, mensaje.y);

                    if ("DESCONOCIDO".equals(tipoBarco)) {
                        conexion.enviarMensaje(ProtocoloBattleship.construirMensajeResultado(
                                ProtocoloBattleship.IMPACTO, mensaje.x, mensaje.y, null));
                        ui.mostrarMensaje("El oponente impactó en (" + mensaje.x + "," + mensaje.y + ")");
                    } else if (juego.estaBarcoHundido(tipoBarco)) {
                        conexion.enviarMensaje(ProtocoloBattleship.construirMensajeResultado(
                                ProtocoloBattleship.HUNDIDO, mensaje.x, mensaje.y, tipoBarco));

                        if (juego.todosBarcosHundidos()) {
                            conexion.enviarMensaje(ProtocoloBattleship.JUEGO_TERMINADO);
                            ui.mostrarMensaje("El oponente hundió tu " + tipoBarco);
                            ui.mostrarMensaje("¡HAS PERDIDO!");
                            return false;
                        } else {
                            ui.mostrarMensaje(
                                    "El oponente hundió tu " + tipoBarco + " en (" + mensaje.x + "," + mensaje.y + ")");
                        }
                    } else {
                        conexion.enviarMensaje(ProtocoloBattleship.construirMensajeResultado(
                                ProtocoloBattleship.IMPACTO, mensaje.x, mensaje.y, null));
                        ui.mostrarMensaje("El oponente impactó en (" + mensaje.x + "," + mensaje.y + ")");
                    }
                } else {
                    conexion.enviarMensaje(ProtocoloBattleship.construirMensajeResultado(
                            ProtocoloBattleship.FALLO, mensaje.x, mensaje.y, null));
                    ui.mostrarMensaje("El oponente falló en (" + mensaje.x + "," + mensaje.y + ")");
                }
            }

            juego.mostrarTableroPropio();
            return true;

        } catch (Exception e) {
            ui.mostrarError("Error procesando mensaje del oponente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private int[] obtenerDisparoJugador() {
        while (true) {
            try {
                String entrada = ui.leerLinea("Ingresa coordenadas para disparar (fila,columna 0-9): ");
                String[] coordenadas = entrada.split(",");

                if (coordenadas.length != 2) {
                    ui.mostrarMensaje("Formato inválido. Usa: fila,columna");
                    continue;
                }

                int fila = Integer.parseInt(coordenadas[0].trim());
                int columna = Integer.parseInt(coordenadas[1].trim());

                if (fila >= 0 && fila < 10 && columna >= 0 && columna < 10) {
                    if (!juego.yaDisparado(fila, columna)) {
                        return new int[] { fila, columna };
                    } else {
                        ui.mostrarMensaje("Ya disparaste en esa posición.");
                    }
                } else {
                    ui.mostrarMensaje("Coordenadas fuera de rango. Usa números del 0 al 9.");
                }
            } catch (NumberFormatException e) {
                ui.mostrarMensaje("Por favor ingresa números válidos.");
            }
        }
    }
}

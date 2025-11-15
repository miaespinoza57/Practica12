package com.example.juegobattles;

public class ProtocoloBattleship {

        public static final String DISPARAR = "DISPARAR";
        public static final String IMPACTO = "IMPACTO";
        public static final String FALLO = "FALLO";
        public static final String HUNDIDO = "HUNDIDO";
        public static final String LISTO = "LISTO";
        public static final String JUEGO_TERMINADO = "JUEGO_TERMINADO";

        // Construcción de mensajes
        public static String construirMensajeDisparo(int x, int y) {
            return DISPARAR + "," + x + "," + y;
        }

        public static String construirMensajeResultado(String comando, int x, int y, String tipoBarco) {
            if (tipoBarco == null)
                return comando + "," + x + "," + y;
            return comando + "," + x + "," + y + "," + tipoBarco;
        }

        // Parseo de mensajes
        public static Mensaje parsearMensaje(String linea) {
            String[] p = linea.split(",");

            String comando = p[0];
            int x = p.length > 1 ? Integer.parseInt(p[1]) : -1;
            int y = p.length > 2 ? Integer.parseInt(p[2]) : -1;
            String barco = p.length > 3 ? p[3] : null;

            return new Mensaje(comando, x, y, barco);
        }

        // Clase interna
        public static class Mensaje {
            public String comando;
            public int x, y;
            public String tipoBarco;

            public Mensaje(String cmd, int x, int y, String tipo) {
                this.comando = cmd;
                this.x = x;
                this.y = y;
                this.tipoBarco = tipo;
            }
        }

}

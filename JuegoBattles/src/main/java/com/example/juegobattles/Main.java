package com.example.juegobattles;

public class Main {
    public static void main(String[] args) {
        InterfazUsuario ui = new UIConsola();
        InterfazRed conexion = new ConexionSocket(ui);
        JuegoBattleship juego = new JuegoBattleship(); // Socket no es necesario aquí porque la red está separada

        ControladorJuego controlador = new ControladorJuego(juego, conexion, ui);
        controlador.iniciar();
    }
}

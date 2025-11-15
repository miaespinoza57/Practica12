package com.example.juegobattles;

public interface InterfazUsuario {
    void mostrarMensaje(String mensaje);
    void mostrarError(String mensaje);
    String leerLinea(String prompt);
    String leerOpcion(String prompt, String opcionesValidas);
}

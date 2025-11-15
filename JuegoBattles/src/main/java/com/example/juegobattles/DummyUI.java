package com.example.juegobattles;

public class DummyUI implements InterfazUsuario {
    @Override
    public void mostrarMensaje(String mensaje) { }

    @Override
    public void mostrarError(String mensaje) { }

    @Override
    public String leerLinea(String prompt) {
        return "TEST";
    }
    @Override
    public String leerOpcion(String prompt, String opcionesValidas) {
        return "test";
    }
}

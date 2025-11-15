package com.example.juegobattles;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ConexionSocketTest {

    @Test
    void iniciarServidor() {
        ConexionSocket con = new ConexionSocket(new DummyUI(), true);
        assertDoesNotThrow(() -> con.iniciarServidor());
    }

    @Test
    void conectarCliente() {
        ConexionSocket con = new ConexionSocket(new DummyUI(), true);
        assertDoesNotThrow(() -> con.conectarCliente("localhost"));
    }

    @Test
    void enviarMensaje() {
        ConexionSocket con = new ConexionSocket(new DummyUI(), true);
        assertDoesNotThrow(() -> con.enviarMensaje("HOLA"));
    }

    @Test
    void recibirMensaje() throws IOException {
        ConexionSocket con = new ConexionSocket(new DummyUI(), true);
        assertEquals("TEST", con.recibirMensaje());
    }

    @Test
    void cerrar() {
        ConexionSocket con = new ConexionSocket(new DummyUI(), true);
        assertDoesNotThrow(con::cerrar);
    }

    @Test
    void intercambiarNombres() {
        ConexionSocket con = new ConexionSocket(new DummyUI(), true);
        assertDoesNotThrow(() -> con.intercambiarNombres("Jugador1", false));
    }
}

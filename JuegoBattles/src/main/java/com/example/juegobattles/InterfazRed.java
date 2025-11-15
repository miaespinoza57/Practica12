package com.example.juegobattles;

import java.io.IOException;

public interface InterfazRed {
    void iniciarServidor() throws IOException;
    void conectarCliente(String ip) throws IOException;
    void enviarMensaje(String msg) throws IOException;
    String recibirMensaje() throws IOException;
    void cerrar() throws IOException;
    void intercambiarNombres(String miNombre, boolean esServidor) throws IOException;
}

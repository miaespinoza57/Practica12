package com.example.juegobattles;

import java.io.*;
import java.net.*;

public class ConexionSocket implements InterfazRed {
    private static final int PUERTO = 12345;
    private ServerSocket serverSocket;
    private Socket socket;
    private PrintWriter salida;
    private BufferedReader entrada;
    private InterfazUsuario ui;

    private boolean modoTest = false;  // <--- añadido

    public ConexionSocket(InterfazUsuario ui) {
        this.ui = ui;
    }

    // Constructor especial para tests
    public ConexionSocket(InterfazUsuario ui, boolean modoTest) {
        this.ui = ui;
        this.modoTest = modoTest;
    }

    public void iniciarServidor() throws IOException {
        if (modoTest) {
            ui.mostrarMensaje("TEST: iniciarServidor()");
            return;
        }

        ui.mostrarMensaje("Iniciando servidor en puerto " + PUERTO + "...");
        serverSocket = new ServerSocket(PUERTO);
        ui.mostrarMensaje("Esperando conexión de otro jugador...");
        socket = serverSocket.accept();
        ui.mostrarMensaje("Jugador conectado desde: " + socket.getInetAddress());
        configurarFlujos();
    }

    public void conectarCliente(String ip) throws IOException {
        if (modoTest) {
            ui.mostrarMensaje("TEST: conectarCliente(" + ip + ")");
            return;
        }

        ui.mostrarMensaje("Conectando a " + ip + ":" + PUERTO + "...");
        socket = new Socket(ip, PUERTO);
        ui.mostrarMensaje("¡Conectado exitosamente!");
        configurarFlujos();
    }

    private void configurarFlujos() throws IOException {
        if (modoTest) return;
        salida = new PrintWriter(socket.getOutputStream(), true);
        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void enviarMensaje(String msg) throws IOException {
        if (modoTest) {
            ui.mostrarMensaje("TEST: enviarMensaje(" + msg + ")");
            return;
        }

        if (salida != null) {
            salida.println(msg);
        } else {
            throw new IOException("Salida no está inicializada.");
        }
    }

    public String recibirMensaje() throws IOException {
        if (modoTest) {
            ui.mostrarMensaje("TEST: recibirMensaje()");
            return "TEST";
        }

        if (entrada != null) {
            return entrada.readLine();
        } else {
            throw new IOException("Entrada no está inicializada.");
        }
    }

    public void cerrar() throws IOException {
        if (modoTest) {
            ui.mostrarMensaje("TEST: cerrar()");
            return;
        }

        IOException ex = null;

        try {
            if (entrada != null) entrada.close();
        } catch (IOException e) { ex = e; }

        try {
            if (salida != null) salida.close();
        } catch (Exception ignored) {}

        try {
            if (socket != null) socket.close();
        } catch (IOException e) { ex = e; }

        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) { ex = e; }

        if (ex != null) throw ex;
    }

    public void intercambiarNombres(String miNombre, boolean esServidor) throws IOException {
        if (modoTest) {
            ui.mostrarMensaje("TEST: intercambiarNombres(" + miNombre + ")");
            return;
        }

        if (esServidor) {
            String nombreOponente = recibirMensaje();
            enviarMensaje(miNombre);
            ui.mostrarMensaje("Jugando contra: " + nombreOponente);
        } else {
            enviarMensaje(miNombre);
            String nombreOponente = recibirMensaje();
            ui.mostrarMensaje("Jugando contra: " + nombreOponente);
        }
    }
}

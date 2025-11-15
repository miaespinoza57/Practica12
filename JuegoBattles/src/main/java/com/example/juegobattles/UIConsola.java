package com.example.juegobattles;

import java.util.Scanner;

public class UIConsola implements InterfazUsuario {

    private Scanner scanner = new Scanner(System.in);

    @Override
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    @Override
    public void mostrarError(String mensaje) {
        System.err.println(mensaje);
    }

    @Override
    public String leerLinea(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    @Override
    public String leerOpcion(String prompt, String opcionesValidas) {
        while (true) {
            System.out.println(prompt);
            String entrada = scanner.nextLine();
            for (String op : opcionesValidas.split("")) {
                if (op.equals(entrada))
                    return entrada;
            }
            System.out.println("Opción inválida, intenta de nuevo.");
        }
    }
}

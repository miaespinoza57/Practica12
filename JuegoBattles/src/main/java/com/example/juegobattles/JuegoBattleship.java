package com.example.juegobattles;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class JuegoBattleship {
    private static final int TAM = 10;
    char[][] tableroPropio;
    char[][] tableroEnemigo;
    private Map<String, Integer> tamanios;
    private Map<String, Integer> impactos;
    private Random rand;

    public JuegoBattleship() {
        tableroPropio = new char[TAM][TAM];
        tableroEnemigo = new char[TAM][TAM];
        rand = new Random();

        inicializarTablero(tableroPropio);
        inicializarTablero(tableroEnemigo);

        tamanios = new HashMap<>();
        tamanios.put("PORTAAVIONES", 5);
        tamanios.put("ACORAZADO", 4);
        tamanios.put("SUBMARINO", 3);
        tamanios.put("CRUCERO", 3);
        tamanios.put("DESTRUCTOR", 2);

        impactos = new HashMap<>();
        tamanios.keySet().forEach(b -> impactos.put(b, 0));
    }

    private void inicializarTablero(char[][] t) {
        for (int i = 0; i < TAM; i++)
            Arrays.fill(t[i], '~');
    }

    public void colocarBarcosAutomaticamente() {
        for (var entry : tamanios.entrySet()) {
            String nombre = entry.getKey();
            int size = entry.getValue();

            colocarBarco(nombre, size);
        }
    }

    private void colocarBarco(String nombre, int size) {
        boolean colocado = false;

        while (!colocado) {
            int fila = rand.nextInt(TAM);
            int col = rand.nextInt(TAM);
            boolean horizontal = rand.nextBoolean();

            if (puedeColocar(fila, col, size, horizontal)) {
                for (int i = 0; i < size; i++) {
                    int f = fila + (horizontal ? 0 : i);
                    int c = col + (horizontal ? i : 0);
                    tableroPropio[f][c] = nombre.charAt(0);
                }
                colocado = true;
            }
        }
    }

    private boolean puedeColocar(int fila, int col, int size, boolean horiz) {
        for (int i = 0; i < size; i++) {
            int f = fila + (horiz ? 0 : i);
            int c = col + (horiz ? i : 0);

            if (f >= TAM || c >= TAM)
                return false;
            if (tableroPropio[f][c] != '~')
                return false;
        }
        return true;
    }

    public boolean yaDisparado(int f, int c) {
        return tableroEnemigo[f][c] == 'X' || tableroEnemigo[f][c] == 'O';
    }

    public void registrarImpacto(int f, int c) {
        tableroEnemigo[f][c] = 'X';
    }

    public void registrarFallo(int f, int c) {
        tableroEnemigo[f][c] = 'O';
    }

    public boolean recibirDisparo(int f, int c) {
        char casilla = tableroPropio[f][c];

        if (casilla == '~') {
            tableroPropio[f][c] = 'O';
            return false;
        }

        tableroPropio[f][c] = 'X';

        String barco = obtenerTipoBarcoEn(f, c);
        impactos.put(barco, impactos.get(barco) + 1);

        return true;
    }

    public String obtenerTipoBarcoEn(int f, int c) {
        char letra = tableroPropio[f][c];

        for (String b : tamanios.keySet()) {
            if (b.charAt(0) == letra || tableroPropio[f][c] == 'X')
                return b;
        }
        return "DESCONOCIDO";
    }

    public boolean estaBarcoHundido(String tipo) {
        return impactos.get(tipo).equals(tamanios.get(tipo));
    }

    public boolean todosBarcosHundidos() {
        for (String b : tamanios.keySet()) {
            if (!estaBarcoHundido(b))
                return false;
        }
        return true;
    }

    public void mostrarTableroPropio() {
        System.out.println("\n--- TABLERO PROPIO ---");
        mostrar(tableroPropio);
    }

    public void mostrarTableroEnemigo() {
        System.out.println("\n--- TABLERO ENEMIGO ---");
        mostrar(tableroEnemigo);
    }

    private void mostrar(char[][] t) {
        System.out.print("  ");
        for (int i = 0; i < TAM; i++)
            System.out.print(i + " ");
        System.out.println();

        for (int f = 0; f < TAM; f++) {
            System.out.print(f + " ");
            for (int c = 0; c < TAM; c++) {
                System.out.print(t[f][c] + " ");
            }
            System.out.println();
        }
    }
}

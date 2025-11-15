package com.example.juegobattles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JuegoBattleshipTest {

    @Test
    void testInicializacionTableros() {
        JuegoBattleship juego = new JuegoBattleship();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                assertEquals('~', juego.tableroPropio[i][j]);
                assertEquals('~', juego.tableroEnemigo[i][j]);
            }
        }
    }

    @Test
    void testColocarBarcosAutomaticamente() {
        JuegoBattleship juego = new JuegoBattleship();
        juego.colocarBarcosAutomaticamente();

        int contador = 0;

        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                if (juego.tableroPropio[i][j] != '~')
                    contador++;

        // número total de casillas ocupadas = 5+4+3+3+2 = 17
        assertEquals(17, contador);
    }

    @Test
    void testRegistrarImpacto() {
        JuegoBattleship juego = new JuegoBattleship();
        juego.registrarImpacto(3,3);
        assertEquals('X', juego.tableroEnemigo[3][3]);
    }

    @Test
    void testRegistrarFallo() {
        JuegoBattleship juego = new JuegoBattleship();
        juego.registrarFallo(4,5);
        assertEquals('O', juego.tableroEnemigo[4][5]);
    }

    @Test
    void testRecibirDisparoAgua() {
        JuegoBattleship juego = new JuegoBattleship();
        boolean impacto = juego.recibirDisparo(0,0);

        assertFalse(impacto);
        assertEquals('O', juego.tableroPropio[0][0]);
    }

    @Test
    void testHundimiento() {
        JuegoBattleship juego = new JuegoBattleship();

        // Simulamos un submarino de tamaño 3 en posiciones (0,0) (0,1) (0,2)
        juego.tableroPropio[0][0] = 'S';
        juego.tableroPropio[0][1] = 'S';
        juego.tableroPropio[0][2] = 'S';

        juego.recibirDisparo(0,0);
        juego.recibirDisparo(0,1);
        juego.recibirDisparo(0,2);

        assertTrue(juego.estaBarcoHundido("SUBMARINO"));
    }
}
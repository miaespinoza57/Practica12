package com.example.juegobattles;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProtocoloBattleshipTest {

    @Test
    void testConstruirDisparo() {
        String msg = ProtocoloBattleship.construirMensajeDisparo(3, 4);
        assertEquals("DISPARAR,3,4", msg);
    }

    @Test
    void testParsearDisparo() {
        ProtocoloBattleship.Mensaje m = ProtocoloBattleship.parsearMensaje("DISPARAR,5,7");
        assertEquals("DISPARAR", m.comando);
        assertEquals(5, m.x);
        assertEquals(7, m.y);
        assertNull(m.tipoBarco);
    }

    @Test
    void testParsearHundido() {
        ProtocoloBattleship.Mensaje m = ProtocoloBattleship.parsearMensaje("HUNDIDO,3,4,CRUCERO");
        assertEquals("HUNDIDO", m.comando);
        assertEquals(3, m.x);
        assertEquals(4, m.y);
        assertEquals("CRUCERO", m.tipoBarco);
    }
}

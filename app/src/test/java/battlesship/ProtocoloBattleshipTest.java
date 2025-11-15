package battlesship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ProtocoloBattleshipTest {

    @Test
    void testConstruirMensajeDisparo_DeberiaFormatearCorrectamente() {
        String mensaje = ProtocoloBattleship.construirMensajeDisparo(3, 5);
        assertEquals("DISPARAR|3,5", mensaje);
    }

    @Test
    void testParsearMensaje_DisparoValido_DeberiaParsearCoordenadas() {
        ProtocoloBattleship.Mensaje mensaje = ProtocoloBattleship.parsearMensaje("DISPARAR|3,5");

        assertEquals("DISPARAR", mensaje.comando);
        assertEquals(3, mensaje.x);
        assertEquals(5, mensaje.y);
    }

    @Test
    void testParsearMensaje_MensajeInvalido_DeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            ProtocoloBattleship.parsearMensaje("MENSAJE_INVALIDO");
        });
    }
}

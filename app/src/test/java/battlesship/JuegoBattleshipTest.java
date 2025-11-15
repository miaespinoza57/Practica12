package battlesship;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JuegoBattleshipTest {
    private JuegoBattleship juego;

    @BeforeEach
    void setUp() {
        juego = new JuegoBattleship(null);
    }

    @Test
    void testConstructor_DeberiaInicializarTableros() {
        assertNotNull(juego);
        // Podemos verificar que los tableros se inicializan correctamente
    }

    @Test
    void testRecibirDisparo_CoordenadaVacia_DeberiaRegistrarFallo() {
        boolean resultado = juego.recibirDisparo(0, 0);
        assertFalse(resultado, "Debería ser fallo en coordenada vacía");
    }

    @Test
    void testYaDisparado_DespuesDeDisparar_DeberiaRetornarTrue() {
        juego.recibirDisparo(0, 0);
        assertTrue(juego.yaDisparado(0, 0), "Debería recordar que ya se disparó aquí");
    }
}

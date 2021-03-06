package com.github.erodriguezg.javautils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by erodriguezg on 16-05-17.
 */
public class RutConverterTest {

    @Test
    public void testGetAsObject() {
        RutConverter converter = new RutConverter();

        // casos donde deberia fallar
        String[] rutsInvalidos = {
                "dadddas",
                "dadasdd",
                "15932446-5",
                "15932426-K",
                "11111111-3",
                "11.111.111-2",
                "11.111.111-",
                "11111111-",
                ".-",
                "--",
                ".",
                "-"
        };

        for (String invalido : rutsInvalidos) {
            try {
                Integer numero = converter.asInteger(invalido);
                fail("Deberia haber fallado. esperado '" + invalido + "'retorno: '" + numero);
            } catch (RutConverterException ex) {
                // pasa por aqui para no fallar
            }
        }

        // casos que deberia funcionar

        Integer numero = converter.asInteger("15932446-k");
        assertEquals(15932446, (int) numero);

        numero = converter.asInteger("15932446-K");
        assertEquals(15932446, (int) numero);

        numero = converter.asInteger("15.932.446-k");
        assertEquals(15932446, (int) numero);

        numero = converter.asInteger("15.932.446-K");
        assertEquals(15932446, (int) numero);

        numero = converter.asInteger("1-9");
        assertEquals(1, (int) numero);

        numero = converter.asInteger("10.501.725-1");
        assertEquals(10501725, (int) numero);

        numero = converter.asInteger("11.111.111-1");
        assertEquals(11111111, (int) numero);

        numero = converter.asInteger(null);
        assertEquals(null, numero);

        numero = converter.asInteger("");
        assertEquals(null, numero);

        numero = converter.asInteger("   ");
        assertEquals(null, numero);

        numero = converter.asInteger("111111111");
        assertEquals(11111111, (int) numero);

        numero = converter.asInteger("158288249");
        assertEquals(15828824, (int) numero);

        numero = converter.asInteger("19");
        assertEquals(1, (int) numero);

    }

    @Test
    public void testGetAsString() {
        RutConverter converter = new RutConverter();

        //casos que deberia funciona

        String resultado = converter.asString(15932446);
        assertEquals("15.932.446-K", resultado);

        resultado = converter.asString(1);
        assertEquals("1-9", resultado);

        resultado = converter.asString(10501725);
        assertEquals("10.501.725-1", resultado);

        resultado = converter.asString(11111111);
        assertEquals("11.111.111-1", resultado);

        resultado = converter.asString(null);
        assertEquals(null, resultado);
    }
}

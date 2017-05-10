package com.github.erodriguezg.javautils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by takeda on 04-01-16.
 */

public class ValidacionesUtilsTest {

    private ValidacionesUtils validacionesUtils;

    @Before
    public void before() {
        this.validacionesUtils = new ValidacionesUtils();
    }

    @Test
    public void fonoMovil() {
        String[] fonosValidos = {
                "09 61234567",
                "61234567",
                "09 71234567",
                "71234567",
                "09 81234567",
                "81234567",
                "09 91234567",
                "91234567",
                "09-61234567",
                "61234567",
                "09-71234567",
                "71234567",
                "09-81234567",
                "81234567",
                "09-91234567",
                "91234567"
        };

        String[] fonosInvalidos = {
                null,
                "",
                " ",
                "dasdasd",
                "+56 032 2337814",
                "08 61234567",
                "09 51234567",
                "51234567",
                "09 11234567",
                "11234567",
                "091 81234567",
                "812345671",
                "09 912345671",
                "032 1234567"
        };

        for (String valido : fonosValidos) {
            assertEquals("fono movil: " + valido, true, validacionesUtils.fonoMovilValido(valido));
        }

        for (String invalido : fonosInvalidos) {
            assertEquals("fono movil: " + invalido, false, validacionesUtils.fonoMovilValido(invalido));
        }

    }

    @Test
    public void fonoFijoNacional() {
        String[] validos = {
                "+56 032 2911069",
                "56 032 2911069",
                "032 2911069"
        };

        String[] invalidos = {
                null,
                "",
                " ",
                "dasdasd",
                "2911069",
                "12345678",
                "0322911069"
        };

        for (String valido : validos) {
            assertEquals("fono fijo nacional: " + valido, true, validacionesUtils.fonoFijoNacional(valido));
        }

        for (String invalido : invalidos) {
            assertEquals("fono fijo nacional: " + invalido, false, validacionesUtils.fonoFijoNacional(invalido));
        }

    }


    @Test
    public void telefonos() {
        String[] validos = {
                "+56 32 2911069",
                "32 2911069",
                "+56322911069",
                "+56 9 67198770",
                "9 67198770",
                "+56967198770"
        };

        String[] invalidos = {
                null,
                "",
                " ",
                "dasdasd",
                "2911069",
                "67198770",
                "967198770"
        };

        for (String valido : validos) {
            assertEquals("telefono: " + valido, true, validacionesUtils.telefonoValido(valido));
        }

        for (String invalido : invalidos) {
            assertEquals("telefono: " + invalido, false, validacionesUtils.telefonoValido(invalido));
        }

    }


    @Test
    public void url() {
        String[] urlValidas = {
                "http://www.github.com",
                "https://www.github.com",
                "http://www.github.com?p=1",
                "http://www.github.com?p=1&p2=4",
                "http://www.github.com?p=1&p2=",
                "www.github.com",
                "www.github.com",
                "www.github.com?p=1&p2=4",
                "www.github.com?p=1&p2=",
                "http://stackoverflow.com/questions/163360/regular-expresion-to-match-urls-in-java"
        };

        String[] urlInvalidas = {
                null,
                "",
                " ",
                "dasdasd",
                "github",
                "mailinator@mailinator.com",
                "192.168.0.1",
                "032 1234567"
        };

        for (String valido : urlValidas) {
            assertEquals("url http: " + valido, true, validacionesUtils.urlValida(valido));
        }

        for (String invalido : urlInvalidas) {
            assertEquals("url http: " + invalido, false, validacionesUtils.urlValida(invalido));
        }

    }


    @Test
    public void email() {
        String[] emailsValidos = {
                "nombre@compania.com",
                "nombre@compania.gob.com",
                "nombre-a@compania.gob.com",
                "nombre_a@compania.gob.com",
                "a@b.cc",
                "a@b.c.dd"
        };

        String[] emailsInvalidos = {
                null,
                "",
                "      ",
                "nombre@ compania.com",
                "nombre@|compania.com",
                "nombre@&compania.com",
                "nombre@ñcompania.com",
                "nombre@compania@.com",
                "dadasdad",
                "algo.com.d",
                "@@@@"};

        for (String valido : emailsValidos) {
            assertEquals("email: " + valido, true, validacionesUtils.emailValido(valido));
        }

        for (String invalido : emailsInvalidos) {
            assertEquals("email: " + invalido, false, validacionesUtils.emailValido(invalido));
        }
    }


    @Test
    public void rut() {

        String[] rutsValidos = {
                "15932446-k",
                "15932446-K",
                "1-9",
                "11111111-1",
                "10501725-1",
                "15.932.446-k",
                "15.932.446-K",
                "1-9",
                "11.111.111-1",
                "10.501.725-1",
        };

        String[] rutsInvalidos = {
                null,
                "",
                "    ",
                "dadddas",
                "dadasdd",
                "15932446-5",
                "15932426-K",
                "11111111-3"
        };

        for (String valido : rutsValidos) {
            assertEquals("rut: " + valido, true, validacionesUtils.validarRut(valido));
        }

        for (String invalido : rutsInvalidos) {
            assertEquals("rut: " + invalido, false, validacionesUtils.validarRut(invalido));
        }
    }

    @Test
    public void alfanumerico() {

        String[] validos = {
                "HOLA MUNDO",
                "hola mundo",
                " hola mundo 123 ",
                "qwertyuioopasdfghjkklñzxcvbnnm1234567890",
                "QWERTYUIOPASDFGHJKLÑZXCVBNM1234567890",
                "áéíóúäëïöü",
                "ÁÉÍÓÚÄËÏÖÜ",
                "12345678909",
                "",
                "    "
        };

        String[] invalidos = {
                null,
                "!", "#", "$", "%", "+", "-", "_", ".", ";", ":"
        };

        for (String valido : validos) {
            assertEquals("alfanumerico: " + valido, true, validacionesUtils.textoAlfanumericoValido(valido));
        }

        for (String invalido : invalidos) {
            assertEquals("alfanumerico: " + invalido, false, validacionesUtils.textoAlfanumericoValido(invalido));
        }
    }

    @Test
    public void alfabetico() {

        String[] validos = {
                "HOLA MUNDO",
                "hola mundo",
                " hola mundo ",
                "qwertyuioopasdfghjkklñzxcvbnnm",
                "QWERTYUIOPASDFGHJKLÑZXCVBNM",
                "áéíóúäËÏöü",
                "ÁÉÍÓÚÄËÏÖÜ",
                "",
                "    "
        };

        String[] invalidos = {
                null,
                "!", "#", "$", "%", "+", "-", "_", ".", ";", ":", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
        };

        for (String valido : validos) {
            assertEquals("alfabetico: " + valido, true, validacionesUtils.textoAlfabeticoValido(valido));
        }

        for (String invalido : invalidos) {
            assertEquals("alfabetico: " + invalido, false, validacionesUtils.textoAlfabeticoValido(invalido));
        }
    }

}

package com.github.erodriguezg.javautils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidacionesUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ValidacionesUtils.class);

    private static final String REGEX_EMAIL = "^([A-Z0-9_%+-]+\\.?)+@([A-Z0-9-]+\\.)+[A-Z]{2,6}$";


    public boolean emailValido(String email) {
        if (email == null) {
            return false;
        }
        Pattern pat = Pattern.compile(REGEX_EMAIL, Pattern.CASE_INSENSITIVE);
        Matcher mat = pat.matcher(email);
        return mat.find();
    }

    public boolean fonoMovilValido(String email) {
        String regex = "^(09[\\s-]{1}){0,1}[9876]{1}\\d{7}";
        if (email == null) {
            return false;
        }
        return email.matches(regex);
    }

    public boolean fonoFijoNacional(String fono) {
        if (fono == null) {
            return false;
        }
        String regex = "^\\+?(56)?\\s?\\d{2,3}\\s\\d{6,8}$";
        return fono.matches(regex);
    }

    public boolean passwordValido(String password) {
        if (password == null) {
            return false;
        }

        String regex = "([0-9a-zA-ZñÑ]*)";

        if (password.length() < 6 || password.length() > 30) {
            return false;
        }

        return password.matches(regex);
    }

    public boolean validarRut(String rutEntrada) {
        if (rutEntrada == null) {
            return false;
        }

        try {
            String rut = rutEntrada.replace(".", "");

            Integer usrRut = Integer.valueOf(rut.split("-")[0]);
            String usrDv = Character.toString(rut.charAt(rut.length() - 1));

            if (usrRut == null || usrRut < 1) {
                return false;
            }

            usrDv = usrDv.toUpperCase();

            char dv = calculaDigitoVerificador(usrRut);

            return usrDv.equals(String.valueOf(dv));
        } catch (Exception ex) {
            LOG.trace("Ocurrio un error validando rut: ", ex);
            return false;
        }

    }

    public boolean urlValida(String url) {
        if (url == null) {
            return false;
        }
        String regex = "^(https?://)?[\\da-z\\.-]+\\.[a-z\\.]{2,6}[&/\\w\\?=.-]*/?";
        return url.matches(regex);
    }

    public boolean textoAlfanumericoValido(String texto) {
        if (texto == null) {
            return false;
        }
        String regex = "([0-9a-zA-Z\\sñÑáéíóúÁÉÍÓÚäëïöüÄËÏÖÜ]*)";
        return texto.matches(regex);
    }

    public boolean textoAlfabeticoValido(String texto) {
        if (texto == null) {
            return false;
        }
        String regex = "([a-zA-Z\\sñÑáéíóúÁÉÍÓÚäëïöüÄËÏÖÜ]*)";
        return texto.matches(regex);
    }

    public boolean textoAlfabeticoParaNombresValido(String texto) {
        if (texto == null) {
            return false;
        }
        String regex = "([a-zA-Z\\sñÑáéíóúÁÉÍÓÚäëïöüÄËÏÖÜ\\-'\\.,]*)";
        return texto.matches(regex);
    }

    private char calculaDigitoVerificador(Integer rutEntero) {
        int m = 0;
        int s = 1;
        int t = rutEntero;
        for (; t != 0; t /= 10) {
            s = (s + t % 10 * (9 - m++ % 6)) % 11;
        }
        return (char) ((s > 0) ? (s + 47) : 75);
    }

    public boolean telefonoValido(String value) {
        if (value == null) {
            return false;
        }
        String exp1 = "(^\\+\\d{1,4}\\s?\\d{1,2}\\s?\\d{7,8}$)";
        String exp2 = "(^\\d{1,2}\\s\\d{7,8}$)";
        return value.matches(exp1 + "|" + exp2);
    }

    public boolean textoSimpleValido(String texto) {
        if (texto == null) {
            return false;
        }
        String regex = "([a-zA-Z\\d\\sñÑáÁéÉíÍóÓúÚäëïöüÄËÏÖÜ\\.,!¡¿?:;\\-_]*)";
        return texto.matches(regex);
    }

}
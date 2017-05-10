package cl.zeke.framework.utils;

/**
 * Created by takeda on 03-01-16.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidacionesUtils {

    public boolean emailValido(String email) {
        if (email == null) {
            return false;
        }

        Pattern pat = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher mat = pat.matcher(email);

        if (mat.find()) {
            return true;
        } else {
            return false;
        }
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

    public boolean validarRut(String rut) {
        if (rut == null) {
            return false;
        }

        try {
            rut = rut.replace(".", "");

            Integer usrRut = Integer.valueOf(rut.split("-")[0]);
            String usrDv = rut.charAt(rut.length() - 1) + "";

            if (usrRut == null || usrRut < 1) {
                return false;
            }

            usrDv = usrDv.toUpperCase();

            char dv = calculaDigitoVerificador(usrRut);

            return usrDv.equals(new String("" + dv));
        } catch (Exception ex) {
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

    private char calculaDigitoVerificador(Integer T) {
        int M = 0, S = 1;
        for (; T != 0; T /= 10) {
            S = (S + T % 10 * (9 - M++ % 6)) % 11;
        }
        return (char) (S != 0 ? S + 47 : 75);
    }

    public boolean telefonoValido(String value) {
        if(value == null) {
            return false;
        }
        String exp1 = "(^\\+\\d{1,4}\\s?\\d{1,2}\\s?\\d{7,8}$)";
        String exp2 = "(^\\d{1,2}\\s\\d{7,8}$)";
        return value.matches(exp1 + "|"+exp2);
    }
}
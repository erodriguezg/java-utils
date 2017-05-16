package com.github.erodriguezg.javautils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by erodriguezg on 16-05-17.
 */
public class RutConverter {

    private static final String RUT_REGEX = "[0-9]+-[0-9kK]{1}";

    public Integer asInteger(String rutTexto) {

        if (rutTexto == null || rutTexto.trim().isEmpty()) {
            return null;
        }

        String rutField;
        char digito = 'c';

		/*
         * Create the correct mask
		 */
        Pattern mask = null;
        mask = Pattern.compile(RUT_REGEX);

		/*
         * Get the string value of the current field
		 */
        rutField = rutTexto.trim();
        String rutConPuntos = rutField.split("-")[0];
        String[] arreglorutSinPuntos = rutConPuntos.split("\\.");
        String rutSinPuntos = "";

        for (int i = 0; i < arreglorutSinPuntos.length; i++) {
            rutSinPuntos = rutSinPuntos + arreglorutSinPuntos[i];
        }

        rutSinPuntos = rutSinPuntos.trim();
        rutSinPuntos = rutSinPuntos.toUpperCase();

        String[] arreglorutSinComas = rutSinPuntos.split("\\,");

        rutSinPuntos = "";
        for (int i = 0; i < arreglorutSinComas.length; i++) {
            rutSinPuntos = rutSinPuntos + arreglorutSinComas[i];
        }

        try {
            int rutInt = Integer.parseInt(rutSinPuntos);
            if (rutInt < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            throw new RutConverterException("Por favor ingrese RUN en formato correcto");
        }

        if (rutField.contains("-")) {
            /*
			 * validar la forma del rut
			 */
            Matcher matcher = mask.matcher(rutSinPuntos + "-" + rutField.split("-")[1]);
            if (!matcher.matches()) {
                throw new RutConverterException("Por favor ingrese RUN en formato correcto");
            }
        }

		/*
		 * validar digito verificador
		 */
        //divide el rut que tiene la forma "XXXXXXXX-X"
        if (rutField.contains("-")) {
            digito = rutField.split("-")[1].charAt(0);
            //validar que sean numeros positivos
            if (Integer.parseInt(rutSinPuntos) < 1 || digito < 0) {
                throw new RutConverterException("RUN invalido");
            }
        }
        //algoritmo raro sacado de algun lado
        int M = 0, S = 1, T = Integer.parseInt(rutSinPuntos);
        for (; T != 0; T /= 10) {
            S = (S + T % 10 * (9 - M++ % 6)) % 11;
        }
        char digitoAux = (char) (S != 0 ? S + 47 : 75);

		/*
		 * Si el digito verificador no es valido manda exception
		 */
        if (!String.valueOf(digito).toUpperCase().equals(String.valueOf(digitoAux).toUpperCase())) {
            throw new RutConverterException("Dígito verificador incorrecto");
        }

        return Integer.parseInt(rutSinPuntos);
    }

    public String asString(Integer rutEntero) {
        /*
		 * convertir numero de rut a String con su digito verificador
		 */
        String rutAux = null;
        int rut;
        if (rutEntero == null) {
            rut = -1;
        } else {
            rut = (Integer) rutEntero;
        }
        if (rut > 0) {
            int M = 0;
            int S = 1;
            int T = (Integer) rut;
            for (; T != 0; T /= 10) {
                S = (S + T % 10 * (9 - M++ % 6)) % 11;
            }
            char digitoAux = (char) (S != 0 ? S + 47 : 75);
            DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
            formatSymbols.setGroupingSeparator('.');
            DecimalFormat df = new DecimalFormat();
            df.setDecimalFormatSymbols(formatSymbols);
            rutAux = df.format(rut) + "-" + digitoAux;
        }
        return rutAux;
    }


}

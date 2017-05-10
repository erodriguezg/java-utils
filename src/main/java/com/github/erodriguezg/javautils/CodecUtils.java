package com.github.erodriguezg.javautils;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class CodecUtils {

    private static final Logger LOG = LoggerFactory.getLogger(CodecUtils.class);

    public enum TypeHash {
        MD5("md5"), SHA1("sha1");

        private String nombre;

        private TypeHash(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }

    }

    public String generarHash(TypeHash typeHash, String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance(typeHash.getNombre());
            byte[] aInput = md.digest(texto.getBytes("UTF8"));
            StringBuilder result = new StringBuilder();
            char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f'};
            for (int idx = 0; idx < aInput.length; ++idx) {
                byte b = aInput[idx];
                result.append(digits[(b & 0xf0) >> 4]);
                result.append(digits[b & 0x0f]);
            }
            return result.toString();
        } catch (Exception ex) {
            LOG.error("error generarHash: ", ex);
            throw new RuntimeException("Problema al generar hash");
        }

    }

    public String generarHash(TypeHash typeHash) {
        try {
            SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
            String randomNum = Integer.toString(prng.nextInt());
            return generarHash(typeHash, randomNum);
        } catch (Exception ex) {
            LOG.error("error generarHash:", ex);
            throw new RuntimeException("Problema al generar hash");
        }
    }

    public String encodiarBase64(String plano) {
        return Base64.encodeBase64String(plano.getBytes());
    }

    public String decodiarBase64(String base64String) throws IOException {
        return new String(Base64.decodeBase64(base64String));
    }

}
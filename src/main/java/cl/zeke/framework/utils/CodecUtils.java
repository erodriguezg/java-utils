package cl.zeke.framework.utils;

/**
 * Created by takeda on 03-01-16.
 */

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class CodecUtils {

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
            ex.printStackTrace();
            throw new RuntimeException("Problema al generar hash");
        }

    }

    public String generarHash(TypeHash typeHash) {
        try {
            SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
            String randomNum = new Integer(prng.nextInt()).toString();
            return generarHash(typeHash, randomNum);
        } catch (Exception ex) {
            ex.printStackTrace();
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
package cl.zeke.framework.utils;

/**
 * Created by eduardo on 27-10-16.
 */

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/*
    ENCRIPTACION ASIMETRICA

    Generate a 2048-bit RSA private key (Puede ser 512-bit o 1024-bit)
    $ openssl genrsa -out private_key.pem 2048
    Convert private Key to PKCS#8 format (so Java can read it)
    $ openssl pkcs8 -topk8 -inform PEM -outform DER -in private_key.pem -out private_key.der -nocrypt
    Output public key portion in DER format (so Java can read it)
    $ openssl rsa -in private_key.pem -pubout -outform DER -out public_key.der

    ENCRIPTACION SIMETRICA

    basada en AES 128-bit

    ENCRIPTACIÓN HIBRIDA
    - generar key dinamica para algoritmo AES simetrico
    - encriptar key dinamica con RSA
    - encriptar contenido/objetivo con AES simetrico
    - enviar contenido cifrado simetrico con clave cifrada asimetrica
    - desencriptar clave cifrada
    - desencriptar contenido con clave simetrica

 */
public class RsaAesEncryptionUtils implements EncryptionUtils {

    public class AesOptions implements Options {

        private final int iterationCount;
        private final int keySize;
        private final String salt;
        private final String iv;

        public AesOptions(int iterationCount, int keySize, String salt, String iv) {
            this.iterationCount = iterationCount;
            this.keySize = keySize;
            this.salt = salt;
            this.iv = iv;
        }

        public int getIterationCount() {
            return iterationCount;
        }

        public int getKeySize() {
            return keySize;
        }

        public String getSalt() {
            return salt;
        }

        public String getIv() {
            return iv;
        }
    }

    public static AesOptions createDefaultAesOptions() {
        return new RsaAesEncryptionUtils().new AesOptions(1000, 128, "99ff32e36fcc3322438a3199ccf03523", "3c78bdd75d53f68c9f6b421ca646693b");
    }

    /*
    filename: ruta hacia archivo public key DER
     */
    @Override
    public PublicKey loadPublicKey(String filename) {
        try {
            byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new IllegalStateException(ex);
        }
    }

    /*
    filename: ruta hacia archivo private key DER
     */
    @Override
    public PrivateKey loadPrivateKey(String filename) {
        try {
            final byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new IllegalStateException(ex);
        }
    }

    /****************************
     * ASYMMETRIC RSA
     ******************************/

    @Override
    public byte[] encryptAsymmetric(final byte[] normalBytes, final Key key) {
        return encryptAsymmetric(normalBytes, key, null);
    }

    @Override
    public byte[] encryptAsymmetric(final byte[] normalBytes, final Key key, final Options options) {
        try {
            final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(normalBytes, 0, normalBytes.length);
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public byte[] decryptAsymmetric(final byte[] cipherBytes, final Key key) {
        return decryptAsymmetric(cipherBytes, key, null);
    }

    @Override
    public byte[] decryptAsymmetric(final byte[] cipherBytes, final Key key, final Options options) {
        try {
            final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(cipherBytes);
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public String encryptAsymmetricToB64(final byte[] normalBytes, final Key key) {
        return base64(encryptAsymmetric(normalBytes, key));
    }

    @Override
    public String encryptAsymmetricToB64(final byte[] normalBytes, final Key key, final Options options) {
        return base64(encryptAsymmetric(normalBytes, key, options));
    }

    @Override
    public byte[] decryptAsymmetricFromB64(final String cipherTextB64, final Key key) {
        return decryptAsymmetric(base64(cipherTextB64), key);
    }

    @Override
    public byte[] decryptAsymmetricFromB64(final String cipherTextB64, final Key key, final Options options) {
        return decryptAsymmetric(base64(cipherTextB64), key, options);
    }

    /****************************
     * SYMMETRIC: AES
     ******************************/

    @Override
    public byte[] encryptSymmetric(final byte[] normalBytes, final String passphrase) {
        return encryptSymmetric(normalBytes, passphrase, createDefaultAesOptions());
    }

    @Override
    public byte[] encryptSymmetric(byte[] normalBytes, final String passphrase, Options options) {
        try {
            AesOptions aesOptions = (AesOptions) options;
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKey secretKey = this.generateKey(passphrase, aesOptions);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(hex(aesOptions.getIv())));
            return cipher.doFinal(normalBytes);
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public byte[] decryptSymmetric(final byte[] cipherBytes, final String passphrase) {
        return decryptSymmetric(cipherBytes, passphrase, createDefaultAesOptions());
    }

    @Override
    public byte[] decryptSymmetric(byte[] cipherBytes, final String passphrase, Options options) {
        try {
            AesOptions aesOptions = (AesOptions) options;
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKey secretKey = this.generateKey(passphrase, aesOptions);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(hex(aesOptions.getIv())));
            return cipher.doFinal(cipherBytes);
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public String encryptSymmetricToB64(final byte[] normalBytes, final String passphrase) {
        return base64(encryptSymmetric(normalBytes, passphrase));
    }

    @Override
    public String encryptSymmetricToB64(byte[] normalBytes, final String passphrase, Options options) {
        return base64(encryptSymmetric(normalBytes, passphrase, options));
    }

    @Override
    public byte[] decryptSymmetricFromB64(final String base64Text, final String passphrase) {
        return decryptSymmetric(base64(base64Text), passphrase);
    }

    @Override
    public byte[] decryptSymmetricFromB64(String base64Text, final String passphrase, Options options) {
        return decryptSymmetric(base64(base64Text), passphrase, options);
    }

    //privados

    private SecretKey generateKey(String passphrase, AesOptions aesOptions) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), hex(aesOptions.getSalt()), aesOptions.getIterationCount(), aesOptions.getKeySize());
            return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException(e);
        }
    }

    private String base64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    private byte[] base64(String str) {
        return Base64.decodeBase64(str);
    }

    public byte[] hex(String str) {
        try {
            return Hex.decodeHex(str.toCharArray());
        } catch (DecoderException e) {
            throw new IllegalStateException(e);
        }
    }

}

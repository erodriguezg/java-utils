package cl.zeke.framework.utils;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by eduardo on 27-10-16.
 */
public interface EncryptionUtils {

    public interface Options {

    }

    PublicKey loadPublicKey(final String filename);

    PrivateKey loadPrivateKey(final String filename);

    byte[] encryptAsymmetric(final byte[] normalBytes, final Key key);

    byte[] encryptAsymmetric(final byte[] normalBytes, final Key key, final Options options);

    byte[] decryptAsymmetric(final byte[] cipherBytes, final Key key);

    byte[] decryptAsymmetric(final byte[] cipherBytes, final Key key, final Options options);

    String encryptAsymmetricToB64(final byte[] normalBytes, final Key key);

    String encryptAsymmetricToB64(final byte[] normalBytes, final Key key, final Options options);

    byte[] decryptAsymmetricFromB64(final String cipherTextB64, final Key key);

    byte[] decryptAsymmetricFromB64(final String cipherTextB64, final Key key, final Options options);

    byte[] encryptSymmetric(final byte[] normalBytes, final String passphrase);

    byte[] encryptSymmetric(final byte[] normalBytes, final String passphrase, Options options);

    byte[] decryptSymmetric(final byte[] cipherBytes, final String passphrase);

    byte[] decryptSymmetric(final byte[] cipherBytes, final String passphrase, Options options);

    String encryptSymmetricToB64(final byte[] normalBytes, final String passphrase);

    String encryptSymmetricToB64(final byte[] normalBytes, final String passphrase, Options options);

    byte[] decryptSymmetricFromB64(final String base64Text, final String passphrase);

    byte[] decryptSymmetricFromB64(final String base64Text, final String passphrase, Options options);

}

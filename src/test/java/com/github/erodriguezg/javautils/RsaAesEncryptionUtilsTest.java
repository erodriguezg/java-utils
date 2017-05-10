package com.github.erodriguezg.javautils;

/**
 * Created by eduardo on 27-10-16.
 */

import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class RsaAesEncryptionUtilsTest {

    private static final String TEXTO_PLANO = "QWERTYUIOP12345678-QWERTYUIOP12345678ÁÉÍÓÚ";

    private static final String TEXTO_PLANO_LARGO =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et " +
                    "dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut " +
                    "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse " +
                    "cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in " +
                    "culpa qui officia deserunt mollit anim id est laborum. áéíúóÁÉÍÓÚÑñ";


    private PublicKey publicKey;
    private PrivateKey privateKey;
    private RsaAesEncryptionUtils encryptionUtils;
    private String passPhrase;

    @Before
    public void before() {
        this.encryptionUtils = new RsaAesEncryptionUtils();
        this.publicKey = this.encryptionUtils.loadPublicKey(RsaAesEncryptionUtilsTest.class.getResource("/utils/public.der").getFile());
        this.privateKey = this.encryptionUtils.loadPrivateKey(RsaAesEncryptionUtilsTest.class.getResource("/utils/private.der").getFile());
        this.passPhrase = UUID.randomUUID().toString();
    }

    @Test
    public void testEncriptarAsimetricoPublicDesencriptarAsimetricoPrivate() throws Exception {
        byte[] encriptadoBytes = this.encryptionUtils.encryptAsymmetric(TEXTO_PLANO.getBytes("UTF-8"), this.publicKey);
        byte[] desencriptadoBytes = this.encryptionUtils.decryptAsymmetric(encriptadoBytes, this.privateKey);
        assertThat(TEXTO_PLANO).isEqualTo(new String(desencriptadoBytes).trim());
    }

    @Test
    public void testEncriptarB64AsimetricoPublicDesencriptarB64AsimetricoPrivate() throws Exception {
        String encriptadoB64 = this.encryptionUtils.encryptAsymmetricToB64(TEXTO_PLANO.getBytes("UTF-8"), this.publicKey);
        byte[] desencriptadoBytes = this.encryptionUtils.decryptAsymmetricFromB64(encriptadoB64, this.privateKey);
        assertThat(TEXTO_PLANO).isEqualTo(new String(desencriptadoBytes).trim());
    }


    @Test
    public void testEncriptarSimetricoDesencriptarSimetrico() {
        byte[] encriptadoBytes = this.encryptionUtils.encryptSymmetric(TEXTO_PLANO_LARGO.getBytes(), this.passPhrase);
        byte[] desencriptadosBytes = this.encryptionUtils.decryptSymmetric(encriptadoBytes, this.passPhrase);
        assertThat(TEXTO_PLANO_LARGO).isEqualTo(new String(desencriptadosBytes).trim());
    }

    @Test
    public void testEncriptarSimetricoB64DesencriptarSimetricoB64() {
        String encriptadoB64 = this.encryptionUtils.encryptSymmetricToB64(TEXTO_PLANO_LARGO.getBytes(), this.passPhrase);
        System.out.println("Encode B64: " + encriptadoB64);
        byte[] desencriptadosBytes = this.encryptionUtils.decryptSymmetricFromB64(encriptadoB64, this.passPhrase);
        assertThat(TEXTO_PLANO_LARGO).isEqualTo(new String(desencriptadosBytes).trim());
    }

    @Test
    public void testIntegracionRSAConJSBN() {
        String b64Encriptado = "BmO9oR8IoPiG2Lni771DdQH4SIqUtECpJ78xzsTXaUSPQzSY4dIrgrcETcPX9YrC7U+N0dTQw7quGSK4efW/MQ=="; //zekepass
        byte[] bytesDesencriptados = this.encryptionUtils.decryptAsymmetricFromB64(b64Encriptado, this.privateKey);
        String resultado = new String(bytesDesencriptados).trim();
        System.out.println("desencriptado: " + resultado);
        assertThat(resultado).isEqualTo("zekepass");
    }

    @Test
    public void testIntegracionCryptoJS() {
        String b64Encriptado = "sl+NzkdNiPRZlUy9b0cxXhn0tQDfHVGE1h7MSRnzkb7FzkUKHkTmo/awhi+I59Rj81x/zSbTEuSh87qqhA8BXGP3QUhL/cqK8rraZ+yg44/UK1KeHfXjQXXqlGD7UcYfzrkpYYz0VVCzV+/U48re1P6mrVqm8NysGsTqVXlid9uCMYmah19MLc4l2o28aJn6TWRdWYebDTi2jvXB/UIHBfNDjagGBMvI98oKu1OwY0uqWOpV2BpS6q0FHBNcwG9c8kCSNly6KeF0ENfgvBU7R83UFwmtZ4x6QU7flEkXi8DkGb4rrYcs+76k8UBAb1N0FUKWnDr8NVVhiv9BFCSF979++CFS6MJiiS/3P2T23mtwjHPCTKUmB8LqQgC4/1yDs7SSmeuI9SZt9XsnLZjwtw==";
        String textoEsperado = "{\"camposRequeridos\": {\"RUN\": \"10091285-6\", \"claveunica\" : \"123456\"},\"claseId\": 2,\"claseNombre\": \"Certificado Defunción\",\"tipoId\": 4,\"tipoNombre\": \"Para Asignación Familiar\",\"tipoNombreReducida\": \"Asig. Familiar\",\"valorCLP\": 0,\"privado\": false,\"pagado\": false,\"dataB64\": null,\"fechaObtencion\": null}";
        String salt = "6b9af7ff49a9185e95dc6fca4effbb70";
        String iv = "31423f4b5da921bda955603291db277c";
        String passPhrase ="zekepass";
        int iteraciones = 1000;
        int keySize = 128;
        RsaAesEncryptionUtils.AesOptions aesOptions = new RsaAesEncryptionUtils().new AesOptions(iteraciones,keySize, salt, iv);
        byte[] bytesDesencriptado = this.encryptionUtils.decryptSymmetricFromB64(b64Encriptado, passPhrase, aesOptions);
        String resultado = new String(bytesDesencriptado).trim();
        System.out.println("desencriptado: " + resultado);
        assertThat(resultado).isEqualTo(textoEsperado);
    }

    @Test
    public void encriptarHibridoDesencriptarHibridoB64() {
        //encriptación hibrida
        System.out.println("passPhrase: " + this.passPhrase);
        RsaAesEncryptionUtils.AesOptions aesOptions = RsaAesEncryptionUtils.createDefaultAesOptions();
        String contenidoEncriptado = this.encryptionUtils.encryptSymmetricToB64(TEXTO_PLANO_LARGO.getBytes(), this.passPhrase, aesOptions);
        String llaveEncriptada = this.encryptionUtils.encryptAsymmetricToB64(this.passPhrase.getBytes(), this.privateKey);
        System.out.println("Contenido encriptado: " + contenidoEncriptado);
        System.out.println("Llave encriptada: " + llaveEncriptada);

        //desencriptación hibrida
        byte[] bytesLlaveDesencriptada = this.encryptionUtils.decryptAsymmetricFromB64(llaveEncriptada, this.publicKey);
        String llaveDesencriptada = new String(bytesLlaveDesencriptada).trim();
        byte[] bytesContenidoDesencriptado = this.encryptionUtils.decryptSymmetricFromB64(contenidoEncriptado, llaveDesencriptada, aesOptions);
        String contenidoDesencriptado = new String(bytesContenidoDesencriptado).trim();

        assertThat(llaveDesencriptada).isEqualTo(this.passPhrase);
        assertThat(contenidoDesencriptado).isEqualTo(TEXTO_PLANO_LARGO);
    }

}

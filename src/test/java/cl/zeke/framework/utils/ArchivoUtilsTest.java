package cl.zeke.framework.utils;

/**
 * Created by takeda on 04-01-16.
 */

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author takeda
 */
public class ArchivoUtilsTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ArchivoUtils achivoUtils;
    private String tmpDir;
    private File fileEntrada;


    @Before
    public void before() {
        tmpDir = System.getProperty("java.io.tmpdir");
        achivoUtils = new ArchivoUtils(new DataDirectoryUtils(), tmpDir);
        fileEntrada = new File(ArchivoUtilsTest.class.getResource("/utils/archivotest.txt").getFile());
    }

    @Test
    public void guardarArchivo() throws IOException {
        String rutaRelativa = "/archivoutils/test/holamundo.txt";
        achivoUtils.guardarArchivo(fileEntrada, rutaRelativa);
        File fileSalida = new File(tmpDir + rutaRelativa);
        fileSalida.deleteOnExit();
        assertThat(FileUtils.readFileToString(fileEntrada))
                .isEqualTo(FileUtils.readFileToString(fileSalida));
    }

    @Test
    public void dadoRutaRelativaNulaCuandoGuardaArchivoEntoncesExcepcion() {
        expectedException.expect(IllegalStateException.class);
        achivoUtils.guardarArchivo(fileEntrada, null);
    }

    @Test
    public void eliminarArchivo() {
        String rutaRelativa = "/archivoutils/test/holamundo.txt";
        achivoUtils.guardarArchivo(fileEntrada, rutaRelativa);
        achivoUtils.eliminarArchivo(rutaRelativa);
        File fileSalida = new File(tmpDir + rutaRelativa);
        assertThat(fileSalida.exists()).isEqualTo(false);
    }

    @Test
    public void obtenerArchivo() throws IOException {
        String rutaRelativa = "/archivoutils/test/holamundo.txt";
        achivoUtils.guardarArchivo(fileEntrada, rutaRelativa);
        File fileSalidaEsperado = new File(tmpDir + rutaRelativa);
        fileSalidaEsperado.deleteOnExit();
        File fileSalidaActual = achivoUtils.obtenerArchivo(rutaRelativa);
        assertThat(FileUtils.readFileToString(fileSalidaEsperado))
                .isEqualTo(FileUtils.readFileToString(fileSalidaActual));

    }

}
package com.github.erodriguezg.javautils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.UUID;

public class ArchivoUtils {

    private final static Logger LOG = LoggerFactory.getLogger(ArchivoUtils.class);

    private final String appDataDir;
    private final DataDirectoryUtils dataDirectoryUtils;

    public ArchivoUtils(DataDirectoryUtils dataDirectoryUtils, String appDataDir) {
        this.dataDirectoryUtils = dataDirectoryUtils;
        this.appDataDir = appDataDir;
    }

    public void guardarArchivo(File fileInput, String rutaRelativa) {
        try (InputStream is = new FileInputStream(fileInput)) {
            dataDirectoryUtils.guardarRecurso(rutaRelativa, appDataDir, is);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    public void eliminarArchivo(String rutaRelativa) {
        File fileToDelete = new File(appDataDir + rutaRelativa);
        if (!fileToDelete.exists()) {
            return;
        }
        boolean deleted = fileToDelete.delete();
        if (!deleted) {
            throw new IllegalStateException("No se puedo eliminar archivo: " + fileToDelete.getAbsolutePath());
        }
    }

    public File obtenerArchivo(String rutaRelativa) {
        File fileArchivo = new File(appDataDir + rutaRelativa);
        if (fileArchivo.exists()) {
            return fileArchivo;
        } else {
            return null;
        }
    }

    public static File obtenerClasspathFile(String path) {
        return obtenerClasspathFile(path, ".tmp", ArchivoUtils.class);
    }

    public static File obtenerClasspathFile(String path, Class clazz) {
        return obtenerClasspathFile(path, ".tmp", clazz);
    }

    public static File obtenerClasspathFile(String path, String suffix, Class clazz) {
        File fileTemp;
        try {
            fileTemp = File.createTempFile(UUID.randomUUID().toString(), suffix);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
        try (InputStream inputStream = clazz.getResourceAsStream(path);
             OutputStream outputStream = new FileOutputStream(fileTemp)) {
            IOUtils.copy(inputStream, outputStream);
            return fileTemp;
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public static InputStream obtenerClassPathInputStream(String path) {
        return obtenerClassPathInputStream(path, ArchivoUtils.class);
    }

    public static InputStream obtenerClassPathInputStream(String path, Class clazz) {
        return clazz.getResourceAsStream(path);
    }

    public static File crearArchivoDesdeExcepcion(Exception ex) {
        try {
            String stacktrace = ExceptionUtils.getFullStackTrace(ex);
            File fileTmp = File.createTempFile(UUID.randomUUID().toString(), ".falla_critica.tmp");
            fileTmp.deleteOnExit();
            try (Writer writer = new FileWriter(fileTmp)) {
                writer.append(stacktrace);
            }
            return fileTmp;
        } catch (IOException ex2) {
            LOG.error("error al crearArchivoDesdeExcepcion: ", ex2);
            throw new IllegalStateException(ex2);
        }
    }

    public static File consolidarArchivos(File[] archivos) throws IOException {
        if (archivos == null) {
            throw new IllegalStateException("Los archivos son nulos!");
        }
        File archivoConsolidado = File.createTempFile(UUID.randomUUID().toString(), ".consolidado.tmp");
        try (OutputStream outputStream = new FileOutputStream(archivoConsolidado)) {
            for (File archivo : archivos) {
                appendFile(outputStream, archivo);
            }
        }
        return archivoConsolidado;
    }

    private static void appendFile(OutputStream outputStream, File fileToAppend) throws IOException {
        try (InputStream inputStream = new FileInputStream(fileToAppend)) {
            IOUtils.copy(inputStream, outputStream);
        }
    }

}
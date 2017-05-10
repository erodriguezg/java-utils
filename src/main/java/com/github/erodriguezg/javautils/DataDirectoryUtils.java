package com.github.erodriguezg.javautils;

import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class DataDirectoryUtils {

    private static final Logger LOG = LoggerFactory.getLogger(DataDirectoryUtils.class);
    private static final String URL_INDENTIFICADOR_INVALIDO = "identificador invalido";

    public String obtenerFileNameEstandar(String nombreArchivoEntrada, String mimeType) {
        String nombreArchivo = nombreArchivoEntrada
                .trim()
                .toLowerCase()
                .replace(" ", "_");
        return nombreArchivo + obtenerExtensionArchivoPorMimeType(mimeType);
    }

    public String obtenerExtensionArchivoPorMimeType(String mimeType) {
        try {
            return MimeTypes.getDefaultMimeTypes().forName(mimeType).getExtension();
        } catch (MimeTypeException ex) {
            LOG.error("error obtenerExtensionArchivoPorMimeType", ex);
            throw new RuntimeException(ex);
        }
    }

    public String obtenerMimeType(byte[] bytesRecurso) {
        return new Tika().detect(bytesRecurso);
    }

    public String obtenerMimeType(File file) {
        try {
            return new Tika().detect(file);
        } catch (IOException ex) {
            LOG.error("error obtenerMimeType", ex);
            throw new RuntimeException(ex);
        }
    }

    public String obtenerMimeType(InputStream inputStream) {
        try {
            return new Tika().detect(inputStream);
        } catch (IOException ex) {
            LOG.error("error obtenerMimeType", ex);
            throw new RuntimeException(ex);
        }
    }

    public InputStream obtenerInputStream(String urlRecurso, String directorioBase) {
        if (urlRecurso == null) {
            throw new IllegalArgumentException(URL_INDENTIFICADOR_INVALIDO);
        }

        if (!existeRecurso(urlRecurso, directorioBase)) {
            return null;
        }

        try {
            return new FileInputStream(directorioBase + urlRecurso);
        } catch (Exception e) {
            LOG.error("error obtenerInputStream: ", e);
            throw new RuntimeException("no se puede obtener OutputStream:" + directorioBase + urlRecurso);
        }
    }

    public OutputStream obtenerOutputStream(String urlRecurso, String directorioBase) {
        if (urlRecurso == null) {
            throw new IllegalArgumentException(URL_INDENTIFICADOR_INVALIDO);
        }

        //se elimina el recurso si existia
        if (existeRecurso(urlRecurso, directorioBase)) {
            eliminarRecurso(urlRecurso, directorioBase);
        }

        crearDirectorios(urlRecurso, directorioBase);

        //se crea nuevo recurso
        try {
            return new FileOutputStream(directorioBase + urlRecurso);
        } catch (Exception e) {
            LOG.error("error obtenerOutputStream: ", e);
            throw new RuntimeException("no se puede obtener OutputStream:" + directorioBase + urlRecurso);
        }
    }

    public void guardarRecurso(String urlRecurso, String directorioBase, InputStream inputStream) {
        if (urlRecurso == null) {
            throw new IllegalArgumentException(URL_INDENTIFICADOR_INVALIDO);
        }
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream del recurso son nulos");
        }

        //se elimina el recurso si existia
        if (existeRecurso(urlRecurso, directorioBase)) {
            eliminarRecurso(urlRecurso, directorioBase);
        }

        crearDirectorios(urlRecurso, directorioBase);

        //se crea nuevo recurso
        try (FileOutputStream fos = new FileOutputStream(directorioBase + urlRecurso)) {
            IOUtils.copy(inputStream, fos);
        } catch (Exception ex) {
            LOG.error("error", ex);
            throw new RuntimeException("problema al guardar recurso: " + urlRecurso);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception ex) {
                LOG.warn("No se pudo cerrar inputstream: ", ex);
            }
        }
    }

    private void crearDirectorios(String identificador, String directorioBase) {
        assert (identificador != null);

        String[] directorios = identificador.split("/");

        String directorio = (directorioBase + identificador).replace("/" + directorios[directorios.length - 1], "");
        File dir = new File(directorio);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("no se pudo crear el directorio");
        }
    }

    private void eliminarRecurso(String identificador, String directorioBase) {
        if (identificador == null) {
            throw new IllegalArgumentException(URL_INDENTIFICADOR_INVALIDO);
        }

        File file = new File(directorioBase + identificador);
        if (file.exists() && file.canWrite() && !file.delete()) {
            LOG.warn("No se pudo eliminar archivo: {} ", file);
        }
    }

    private boolean existeRecurso(String identificador, String directorioBase) {
        assert (identificador != null);
        File file = new File(directorioBase + identificador);
        return file.exists();
    }

}
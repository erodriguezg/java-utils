package com.github.erodriguezg.javautils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {

    private final Logger LOG = LoggerFactory.getLogger(ImageUtils.class);

    /**
     * Valida tamaño width y height. si es -1 la omite
     *
     * @param bufImage imagen de entrada a validar
     * @param width largo esperado. si es -1 se omite
     * @param height alto esperado. si es -1 se omite
     * @return true sí es valido. false en caso contrario
     */
    public boolean proporcionesValidas(BufferedImage bufImage, int width, int height) {
        return (bufImage.getWidth() == width || width == -1) && (bufImage.getHeight() == height || height == -1);
    }

    public BufferedImage toBufferImage(byte[] bytesImage) {
        try (InputStream imageInputStrem = new ByteArrayInputStream(bytesImage)) {
            return ImageIO.read(imageInputStrem);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    public byte[] escalar(byte[] img, int width, int height, String extensionImagen) {
        try (InputStream is = new ByteArrayInputStream(img);
             ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            BufferedImage bufImage = ImageIO.read(is);
            ImageIO.write(escalar(bufImage, width, height), extensionImagen, os);
            return os.toByteArray();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    private BufferedImage escalar(BufferedImage img, int width, int height) throws IOException {
        // Create new (blank) image of required (scaled) size
        LOG.debug("escalando a {}x{}", new Object[]{width, height});

        BufferedImage scaledImage = new BufferedImage(
                width, height, BufferedImage.TYPE_INT_RGB);

        // Paint scaled version of image to new image
        Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(img, 0, 0, width, height, null);

        // clean up
        return scaledImage;
    }

    public String obtenerFormato(String contentType) {
        if (contentType == null) {
            throw new RuntimeException("Problem getting url contentType is null!");
        }

        String format = contentType.toLowerCase().trim().replace("image/", "");

        if (format == null || format.length() == 0) {
            throw new RuntimeException("Unknow image mime type");
        }

        if (format.contains(";")) //contains a semicolon
        {
            format = format.split(";")[0];
        }

        if (format == null || format.length() == 0) {
            throw new RuntimeException("Unknow image mime type");
        }
        return format;
    }

}

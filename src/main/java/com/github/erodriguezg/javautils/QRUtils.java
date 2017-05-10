package com.github.erodriguezg.javautils;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

import java.io.ByteArrayOutputStream;

public class QRUtils {

    public QRCode generarQR(String texto) {
        return QRCode.from(texto);
    }

    public byte[] generarBufferedImageQR(String texto, ImageType imageType, int largo, int alto) {
        QRCode qrcode = generarQR(texto).withSize(largo, alto).to(imageType);
        try(ByteArrayOutputStream os = qrcode.stream()) {
            return os.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
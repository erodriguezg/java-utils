package cl.zeke.framework.utils;

/**
 * Created by takeda on 03-01-16.
 */

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

import java.io.ByteArrayOutputStream;

public class QRUtils {

    public QRCode generarQR(String texto) {
        return QRCode.from(texto);
    }

    public byte[] generarBufferedImageQR(String texto, ImageType imageType, int largo, int alto) {
        try {
            QRCode qrcode = generarQR(texto).withSize(largo, alto).to(imageType);
            ByteArrayOutputStream os = qrcode.stream();
            byte[] qrBytes = os.toByteArray();
            os.flush();
            os.close();
            os = null;
            return qrBytes;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
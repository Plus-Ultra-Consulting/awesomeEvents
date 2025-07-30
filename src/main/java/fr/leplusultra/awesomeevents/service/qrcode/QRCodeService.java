package fr.leplusultra.awesomeevents.service.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;

@Transactional(readOnly = true)
@Service
public class QRCodeService {
    @Value("${qrcode.size}")
    private int qrCodeSize;

    public BitMatrix generateBasicQRCode(String data) throws WriterException {
        return new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize);
    }

    public BufferedImage convertToImage(BitMatrix bitMatrix) {
        return MatrixToImageWriter.toBufferedImage(bitMatrix, new MatrixToImageConfig(MatrixToImageConfig.BLACK, MatrixToImageConfig.WHITE));
    }
}

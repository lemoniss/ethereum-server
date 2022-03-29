package com.maxlength.component;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class QRcode {

    public String create(String address) throws Exception {

        BitMatrix bitMatrix = null;
        MatrixToImageConfig matrixToImageConfig;

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        matrixToImageConfig = new MatrixToImageConfig();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, "L");

        int width = 100;
        int height = 100;

        try {
            bitMatrix = qrCodeWriter.encode(address, BarcodeFormat.QR_CODE, width, height);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "png", outputStream, matrixToImageConfig);

        return "data:image/png;base64," + Base64.getEncoder().encodeToString(outputStream.toByteArray());

    }
}


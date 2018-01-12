import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QRCode {
    private static final String QR_CODE_IMAGE_PATH = "./QRCode.png";

    private static void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }


    private static String decodeQRCodeImage(File imgFile) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(imgFile);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            System.out.println("Can not doecode the image");
            return null;
        }


    }

    public static void main(String[] args) {
        System.setProperty("java.specification.version","1.9"); // ZXing只能识别1.X这种形式的java版本
        try {
            generateQRCodeImage("http://www.baidu.com", 350, 350, QR_CODE_IMAGE_PATH);
            System.out.println("QR Code Generated!");
            System.out.println("Now we're going to decode the image:");
            System.out.println("Texts: "+ decodeQRCodeImage(new File(QR_CODE_IMAGE_PATH)));
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }
    }

}

package pers.louisun.qrcode;

import com.google.zxing.WriterException;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class Controller {

    @FXML
    private Button bgenerate = new Button();
    @FXML
    private Button bexport = new Button();
    @FXML
    private Button bopen  = new Button();
    @FXML
    private Button bcopy = new Button();
    @FXML
    private ImageView imgview1 = new ImageView();
    @FXML
    private ImageView imgview2 = new ImageView();
    @FXML
    private TextField tcode = new TextField();
    @FXML
    private TextField tdecode = new TextField();


    public File findImage(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open QR Code File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser.showOpenDialog(new Stage());
    }

    public File findDirectory(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Directory");
        return directoryChooser.showDialog(new Stage());

    }
    @FXML
    public void bgenerateOnClick(){
        String text = tcode.getText();
        if(text.isEmpty()) return;
        try {
            imgview1.setImage(QRCode.generateQRCodeImage(text, 300,300));
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void bexportOnClick(){
        Image img = imgview1.getImage();
        if(img == null) return;
        File dir = findDirectory();
        if(dir==null) return;
        File savefile = Paths.get(dir.toString(), "QRCode.png").toFile();
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(img, null), "PNG", savefile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setWidth(400);
        alert.setTitle("通知");
        alert.setHeaderText("生成图片成功");
        alert.setContentText("请到对应目录查看生成的二维码");
        alert.show();

    }

    @FXML
    public void bopenOnClick(){
        File file = findImage();
        if(file == null) return;
        try {
            imgview2.setImage(new Image(file.toURI().toString()));
            String result = QRCode.decodeQRCodeImage(file);
            tdecode.setText(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void bcopyOnClick(){
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(tdecode.getText());
        clipboard.setContent(content);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setWidth(300);
        alert.setTitle("通知");
        alert.setHeaderText("复制成功");
        alert.setContentText("请检查你的剪贴板");
        alert.show();
    }




}

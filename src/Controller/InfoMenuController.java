package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class InfoMenuController {
    @FXML
    private Button backButton;
    @FXML
    private Button downloadButton;
    @FXML
    private Button doneButton;

    @FXML
    private Label titleLabel;

    @FXML
    private TextArea infoTextArea;


    private static String title;
    private static String documentTitle;
    private static String info;


    @FXML
    private void initialize() {
        titleLabel.setText(title);
        infoTextArea.setText(info);


        backButton.setOnAction(event -> backButton.getScene().getWindow().hide());

        doneButton.setOnAction(event -> {
            doneButton.getScene().getWindow().hide();
        });

        downloadButton.setOnAction(event -> {
            download();
        });
    }

    public static void initializeData(String title, String documentTitle, String info) {
        InfoMenuController.title = title;
        InfoMenuController.documentTitle = documentTitle;
        InfoMenuController.info = info;
    }

    private void download() {
        String link = "D:" + File.separator + "Desktop" + File.separator + documentTitle + ".txt";
        File newFile = new File(link);
        if (!newFile.exists()) {
            try {
                newFile.createNewFile();
                PrintWriter printWriter = new PrintWriter(newFile);
                printWriter.write(info);
                printWriter.flush();
                printWriter.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
                alert.setTitle("Storage Keeper");
                alert.setHeaderText("Звіт успішно завантажено на робочий стіл");
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
            alert.setTitle("Storage Keeper");
            alert.setHeaderText("На робочому столі вже є такий звіт");
            alert.showAndWait();
        }
    }
}

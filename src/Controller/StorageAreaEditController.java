package Controller;

import DAO.AreaDaoImpl;
import DTO.Area;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

public class StorageAreaEditController {
    @FXML
    private Button backButton;
    @FXML
    private Button doneButton;

    @FXML
    private Label titleLabel;

    @FXML
    private TextField nameField;


    private static Area area;
    private AreaDaoImpl areaDao = new AreaDaoImpl();


    @FXML
    private void initialize() {
        if(area == null) {
            titleLabel.setText("Створення нової ділянки");
        } else {
            titleLabel.setText("Редагування ділянки");
            setData();
        }

        backButton.setOnAction(event -> backButton.getScene().getWindow().hide());

        doneButton.setOnAction(event -> {
            if (isInputValid()) {
                Area newArea = new Area(
                        null,
                        nameField.getText().trim()

                );

                if (area == null) {
                    Integer id = areaDao.save(newArea);
                    newArea.setAreaId(id);

                    Main.getStorageMenuController().addArea(newArea);
                } else {
                    newArea.setAreaId(area.getAreaId());
                    areaDao.update(newArea);

                    Main.getStorageMenuController().setAreaTable();
                }
                doneButton.getScene().getWindow().hide();
            }
        });
    }

    public static void initializeData(Area area) {
        StorageAreaEditController.area = area;
    }

    private void setData() {
        nameField.setText(area.getName());
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText().trim().length() == 0) {
            errorMessage += "Не вказано назву!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
            alert.setTitle("Storage Keeper");
            alert.setHeaderText("Будь ласка, заповніть усі поля");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }
    }
}

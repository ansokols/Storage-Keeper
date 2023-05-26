package Controller;

import DAO.TypeDaoImpl;
import DTO.Type;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

public class TypeEditController {
    @FXML
    private Button backButton;
    @FXML
    private Button doneButton;

    @FXML
    private Label titleLabel;

    @FXML
    private TextField nameField;


    private static Type type;
    private final TypeDaoImpl typeDao = new TypeDaoImpl();


    @FXML
    private void initialize() {
        if (type == null) {
            titleLabel.setText("Створення нового типу товару");
        } else {
            titleLabel.setText("Редагування типу товару");
            setData();
        }

        backButton.setOnAction(event -> backButton.getScene().getWindow().hide());

        doneButton.setOnAction(event -> {
            if (isInputValid()) {
                Type newType = new Type(
                        null,
                        nameField.getText().trim()
                );

                if (type == null) {
                    Integer id = typeDao.save(newType);
                    newType.setTypeId(id);

                    Main.getTypeMenuController().addType(newType);
                } else {
                    newType.setTypeId(type.getTypeId());
                    typeDao.update(newType);

                    Main.getTypeMenuController().setTypeTable();
                }
                doneButton.getScene().getWindow().hide();
            }
        });
    }

    public static void initializeData(Type type) {
        TypeEditController.type = type;
    }

    private void setData() {
        nameField.setText(type.getName());
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
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
            alert.setTitle("Storage Keeper");
            alert.setHeaderText("Будь ласка, заповніть усі поля");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }
    }
}

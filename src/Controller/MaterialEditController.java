package Controller;

import DAO.MaterialDaoImpl;
import DAO.TypeDaoImpl;
import DAO.UnitDaoImpl;
import DTO.Material;
import DTO.Type;
import DTO.Unit;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

public class MaterialEditController {
    @FXML
    private Button backButton;
    @FXML
    private Button doneButton;

    @FXML
    private Label titleLabel;

    @FXML
    private ComboBox<Type> typeComboBox;
    @FXML
    private TextField nameField;
    @FXML
    private TextField manufacturerField;
    @FXML
    private ComboBox<Unit> unitComboBox;
    @FXML
    private TextField priceField;


    private static Material material;
    private MaterialDaoImpl materialDao = new MaterialDaoImpl();
    private TypeDaoImpl typeDao = new TypeDaoImpl();
    private UnitDaoImpl unitDao = new UnitDaoImpl();


    @FXML
    private void initialize() {
        typeComboBox.setItems(FXCollections.observableArrayList(typeDao.getAll()));
        unitComboBox.setItems(FXCollections.observableArrayList(unitDao.getAll()));

        if(material == null) {
            titleLabel.setText("Створення нового товару");
        } else {
            titleLabel.setText("Редагування товару");
            setData();
        }

        backButton.setOnAction(event -> backButton.getScene().getWindow().hide());

        doneButton.setOnAction(event -> {
            if (isInputValid()) {
                if (isTypeValid()) {
                    Material newMaterial = new Material(
                            null,
                            nameField.getText().trim(),
                            manufacturerField.getText().trim(),
                            Double.valueOf(priceField.getText().trim()),
                            0,
                            typeComboBox.getValue().getTypeId(),
                            unitComboBox.getValue().getUnitId()

                    );

                    if (material == null) {
                        Integer id = materialDao.save(newMaterial);
                        newMaterial.setMaterialId(id);

                        Main.getMaterialMenuController().addMaterial(newMaterial);
                    } else {
                        newMaterial.setMaterialId(material.getMaterialId());
                        newMaterial.setAmount(material.getAmount());
                        materialDao.update(newMaterial);

                        Main.getMaterialMenuController().setMaterialTable();
                    }
                    doneButton.getScene().getWindow().hide();
                }
            }
        });
    }

    public static void initializeData(Material material) {
        MaterialEditController.material = material;
    }

    private void setData() {
        typeComboBox.setValue(typeDao.get(material.getTypeId()));
        nameField.setText(material.getName());
        manufacturerField.setText(material.getManufacturer());
        unitComboBox.setValue(unitDao.get(material.getUnitId()));
        priceField.setText(material.getUnitPrice().toString());
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (typeComboBox.getValue() == null) {
            errorMessage += "Не вказано тип!\n";
        }
        if (nameField.getText().trim().length() == 0) {
            errorMessage += "Не вказано назву!\n";
        }
        if (manufacturerField.getText().trim().length() == 0) {
            errorMessage += "Не вказано виробника!\n";
        }
        if (unitComboBox.getValue() == null) {
            errorMessage += "Не вказано одиницю вимірювання!\n";
        }
        if (priceField.getText().trim().length() == 0) {
            errorMessage += "Не вказано ціну!\n";
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

    private boolean isTypeValid() {
        String errorMessage = "";
        String input = priceField.getText().trim();
        String pattern = "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?";

        if (!input.matches(pattern) || Double.parseDouble(priceField.getText().trim()) < 0) {
            errorMessage += "Ціна вказана неправильно!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
            alert.setTitle("Storage Keeper");
            alert.setHeaderText("Будь ласка, заповніть усі поля правильно");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }
    }
}

package Controller;

import DAO.SupplierDaoImpl;
import DTO.Shipper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

public class SupplierEditController {
    @FXML
    private Button backButton;
    @FXML
    private Button doneButton;

    @FXML
    private Label titleLabel;

    @FXML
    private TextField nameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField contactPersonField;


    private static Shipper supplier;
    private final SupplierDaoImpl supplierDao = new SupplierDaoImpl();


    @FXML
    private void initialize() {
        if(supplier == null) {
            titleLabel.setText("Створення нового постачальника");
        } else {
            titleLabel.setText("Редагування постачальника");
            setData();
        }

        backButton.setOnAction(event -> backButton.getScene().getWindow().hide());

        doneButton.setOnAction(event -> {
            if (isInputValid()) {
                Shipper newSupplier = new Shipper(
                        null,
                        nameField.getText().trim(),
                        addressField.getText().trim(),
                        phoneNumberField.getText().trim(),
                        contactPersonField.getText().trim()
                );

                if (supplier == null) {
                    Integer id = supplierDao.save(newSupplier);
                    newSupplier.setShipperId(id);

                    Main.getSupplierMenuController().addSupplier(newSupplier);
                } else {
                    newSupplier.setShipperId(supplier.getShipperId());
                    supplierDao.update(newSupplier);

                    Main.getSupplierMenuController().setSupplierTable();
                }
                doneButton.getScene().getWindow().hide();
            }
        });
    }

    public static void initializeData(Shipper supplier) {
        SupplierEditController.supplier = supplier;
    }

    private void setData() {
        nameField.setText(supplier.getName());
        addressField.setText(supplier.getAddress());
        phoneNumberField.setText(supplier.getPhoneNumber());
        contactPersonField.setText(supplier.getContactPerson());
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText().trim().length() == 0) {
            errorMessage += "Не вказано назву!\n";
        }
        if (addressField.getText().trim().length() == 0) {
            errorMessage += "Не вказано адресу!\n";
        }
        if (phoneNumberField.getText().trim().length() == 0) {
            errorMessage += "Не вказано номер телефону!\n";
        }
        if (contactPersonField.getText().trim().length() == 0) {
            errorMessage += "Не вказано контактну особу!\n";
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

package Controller;

import DAO.ClientDaoImpl;
import DTO.Shipper;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

public class ClientEditController {
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


    private static Shipper client;
    private ClientDaoImpl clientDao = new ClientDaoImpl();


    @FXML
    private void initialize() {
        if(client == null) {
            titleLabel.setText("Створення нового замовника");
        } else {
            titleLabel.setText("Редагування замовника");
            setData();
        }

        backButton.setOnAction(event -> backButton.getScene().getWindow().hide());

        doneButton.setOnAction(event -> {
            if (isInputValid()) {
                Shipper newClient = new Shipper(
                        null,
                        nameField.getText().trim(),
                        addressField.getText().trim(),
                        phoneNumberField.getText().trim(),
                        contactPersonField.getText().trim()
                );

                if (client == null) {
                    Integer id = clientDao.save(newClient);
                    newClient.setShipperId(id);

                    Main.getClientMenuController().addClient(newClient);
                } else {
                    newClient.setShipperId(client.getShipperId());
                    clientDao.update(newClient);

                    Main.getClientMenuController().setClientTable();
                }
                doneButton.getScene().getWindow().hide();
            }
        });
    }

    public static void initializeData(Shipper client) {
        ClientEditController.client = client;
    }

    private void setData() {
        nameField.setText(client.getName());
        addressField.setText(client.getAddress());
        phoneNumberField.setText(client.getPhoneNumber());
        contactPersonField.setText(client.getContactPerson());
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

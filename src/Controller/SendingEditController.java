package Controller;

import DAO.ClientDaoImpl;
import DAO.SendingDaoImpl;
import DTO.Shipment;
import DTO.Shipper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Timestamp;

public class SendingEditController {
    @FXML
    private Button backButton;
    @FXML
    private Button doneButton;

    @FXML
    private Label titleLabel;

    @FXML
    private TextField nameField;
    @FXML
    private DatePicker datePicker;

    @FXML
    private TableView<Shipper> clientTable;
    @FXML
    private TableColumn<Shipper, String> clientNameColumn;
    @FXML
    private TableColumn<Shipper, String> clientAddressColumn;
    @FXML
    private TableColumn<Shipper, String> clientPhoneNumberColumn;
    @FXML
    private TableColumn<Shipper, String> clientContactPersonColumn;


    private static Shipment sending;
    private SendingDaoImpl sendingDao = new SendingDaoImpl();
    private ClientDaoImpl clientDao = new ClientDaoImpl();


    @FXML
    private void initialize() {
        setClientTable();

        if(sending == null) {
            titleLabel.setText("Створення нової відправки");
        } else {
            titleLabel.setText("Редагування відправки");
            setData();
        }

        backButton.setOnAction(event -> backButton.getScene().getWindow().hide());

        doneButton.setOnAction(event -> {
            if (isInputValid()) {
                if (sending == null) {
                    Shipment newSending = new Shipment(
                            null,
                            nameField.getText().trim(),
                            Timestamp.valueOf(datePicker.getValue().atStartOfDay()),
                            "created",
                            clientTable.getSelectionModel().getSelectedItem().getShipperId(),
                            1   //TODO
                    );

                    Integer id = sendingDao.save(newSending);
                    newSending.setShipmentId(id);

                    Main.getSendingMenuController().addSending(newSending);
                } else {
                    Shipment newSending = new Shipment(
                            sending.getShipmentId(),
                            nameField.getText().trim(),
                            Timestamp.valueOf(datePicker.getValue().atStartOfDay()),
                            sending.getStatus(),
                            null,
                            1   //TODO
                    );

                    if (clientTable.getSelectionModel().getSelectedItem() == null) {
                        newSending.setShipperId(sending.getShipperId());
                    } else {
                        newSending.setShipperId(clientTable.getSelectionModel().getSelectedItem().getShipperId());
                    }

                    sendingDao.update(newSending);

                    Main.getSendingMenuController().setSendingTable();
                }
                doneButton.getScene().getWindow().hide();
            }
        });
    }

    public static void initializeData(Shipment sending) {
        SendingEditController.sending = sending;
    }

    private void setData() {
        nameField.setText(sending.getName());
        datePicker.setValue(sending.getDate().toLocalDateTime().toLocalDate());
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText().trim().length() == 0) {
            errorMessage += "Не вказано назву!\n";
        }
        if (datePicker.getValue() == null) {
            errorMessage += "Не вказано дату!\n";
        }
        if (sending == null && clientTable.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Не вказано замовника!\n";
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

    public void setClientTable() {
        clientNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName())
        );
        clientAddressColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getAddress())
        );
        clientPhoneNumberColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPhoneNumber())
        );
        clientContactPersonColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getContactPerson())
        );

        clientTable.setItems(FXCollections.observableArrayList(clientDao.getAll()));
    }
}

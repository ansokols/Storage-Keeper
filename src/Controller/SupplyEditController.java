package Controller;

import DAO.SupplierDaoImpl;
import DAO.SupplyDaoImpl;
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

public class SupplyEditController {
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
    private TableView<Shipper> supplierTable;
    @FXML
    private TableColumn<Shipper, String> supplierNameColumn;
    @FXML
    private TableColumn<Shipper, String> supplierAddressColumn;
    @FXML
    private TableColumn<Shipper, String> supplierPhoneNumberColumn;
    @FXML
    private TableColumn<Shipper, String> supplierContactPersonColumn;


    private static Shipment supply;
    private final SupplyDaoImpl supplyDao = new SupplyDaoImpl();
    private final SupplierDaoImpl supplierDao = new SupplierDaoImpl();


    @FXML
    private void initialize() {
        setSupplierTable();

        if(supply == null) {
            titleLabel.setText("Створення нового постачання");
        } else {
            titleLabel.setText("Редагування постачання");
            setData();
        }

        backButton.setOnAction(event -> backButton.getScene().getWindow().hide());

        doneButton.setOnAction(event -> {
            if (isInputValid()) {
                if (supply == null) {
                    Shipment newSupply = new Shipment(
                            null,
                            nameField.getText().trim(),
                            Timestamp.valueOf(datePicker.getValue().atStartOfDay()),
                            "created",
                            supplierTable.getSelectionModel().getSelectedItem().getShipperId(),
                            Main.getEmployee().getEmployeeId()
                    );

                    Integer id = supplyDao.save(newSupply);
                    newSupply.setShipmentId(id);

                    Main.getSupplyMenuController().addSupply(newSupply);
                } else {
                    Shipment newSupply = new Shipment(
                            supply.getShipmentId(),
                            nameField.getText().trim(),
                            Timestamp.valueOf(datePicker.getValue().atStartOfDay()),
                            supply.getStatus(),
                            null,
                            Main.getEmployee().getEmployeeId()
                    );

                    if (supplierTable.getSelectionModel().getSelectedItem() == null) {
                        newSupply.setShipperId(supply.getShipperId());
                    } else {
                        newSupply.setShipperId(supplierTable.getSelectionModel().getSelectedItem().getShipperId());
                    }

                    supplyDao.update(newSupply);

                    Main.getSupplyMenuController().setSupplyTable();
                }
                doneButton.getScene().getWindow().hide();
            }
        });
    }

    public static void initializeData(Shipment supply) {
        SupplyEditController.supply = supply;
    }

    private void setData() {
        nameField.setText(supply.getName());
        datePicker.setValue(supply.getDate().toLocalDateTime().toLocalDate());
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText().trim().length() == 0) {
            errorMessage += "Не вказано назву!\n";
        }
        if (datePicker.getValue() == null) {
            errorMessage += "Не вказано дату!\n";
        }
        if (supply == null && supplierTable.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Не вказано постачальника!\n";
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

    public void setSupplierTable() {
        supplierNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName())
        );
        supplierAddressColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getAddress())
        );
        supplierPhoneNumberColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPhoneNumber())
        );
        supplierContactPersonColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getContactPerson())
        );

        supplierTable.setItems(FXCollections.observableArrayList(supplierDao.getAll()));
    }
}


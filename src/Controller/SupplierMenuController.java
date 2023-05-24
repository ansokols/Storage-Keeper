package Controller;

import DAO.SupplierDaoImpl;
import DTO.Shipper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;

public class SupplierMenuController {
    @FXML
    private Button backButton;

    @FXML
    private Button storageMapButton;
    @FXML
    private Button shipmentButton;
    @FXML
    private Button materialButton;
    @FXML
    private Button employeeButton;

    @FXML
    private Button newSupplierButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Shipper> supplierTable;
    @FXML
    private TableColumn<Shipper, String> nameColumn;
    @FXML
    private TableColumn<Shipper, String> addressColumn;
    @FXML
    private TableColumn<Shipper, String> phoneNumberColumn;
    @FXML
    private TableColumn<Shipper, String> contactPersonColumn;


    private SupplierDaoImpl supplierDao = new SupplierDaoImpl();


    @FXML
    void initialize() {
        Main.setSupplierMenuController(this);
        setSupplierTable();

        supplierTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    editButton.setDisable(false);
                    deleteButton.setDisable(false);
                });

        backButton.setOnAction(event -> Main.newWindow("/View/SupplyMenu.fxml"));

        storageMapButton.setOnAction(event -> Main.newWindow("/View/StorageMapMenu.fxml"));
        shipmentButton.setOnAction(event -> Main.newWindow("/View/SupplyMenu.fxml"));
        materialButton.setOnAction(event -> Main.newWindow("/View/MaterialMenu.fxml"));
        employeeButton.setOnAction(event -> Main.newWindow("/View/EmployeeMenu.fxml"));

        newSupplierButton.setOnAction(event -> {
            SupplierEditController.initializeData(null);
            Main.newModalWindow("/View/SupplierEdit.fxml");
        });

        editButton.setOnAction(event -> {
            if(supplierTable.getSelectionModel().getSelectedItem() != null) {
                SupplierEditController.initializeData(supplierTable.getSelectionModel().getSelectedItem());
                Main.newModalWindow("/View/SupplierEdit.fxml");
            }
        });

        deleteButton.setOnAction(event -> {
            if(supplierTable.getSelectionModel().getSelectedItem() != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
                alert.setTitle("Storage Keeper");
                alert.setHeaderText("Ви впевнені, що бажаєте видалити постачальника?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get() == ButtonType.OK) {
                    supplierDao.delete(supplierTable.getSelectionModel().getSelectedItem());
                    supplierTable.getItems().remove(supplierTable.getSelectionModel().getSelectedIndex());
                }
            }
        });
    }

    public void setSupplierTable() {
        int selectedIndex = supplierTable.getSelectionModel().getSelectedIndex();

        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName())
        );
        addressColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAddress())
        );
        phoneNumberColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPhoneNumber())
        );
        contactPersonColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getContactPerson())
        );

        supplierTable.setItems(FXCollections.observableArrayList(supplierDao.getAll()));
        supplierTable.getSelectionModel().select(selectedIndex);
    }

    public void addSupplier(Shipper supplier) {
        supplierTable.getItems().add(supplier);
    }
}

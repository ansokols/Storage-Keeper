package Controller;

import DAO.*;
import DTO.Shipment;
import DTO.ShipmentMaterial;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Timestamp;
import java.util.Optional;

public class SendingMenuController {
    @FXML
    private Button backButton;
    @FXML
    private Button clientButton;

    @FXML
    private Button storageMapButton;
    @FXML
    private Button supplyButton;
    @FXML
    private Button sendingButton;
    @FXML
    private Button materialButton;
    @FXML
    private Button employeeButton;

    @FXML
    private Button newSendingButton;
    @FXML
    private Button newMaterialButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Shipment> sendingTable;
    @FXML
    private TableColumn<Shipment, String> sendingNameColumn;
    @FXML
    private TableColumn<Shipment, Timestamp> sendingDateColumn;
    @FXML
    private TableColumn<Shipment, String> sendingStatusColumn;
    @FXML
    private TableColumn<Shipment, String> clientColumn;
    @FXML
    private TableColumn<Shipment, String> employeeColumn;

    @FXML
    private TableView<ShipmentMaterial> materialTable;
    @FXML
    private TableColumn<ShipmentMaterial, String> typeColumn;
    @FXML
    private TableColumn<ShipmentMaterial, String> nameColumn;
    @FXML
    private TableColumn<ShipmentMaterial, String> manufacturerColumn;
    @FXML
    private TableColumn<ShipmentMaterial, Integer> loadedAmountColumn;
    @FXML
    private TableColumn<ShipmentMaterial, Integer> amountColumn;
    @FXML
    private TableColumn<ShipmentMaterial, String> unitColumn;
    @FXML
    private TableColumn<ShipmentMaterial, Double> priceColumn;

    private SendingMaterialDaoImpl sendingMaterialDao = new SendingMaterialDaoImpl();
    private SendingDaoImpl sendingDao = new SendingDaoImpl();
    private ClientDaoImpl clientDao = new ClientDaoImpl();
    private EmployeeDaoImpl employeeDao = new EmployeeDaoImpl();
    private MaterialDaoImpl materialDao = new MaterialDaoImpl();
    private TypeDaoImpl typeDao = new TypeDaoImpl();
    private UnitDaoImpl unitDao = new UnitDaoImpl();


    @FXML
    void initialize() {
        Main.setSendingMenuController(this);
        setSendingTable();

        sendingTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    setMaterialTable();
                    newMaterialButton.setDisable(false);
                    editButton.setDisable(false);
                    deleteButton.setDisable(false);
                });

        sendingTable.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                materialTable.getSelectionModel().clearSelection();
            }
        });

        backButton.setOnAction(event -> Main.newWindow("/View/Authorization.fxml"));
        clientButton.setOnAction(event -> Main.newWindow("/View/ClientMenu.fxml"));

        storageMapButton.setOnAction(event -> Main.newWindow("/View/StorageMapMenu.fxml"));
        supplyButton.setOnAction(event -> Main.newWindow("/View/SupplyMenu.fxml"));
        sendingButton.setOnAction(event -> Main.newWindow("/View/SendingMenu.fxml"));
        materialButton.setOnAction(event -> Main.newWindow("/View/MaterialMenu.fxml"));
        employeeButton.setOnAction(event -> Main.newWindow("/View/EmployeeMenu.fxml"));

        newSendingButton.setOnAction(event -> {
            SendingEditController.initializeData(null);
            Main.newModalWindow("/View/SendingEdit.fxml");
        });

        newMaterialButton.setOnAction(event -> {
            if(sendingTable.getSelectionModel().getSelectedItem() != null) {
                SendingMaterialEditController.initializeData(sendingTable.getSelectionModel().getSelectedItem(), null);
                Main.newModalWindow("/View/SendingMaterialEdit.fxml");
            }
        });

        editButton.setOnAction(event -> {
            if(materialTable.getSelectionModel().getSelectedItem() != null) {
                if (materialTable.getSelectionModel().getSelectedItem().getLoadedAmount() == 0) {
                    SendingMaterialEditController.initializeData(
                            sendingTable.getSelectionModel().getSelectedItem(),
                            materialTable.getSelectionModel().getSelectedItem()
                    );
                    Main.newModalWindow("/View/SendingMaterialEdit.fxml");
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
                    alert.setTitle("Storage Keeper");
                    alert.setHeaderText("Не можна редагувати цей товар у відправці");
                    alert.setContentText("Для цього товару вже розпочато процес укомплектації");
                    alert.showAndWait();
                }
            }
            else if(sendingTable.getSelectionModel().getSelectedItem() != null) {
                SendingEditController.initializeData(sendingTable.getSelectionModel().getSelectedItem());
                Main.newModalWindow("/View/SendingEdit.fxml");
            }
        });

        deleteButton.setOnAction(event -> {
            if(materialTable.getSelectionModel().getSelectedItem() != null) {
                if (materialTable.getSelectionModel().getSelectedItem().getLoadedAmount() == 0) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
                    alert.setTitle("Storage Keeper");
                    alert.setHeaderText("Ви впевнені, що бажаєте видалити товар із відправки?");
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.get() == ButtonType.OK) {
                        sendingMaterialDao.delete(materialTable.getSelectionModel().getSelectedItem());
                        materialTable.getItems().remove(materialTable.getSelectionModel().getSelectedIndex());
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
                    alert.setTitle("Storage Keeper");
                    alert.setHeaderText("Не можна видалити цей товар із відправки");
                    alert.setContentText("Для цього товару вже розпочато процес укомплектації");
                    alert.showAndWait();
                }
            }
            else if(sendingTable.getSelectionModel().getSelectedItem() != null) {
                if (sendingTable.getSelectionModel().getSelectedItem().getStatus().equals("created")) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
                    alert.setTitle("Storage Keeper");
                    alert.setHeaderText("Ви впевнені, що бажаєте видалити відправку?");
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.get() == ButtonType.OK) {
                        sendingDao.delete(sendingTable.getSelectionModel().getSelectedItem());
                        sendingTable.getItems().remove(sendingTable.getSelectionModel().getSelectedIndex());
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
                    alert.setTitle("Storage Keeper");
                    alert.setHeaderText("Не можна видалити цю відправку");
                    alert.setContentText("Для товарів цього постачання вже розпочато процес укомплектації");
                    alert.showAndWait();
                }
            }
        });
    }

    public void setSendingTable() {
        int selectedIndex = sendingTable.getSelectionModel().getSelectedIndex();

        sendingNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName())
        );
        sendingDateColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getDate())
        );
        sendingStatusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus())
        );
        clientColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(clientDao.get(cellData.getValue().getShipperId()).getName())
        );
        employeeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(employeeDao.get(cellData.getValue().getEmployeeId()).getName())
        );

        sendingTable.setItems(FXCollections.observableArrayList(sendingDao.getAll()));
        sendingTable.getSelectionModel().select(selectedIndex);
    }

    public void setMaterialTable() {
        if (sendingTable.getSelectionModel().getSelectedItem() != null) {
            int selectedIndex = materialTable.getSelectionModel().getSelectedIndex();

            typeColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(typeDao.get(materialDao.get(cellData.getValue().getMaterialId()).getTypeId()).getName())
            );
            nameColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(materialDao.get(cellData.getValue().getMaterialId()).getName())
            );
            manufacturerColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(materialDao.get(cellData.getValue().getMaterialId()).getManufacturer())
            );
            loadedAmountColumn.setCellValueFactory(cellData ->
                    new SimpleIntegerProperty(cellData.getValue().getLoadedAmount()).asObject()
            );
            amountColumn.setCellValueFactory(cellData ->
                    new SimpleIntegerProperty(cellData.getValue().getAmount()).asObject()
            );
            unitColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(unitDao.get(materialDao.get(cellData.getValue().getMaterialId()).getUnitId()).getName())
            );
            priceColumn.setCellValueFactory(cellData ->
                    new SimpleDoubleProperty(cellData.getValue().getUnitPrice()).asObject()
            );

            materialTable.setItems(FXCollections.observableArrayList(
                    sendingMaterialDao.getAllByShipment(sendingTable.getSelectionModel().getSelectedItem().getShipmentId())
            ));
            materialTable.getSelectionModel().select(selectedIndex);
        }
    }

    public void addSending(Shipment sending) {
        sendingTable.getItems().add(sending);
    }

    public void addMaterial(ShipmentMaterial sendingMaterial) {
        materialTable.getItems().add(sendingMaterial);
    }
}

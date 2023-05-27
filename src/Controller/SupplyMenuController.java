package Controller;

import DAO.*;
import DTO.Post;
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

public class SupplyMenuController {
    @FXML
    private Button backButton;
    @FXML
    private Button supplierButton;

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
    private Button newSupplyButton;
    @FXML
    private Button newMaterialButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Shipment> supplyTable;
    @FXML
    private TableColumn<Shipment, String> supplyNameColumn;
    @FXML
    private TableColumn<Shipment, Timestamp> supplyDateColumn;
    @FXML
    private TableColumn<Shipment, String> supplyStatusColumn;
    @FXML
    private TableColumn<Shipment, String> supplierColumn;
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


    private final PostDaoImpl postDao = new PostDaoImpl();
    private final SupplyMaterialDaoImpl supplyMaterialDao = new SupplyMaterialDaoImpl();
    private final SupplyDaoImpl supplyDao = new SupplyDaoImpl();
    private final SupplierDaoImpl supplierDao = new SupplierDaoImpl();
    private final EmployeeDaoImpl employeeDao = new EmployeeDaoImpl();
    private final MaterialDaoImpl materialDao = new MaterialDaoImpl();
    private final TypeDaoImpl typeDao = new TypeDaoImpl();
    private final UnitDaoImpl unitDao = new UnitDaoImpl();


    @FXML
    void initialize() {
        Main.setSupplyMenuController(this);
        setSupplyTable();
        setAccess();

        supplyTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    setMaterialTable();
                    newMaterialButton.setDisable(false);
                    editButton.setDisable(false);
                    deleteButton.setDisable(false);
                });

        supplyTable.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                materialTable.getSelectionModel().clearSelection();
            }
        });

        backButton.setOnAction(event -> Main.newWindow("/View/AuthorizationMenu.fxml"));
        supplierButton.setOnAction(event -> Main.newWindow("/View/SupplierMenu.fxml"));

        storageMapButton.setOnAction(event -> Main.newWindow("/View/StorageMapMenu.fxml"));
        supplyButton.setOnAction(event -> Main.newWindow("/View/SupplyMenu.fxml"));
        sendingButton.setOnAction(event -> Main.newWindow("/View/SendingMenu.fxml"));
        materialButton.setOnAction(event -> Main.newWindow("/View/MaterialMenu.fxml"));
        employeeButton.setOnAction(event -> Main.newWindow("/View/EmployeeMenu.fxml"));

        newSupplyButton.setOnAction(event -> {
            SupplyEditController.initializeData(null);
            Main.newModalWindow("/View/SupplyEdit.fxml");
        });

        newMaterialButton.setOnAction(event -> {
            if(supplyTable.getSelectionModel().getSelectedItem() != null) {
                SupplyMaterialEditController.initializeData(supplyTable.getSelectionModel().getSelectedItem(), null);
                Main.newModalWindow("/View/SupplyMaterialEdit.fxml");
            }
        });

        editButton.setOnAction(event -> {
            if(materialTable.getSelectionModel().getSelectedItem() != null) {
                if (materialTable.getSelectionModel().getSelectedItem().getLoadedAmount() == 0) {
                    SupplyMaterialEditController.initializeData(
                            supplyTable.getSelectionModel().getSelectedItem(),
                            materialTable.getSelectionModel().getSelectedItem()
                    );
                    Main.newModalWindow("/View/SupplyMaterialEdit.fxml");
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
                    alert.setTitle("Storage Keeper");
                    alert.setHeaderText("Не можна редагувати цей товар у постачанні");
                    alert.setContentText("Для цього товару вже розпочато процес комплектації");
                    alert.showAndWait();
                }
            }
            else if(supplyTable.getSelectionModel().getSelectedItem() != null) {
                SupplyEditController.initializeData(supplyTable.getSelectionModel().getSelectedItem());
                Main.newModalWindow("/View/SupplyEdit.fxml");
            }
        });

        deleteButton.setOnAction(event -> {
            if(materialTable.getSelectionModel().getSelectedItem() != null) {
                if (materialTable.getSelectionModel().getSelectedItem().getLoadedAmount() == 0) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
                    alert.setTitle("Storage Keeper");
                    alert.setHeaderText("Ви впевнені, що бажаєте видалити товар із постачання?");
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.get() == ButtonType.OK) {
                        supplyMaterialDao.delete(materialTable.getSelectionModel().getSelectedItem());
                        materialTable.getItems().remove(materialTable.getSelectionModel().getSelectedIndex());
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
                    alert.setTitle("Storage Keeper");
                    alert.setHeaderText("Не можна видалити цей товар із постачання");
                    alert.setContentText("Для цього товару вже розпочато процес комплектації");
                    alert.showAndWait();
                }
            }
            else if(supplyTable.getSelectionModel().getSelectedItem() != null) {
                if (supplyTable.getSelectionModel().getSelectedItem().getStatus().equals("created")) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
                    alert.setTitle("Storage Keeper");
                    alert.setHeaderText("Ви впевнені, що бажаєте видалити постачання?");
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.get() == ButtonType.OK) {
                        supplyDao.delete(supplyTable.getSelectionModel().getSelectedItem());
                        supplyTable.getItems().remove(supplyTable.getSelectionModel().getSelectedIndex());
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
                    alert.setTitle("Storage Keeper");
                    alert.setHeaderText("Не можна видалити це постачання");
                    alert.setContentText("Для товарів цього постачання вже розпочато процес комплектації");
                    alert.showAndWait();
                }
            }
        });
    }

    private void setAccess() {
        Post post = postDao.get(Main.getEmployee().getPostId());

        supplierButton.setVisible(post.isShipperAccess());
        storageMapButton.setVisible(post.isStorageMapAccess());
        supplyButton.setVisible(post.isShipmentAccess());
        sendingButton.setVisible(post.isShipmentAccess());
        materialButton.setVisible(post.isMaterialAccess());
        employeeButton.setVisible(post.isEmployeeAccess());

        newSupplyButton.setVisible(post.isShipmentEdit());
        newMaterialButton.setVisible(post.isShipmentEdit());
        editButton.setVisible(post.isShipmentEdit());
        deleteButton.setVisible(post.isShipmentEdit());
    }

    public void setSupplyTable() {
        int selectedIndex = supplyTable.getSelectionModel().getSelectedIndex();

        supplyNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName())
        );
        supplyDateColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getDate())
        );
        supplyStatusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus())
        );
        supplierColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(supplierDao.get(cellData.getValue().getShipperId()).getName())
        );
        employeeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(employeeDao.get(cellData.getValue().getEmployeeId()).getName())
        );

        supplyTable.setItems(FXCollections.observableArrayList(supplyDao.getAll()));
        supplyTable.getSelectionModel().select(selectedIndex);
    }

    public void setMaterialTable() {
        if (supplyTable.getSelectionModel().getSelectedItem() != null) {
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
                    supplyMaterialDao.getAllByShipment(supplyTable.getSelectionModel().getSelectedItem().getShipmentId())
            ));
            materialTable.getSelectionModel().select(selectedIndex);
        }
    }

    public void addSupply(Shipment supply) {
        supplyTable.getItems().add(supply);
    }

    public void addMaterial(ShipmentMaterial supplyMaterial) {
        materialTable.getItems().add(supplyMaterial);
    }
}
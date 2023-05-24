package Controller;

import DAO.MaterialDaoImpl;
import DAO.TypeDaoImpl;
import DAO.UnitDaoImpl;
import DTO.Material;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;

public class MaterialMenuController {
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
    private Button newMaterialButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Material> materialTable;
    @FXML
    private TableColumn<Material, String> typeColumn;
    @FXML
    private TableColumn<Material, String> nameColumn;
    @FXML
    private TableColumn<Material, String> manufacturerColumn;
    @FXML
    private TableColumn<Material, Integer> amountColumn;
    @FXML
    private TableColumn<Material, String> unitColumn;
    @FXML
    private TableColumn<Material, Double> priceColumn;

    private MaterialDaoImpl materialDao = new MaterialDaoImpl();
    private TypeDaoImpl typeDao = new TypeDaoImpl();
    private UnitDaoImpl unitDao = new UnitDaoImpl();


    @FXML
    void initialize() {
        Main.setMaterialMenuController(this);
        setMaterialTable();

        materialTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    editButton.setDisable(false);
                    deleteButton.setDisable(false);
                });

        backButton.setOnAction(event -> Main.newWindow("/View/Authorization.fxml"));

        storageMapButton.setOnAction(event -> Main.newWindow("/View/StorageMapMenu.fxml"));
        shipmentButton.setOnAction(event -> Main.newWindow("/View/SupplyMenu.fxml"));
        materialButton.setOnAction(event -> Main.newWindow("/View/MaterialMenu.fxml"));
        employeeButton.setOnAction(event -> Main.newWindow("/View/EmployeeMenu.fxml"));

        newMaterialButton.setOnAction(event -> {
            MaterialEditController.initializeData(null);
            Main.newModalWindow("/View/MaterialEdit.fxml");
        });

        editButton.setOnAction(event -> {
            if(materialTable.getSelectionModel().getSelectedItem() != null) {
                MaterialEditController.initializeData(materialTable.getSelectionModel().getSelectedItem());
                Main.newModalWindow("/View/MaterialEdit.fxml");
            }
        });

        deleteButton.setOnAction(event -> {
            if(materialTable.getSelectionModel().getSelectedItem() != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
                alert.setTitle("Storage Keeper");
                alert.setHeaderText("Ви впевнені, що бажаєте видалити товар?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get() == ButtonType.OK) {
                    materialDao.delete(materialTable.getSelectionModel().getSelectedItem());
                    materialTable.getItems().remove(materialTable.getSelectionModel().getSelectedIndex());
                }
            }
        });
    }

    public void setMaterialTable() {
        int selectedIndex = materialTable.getSelectionModel().getSelectedIndex();

        typeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(typeDao.get(cellData.getValue().getTypeId()).getName())
        );
        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName())
        );
        manufacturerColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getManufacturer())
        );
        amountColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getAmount()).asObject()
        );
        unitColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(unitDao.get(cellData.getValue().getUnitId()).getName())
        );
        priceColumn.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getUnitPrice()).asObject()
        );

        materialTable.setItems(FXCollections.observableArrayList(materialDao.getAll()));
        materialTable.getSelectionModel().select(selectedIndex);
    }

    public void addMaterial(Material material) {
        materialTable.getItems().add(material);
    }
}

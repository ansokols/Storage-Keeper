package Controller;

import DAO.*;
import DTO.Material;
import DTO.Shipment;
import DTO.ShipmentMaterial;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

public class SupplyMaterialEditController {
    @FXML
    private Button backButton;
    @FXML
    private Button doneButton;

    @FXML
    private Label titleLabel;

    @FXML
    private TextField amountField;

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


    private static Shipment supply;
    private static ShipmentMaterial supplyMaterial;
    private final SupplyMaterialDaoImpl supplyMaterialDao = new SupplyMaterialDaoImpl();
    private final MaterialDaoImpl materialDao = new MaterialDaoImpl();
    private final TypeDaoImpl typeDao = new TypeDaoImpl();
    private final UnitDaoImpl unitDao = new UnitDaoImpl();


    @FXML
    private void initialize() {
        setMaterialTable();

        if(supplyMaterial == null) {
            titleLabel.setText("Додавання нового товару до постачання");
        } else {
            titleLabel.setText("Редагування товару в постачанні");
            setData();
        }

        backButton.setOnAction(event -> backButton.getScene().getWindow().hide());

        doneButton.setOnAction(event -> {
            if (isInputValid()) {
                if (supplyMaterial == null) {
                    ShipmentMaterial newSupplyMaterial = new ShipmentMaterial(
                            null,
                            Integer.parseInt(amountField.getText().trim()),
                            0,
                            materialTable.getSelectionModel().getSelectedItem().getUnitPrice(),
                            supply.getShipmentId(),
                            materialTable.getSelectionModel().getSelectedItem().getMaterialId()
                    );

                    Integer id = supplyMaterialDao.save(newSupplyMaterial);
                    newSupplyMaterial.setShipmentMaterialId(id);

                    Main.getSupplyMenuController().addMaterial(newSupplyMaterial);
                } else {
                    ShipmentMaterial newSupplyMaterial = new ShipmentMaterial(
                            supplyMaterial.getShipmentMaterialId(),
                            Integer.parseInt(amountField.getText().trim()),
                            supplyMaterial.getLoadedAmount(),
                            null,
                            supply.getShipmentId(),
                            null
                    );

                    if (materialTable.getSelectionModel().getSelectedItem() == null) {
                        newSupplyMaterial.setUnitPrice(supplyMaterial.getUnitPrice());
                        newSupplyMaterial.setMaterialId(supplyMaterial.getMaterialId());
                    } else {
                        newSupplyMaterial.setUnitPrice(materialTable.getSelectionModel().getSelectedItem().getUnitPrice());
                        newSupplyMaterial.setMaterialId(materialTable.getSelectionModel().getSelectedItem().getMaterialId());
                    }

                    supplyMaterialDao.update(newSupplyMaterial);

                    Main.getSupplyMenuController().setMaterialTable();
                }
                doneButton.getScene().getWindow().hide();
            }
        });
    }

    public static void initializeData(Shipment supply, ShipmentMaterial supplyMaterial) {
        SupplyMaterialEditController.supply = supply;
        SupplyMaterialEditController.supplyMaterial = supplyMaterial;
    }

    private void setData() {
        amountField.setText(supplyMaterial.getAmount().toString());
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (amountField.getText().trim().length() == 0) {
            errorMessage += "Не вказано кількість!\n";
        }
        if (supplyMaterial == null && materialTable.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Не вказано товар!\n";
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

    public void setMaterialTable() {
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
    }
}

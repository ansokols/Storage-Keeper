package Controller;

import DAO.*;
import Model.*;
import Model.Cell;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class StorageController {
    @FXML
    private Button storeButton;
    @FXML
    private Button switchButton;

    @FXML
    private VBox areasVBox;

    @FXML
    private ComboBox<Shipment> shipmentComboBox;

    @FXML
    private TableView<ShipmentMaterial> materialTable;
    @FXML
    private TableColumn<ShipmentMaterial, String> materialTypeColumn;
    @FXML
    private TableColumn<ShipmentMaterial, String> materialNameColumn;
    @FXML
    private TableColumn<ShipmentMaterial, String> materialManufacturerColumn;
    @FXML
    private TableColumn<ShipmentMaterial, Integer> materialLoadedAmountColumn;
    @FXML
    private TableColumn<ShipmentMaterial, Integer> materialAmountColumn;

    private AnchorPane currentCellPane;
    private String shipmentMode = "supply";
    private static final int numRows = 3;

    private final AreaDaoImpl areaDataAccessor = new AreaDaoImpl();
    private final CellDaoImpl cellDao = new CellDaoImpl();
    private final MaterialDaoImpl materialDao = new MaterialDaoImpl();
    private final SendingDaoImpl sendingDao = new SendingDaoImpl();
    private final SendingMaterialDaoImpl sendingMaterialDao = new SendingMaterialDaoImpl();
    private final SupplyDaoImpl supplyDao = new SupplyDaoImpl();
    private final SupplyMaterialDaoImpl supplyMaterialDao = new SupplyMaterialDaoImpl();
    private final TypeDaoImpl typeDao = new TypeDaoImpl();
    private final UnitDaoImpl unitDao = new UnitDaoImpl();

    @FXML
    void initialize() {
        switchButton.setText("Постачання");
        shipmentComboBox.setItems(FXCollections.observableArrayList(supplyDao.getAll()));
        setAreas();


        storeButton.setOnAction(event -> {
            Cell cell = cellDao.get(Integer.parseInt(currentCellPane.getId()));
            EditController.initializeData(shipmentMode, cell, materialTable.getSelectionModel().getSelectedItem());

            Stage stage = new Stage();
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/View/edit.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setTitle("Storage Keeper");
            stage.getIcons().add(new Image("file:images" + File.separator + "logo.png"));
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setOnHidden(hideEvent -> {
                setAreas();
                materialTable.refresh();
            });
            stage.show();
        });

        switchButton.setOnAction(event -> {
            storeButton.setDisable(true);

            switch (shipmentMode) {
                case "supply":
                    shipmentMode = "sending";
                    switchButton.setText("Відправки");
                    storeButton.setText("\uD83D\uDCE4");
                    shipmentComboBox.setItems(FXCollections.observableArrayList(sendingDao.getAll()));
                    break;

                case "sending":
                    shipmentMode = "supply";
                    switchButton.setText("Постачання");
                    storeButton.setText("\uD83D\uDCE5");
                    shipmentComboBox.setItems(FXCollections.observableArrayList(supplyDao.getAll()));
                    break;
            }
        });


        shipmentComboBox.setOnAction(event -> {
            storeButton.setDisable(true);
            List<ShipmentMaterial> tempMats = null;

            if (shipmentComboBox.getSelectionModel().getSelectedItem() != null) {
                switch (shipmentMode) {
                    case "supply":
                        tempMats = supplyMaterialDao.getAll(shipmentComboBox.getSelectionModel().getSelectedItem().getShipmentId());
                        break;

                    case "sending":
                        tempMats = sendingMaterialDao.getAll(shipmentComboBox.getSelectionModel().getSelectedItem().getShipmentId());
                        break;
                }

                //TODO Переделать
                materialTypeColumn.setCellValueFactory(cellData ->
                        new SimpleStringProperty(typeDao.get(materialDao.get(cellData.getValue().getMaterialId()).getTypeId()).getName())
                );
                materialNameColumn.setCellValueFactory(cellData ->
                        new SimpleStringProperty(materialDao.get(cellData.getValue().getMaterialId()).getName())
                );
                materialManufacturerColumn.setCellValueFactory(cellData ->
                        new SimpleStringProperty(materialDao.get(cellData.getValue().getMaterialId()).getManufacturer())
                );
                materialLoadedAmountColumn.setCellValueFactory(cellData ->
                        new SimpleIntegerProperty(cellData.getValue().getLoadedAmount()).asObject()
                );
                materialAmountColumn.setCellValueFactory(cellData ->
                        new SimpleIntegerProperty(cellData.getValue().getAmount()).asObject()
                );
                materialTable.setItems(FXCollections.observableArrayList(tempMats));
            } else {
                materialTable.getItems().clear();
            }
        });


        materialTable.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> enableStoreButton());
    }

    private void setAreas() {
        areasVBox.getChildren().clear();
        currentCellPane = null;
        storeButton.setDisable(true);

        String cellStyle =
                "-fx-background-radius: 10; -fx-background-color: grey;" +
                        "-fx-border-radius: 2; -fx-border-width: 5; -fx-border-color: #fafafa;" +
                        "-fx-cursor: HAND";
        String pressedCellStyle =
                "-fx-background-radius: 10; -fx-background-color: #aaaaaa;" +
                        "-fx-border-radius: 2; -fx-border-width: 5; -fx-border-color: #8ff7ab;" +
                        "-fx-cursor: HAND";

        for (Area area : areaDataAccessor.getAll()) {
            GridPane gridPane = new GridPane();
            gridPane.setHgap(5);
            gridPane.setVgap(5);
            gridPane.setPadding(new Insets(0, 10, 10, 10));

            int areaId = area.getAreaId();
            //TODO Переделать
            List<Cell> tempCellList = cellDao.getAll().stream().filter(cell ->
                    cell.getAreaId().equals(areaId)).collect(Collectors.toList());

            int j = 0;
            for (int i = 0; i < tempCellList.size(); i++) {
                if (i % numRows == 0) j++;

                Label cellNameLabel = new Label(tempCellList.get(i).getName());
                cellNameLabel.prefHeight(20);
                cellNameLabel.setStyle("-fx-font: 16 arial;");

                Label cellTypeLabel = new Label(typeDao.get(tempCellList.get(i).getTypeId()).getName());
                cellTypeLabel.prefHeight(20);
                cellTypeLabel.setStyle("-fx-font: 16 arial;");

                Label cellMaterialLabel = new Label("");
                cellMaterialLabel.prefHeight(20);
                cellMaterialLabel.setStyle("-fx-font: 16 arial;");

                Label cellManufacturerLabel = new Label("");
                cellManufacturerLabel.prefHeight(20);
                cellManufacturerLabel.setStyle("-fx-font: 16 arial;");

                Label cellCapacityLabel = new Label(tempCellList.get(i).getCapacity().toString());
                cellCapacityLabel.prefHeight(20);
                cellCapacityLabel.setStyle("-fx-font: 16 arial;");

                Material tempMaterial = materialDao.get(tempCellList.get(i).getMaterialId());
                if (tempMaterial != null) {
                    cellMaterialLabel.setText(tempMaterial.getName());
                    cellManufacturerLabel.setText(tempMaterial.getManufacturer());
                    cellCapacityLabel.setText(
                            tempCellList.get(i).getOccupancy().toString()  + " " +
                                    " / " + tempCellList.get(i).getCapacity().toString() + " " +
                                    unitDao.get(tempMaterial.getUnitId()).getName()
                    );
                }

                VBox cellVBox = new VBox(cellNameLabel, cellTypeLabel, cellMaterialLabel, cellManufacturerLabel, cellCapacityLabel);
                cellVBox.setPadding(new Insets(0, 0, 0, 10));

                AnchorPane cellPane = new AnchorPane(cellVBox);
                AnchorPane.setTopAnchor(cellVBox, 0.0);
                AnchorPane.setBottomAnchor(cellVBox, 0.0);

                cellPane.setId(String.valueOf(tempCellList.get(i).getCellId()));
                cellPane.setStyle(cellStyle);
                cellPane.setPrefSize(100, 100);
                cellPane.setOnMouseClicked(e -> {
                    if (currentCellPane != null) {
                        currentCellPane.setStyle(cellStyle);
                    }
                    cellPane.setStyle(pressedCellStyle);
                    currentCellPane = cellPane;
                    enableStoreButton();
                });
                gridPane.add(cellPane, (i % numRows), j);
            }

            Label groupLabel = new Label(area.getName());
            groupLabel.prefHeight(25);
            groupLabel.setPadding(new Insets(5, 10, 5, 10));
            groupLabel.setStyle("-fx-font: 20 arial;");
            AnchorPane areaPane = new AnchorPane(gridPane);
            areaPane.setId(String.valueOf(areaId));
            VBox vBox = new VBox(new AnchorPane(groupLabel), areaPane);
            vBox.setStyle("-fx-border-width: 2; -fx-border-radius: 10; -fx-border-color: grey;");
            areasVBox.getChildren().add(new AnchorPane(vBox));
        }
    }

    private void enableStoreButton() {
        if(currentCellPane != null && materialTable.getSelectionModel().getSelectedItem() != null) {
            Cell cell = cellDao.get(Integer.parseInt(currentCellPane.getId()));
            storeButton.setDisable(!checkMatching(cell, materialTable.getSelectionModel().getSelectedItem()));
        }
    }

    /*public*/ private boolean checkMatching(Cell cell, ShipmentMaterial shipmentMaterial) {
        Material material = materialDao.get(shipmentMaterial.getMaterialId());

        switch (shipmentMode) {
            default:
                return false;

            case "supply":
                if (shipmentMaterial.getLoadedAmount() >= shipmentMaterial.getAmount()) {
                    return false;
                }
                if (cell.getCapacity() - cell.getOccupancy() <= 0) {
                    return false;
                }
                if (!cell.getTypeId().equals(material.getTypeId())) {
                    return false;
                }
                if (cell.getMaterialId() != 0) {
                    if (!cell.getMaterialId().equals(material.getMaterialId())) {
                        return false;
                    }
                }
                return true;

            case "sending":
                if (shipmentMaterial.getLoadedAmount() >= shipmentMaterial.getAmount()) {
                    return false;
                }
                if (cell.getOccupancy() <= 0) {
                    return false;
                }
                if (!cell.getMaterialId().equals(material.getMaterialId())) {
                    return false;
                }
                return true;
        }
    }

    public void setShipmentMode(String shipmentMode) {
        this.shipmentMode = shipmentMode;
    }
}

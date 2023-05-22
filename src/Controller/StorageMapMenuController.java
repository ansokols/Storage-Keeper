package Controller;

import DAO.*;
import DTO.*;
import DTO.Cell;

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

public class StorageMapMenuController {
    @FXML
    private Button backButton;
    @FXML
    private Button storageButton;

    @FXML
    private Button storageMapButton;
    @FXML
    private Button shipmentButton;
    @FXML
    private Button materialButton;
    @FXML
    private Button employeeButton;

    @FXML
    private Button supplyButton;
    @FXML
    private Button sendingButton;
    @FXML
    private Button storeButton;

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
    private final int numRows = 3;

    private final AreaDaoImpl areaDao = new AreaDaoImpl();
    private final CellDaoImpl cellDao = new CellDaoImpl();
    private final CellTypeDaoImp cellTypeDao = new CellTypeDaoImp();
    private final MaterialDaoImpl materialDao = new MaterialDaoImpl();
    private final SendingDaoImpl sendingDao = new SendingDaoImpl();
    private final SendingMaterialDaoImpl sendingMaterialDao = new SendingMaterialDaoImpl();
    private final SupplyDaoImpl supplyDao = new SupplyDaoImpl();
    private final SupplyMaterialDaoImpl supplyMaterialDao = new SupplyMaterialDaoImpl();
    private final TypeDaoImpl typeDao = new TypeDaoImpl();
    private final UnitDaoImpl unitDao = new UnitDaoImpl();

    @FXML
    void initialize() {
        setAreas();

        materialTable.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> enableStoreButton());

        backButton.setOnAction(event -> Main.newWindow("/View/Authorization.fxml"));
        storageButton.setOnAction(event -> Main.newWindow("/View/StorageMenu.fxml"));

        storageMapButton.setOnAction(event -> Main.newWindow("/View/StorageMapMenu.fxml"));
        shipmentButton.setOnAction(event -> Main.newWindow("/View/SupplyMenu.fxml"));
        materialButton.setOnAction(event -> Main.newWindow("/View/MaterialMenu.fxml"));
        employeeButton.setOnAction(event -> Main.newWindow("/View/EmployeeMenu.fxml"));

        supplyButton.setOnAction(event -> {
            supplyButton.setDisable(true);
            supplyButton.setDefaultButton(true);
            sendingButton.setDisable(false);
            sendingButton.setDefaultButton(false);
            storeButton.setDisable(true);

            shipmentMode = "supply";
            storeButton.setText("\uD83D\uDCE5");
            shipmentComboBox.setItems(FXCollections.observableArrayList(supplyDao.getAll()));
        });

        sendingButton.setOnAction(event -> {
            supplyButton.setDisable(false);
            supplyButton.setDefaultButton(false);
            sendingButton.setDisable(true);
            sendingButton.setDefaultButton(true);
            storeButton.setDisable(true);

            shipmentMode = "sending";
            storeButton.setText("\uD83D\uDCE4");
            shipmentComboBox.setItems(FXCollections.observableArrayList(sendingDao.getAll()));
        });

        storeButton.setOnAction(event -> {
            Cell cell = cellDao.get(Integer.parseInt(currentCellPane.getId()));
            StorageMapEditController.initializeData(shipmentMode, cell, materialTable.getSelectionModel().getSelectedItem());

            Stage stage = new Stage();
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/View/StorageMapEdit.fxml"));
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

        shipmentComboBox.setOnAction(event -> {
            storeButton.setDisable(true);
            List<ShipmentMaterial> tempMats = null;

            if (shipmentComboBox.getSelectionModel().getSelectedItem() != null) {
                switch (shipmentMode) {
                    case "supply" ->
                            tempMats = supplyMaterialDao.getAllByShipment(shipmentComboBox.getSelectionModel().getSelectedItem().getShipmentId());
                    case "sending" ->
                            tempMats = sendingMaterialDao.getAllByShipment(shipmentComboBox.getSelectionModel().getSelectedItem().getShipmentId());
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
    }

    private void setAreas() {
        areasVBox.getChildren().clear();
        currentCellPane = null;
        storeButton.setDisable(true);

        String emptyCellStyle =
                "-fx-background-radius: 10; -fx-background-color: #e6e6e6;" +
                        "-fx-border-radius: 2; -fx-border-width: 5; -fx-border-color: #f4f4f4;" +
                        "-fx-cursor: HAND";
        String emptyPressedCellStyle =
                "-fx-background-radius: 10; -fx-background-color: #e6e6e6;" +
                        "-fx-border-radius: 2; -fx-border-width: 5; -fx-border-color: #8ff7ab;" +
                        "-fx-cursor: HAND";
        String occupiedCellStyle =
                "-fx-background-radius: 10; -fx-background-color: grey;" +
                        "-fx-border-radius: 2; -fx-border-width: 5; -fx-border-color: #f4f4f4;" +
                        "-fx-cursor: HAND";
        String occupiedPressedCellStyle =
                "-fx-background-radius: 10; -fx-background-color: grey;" +
                        "-fx-border-radius: 2; -fx-border-width: 5; -fx-border-color: #8ff7ab;" +
                        "-fx-cursor: HAND";

        for (Area area : areaDao.getAll()) {
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

                Label cellTypeLabel = new Label();
                cellTypeLabel.prefHeight(20);
                cellTypeLabel.setStyle("-fx-font: 16 arial;");

                Label cellMaterialLabel = new Label();
                cellMaterialLabel.prefHeight(20);
                cellMaterialLabel.setStyle("-fx-font: 16 arial;");

                Label cellManufacturerLabel = new Label();
                cellManufacturerLabel.prefHeight(20);
                cellManufacturerLabel.setStyle("-fx-font: 16 arial;");

                Label cellCapacityLabel = new Label();
                cellCapacityLabel.prefHeight(20);
                cellCapacityLabel.setStyle("-fx-font: 16 arial;");

                VBox cellVBox = new VBox();
                cellVBox.setPadding(new Insets(0, 0, 0, 10));
                cellVBox.getChildren().add(cellNameLabel);

                Material tempMaterial = materialDao.get(tempCellList.get(i).getMaterialId());

                if (tempMaterial != null) {
                    cellTypeLabel.setText(typeDao.get(tempMaterial.getTypeId()).getName());
                    cellVBox.getChildren().add(cellTypeLabel);

                    cellMaterialLabel.setText(tempMaterial.getName());
                    cellVBox.getChildren().add(cellMaterialLabel);

                    cellManufacturerLabel.setText(tempMaterial.getManufacturer());
                    cellVBox.getChildren().add(cellManufacturerLabel);

                    cellCapacityLabel.setText(
                            tempCellList.get(i).getOccupancy().toString()  + " " +
                                    " / " + cellTypeDao.get(tempCellList.get(i).getCellId(), tempMaterial.getTypeId()).getCapacity() + " " +
                                    unitDao.get(tempMaterial.getUnitId()).getName()
                    );
                    cellVBox.getChildren().add(cellCapacityLabel);
                } else {
                    List<CellType> cellTypeList = cellTypeDao.getAllByCell(tempCellList.get(i).getCellId());

                    for (CellType cellType : cellTypeList) {
                        String cellTypeInfo = typeDao.get(cellType.getTypeId()).getName() +
                                "   [" + cellType.getCapacity() + "]";
                        Label newLabel = new Label(cellTypeInfo);
                        newLabel.prefHeight(20);
                        newLabel.setStyle("-fx-font: 16 arial;");
                        cellVBox.getChildren().add(newLabel);
                    }
                }

                AnchorPane cellPane = new AnchorPane(cellVBox);
                AnchorPane.setTopAnchor(cellVBox, 0.0);
                AnchorPane.setBottomAnchor(cellVBox, 0.0);

                cellPane.setId(String.valueOf(tempCellList.get(i).getCellId()));
                if (tempMaterial != null) {
                    cellPane.setStyle(occupiedCellStyle);
                } else {
                    cellPane.setStyle(emptyCellStyle);
                }
                cellPane.setPrefSize(100, 100);
                cellPane.setOnMouseClicked(e -> {
                    if (currentCellPane != null) {
                        if (cellDao.get(Integer.parseInt(currentCellPane.getId())).getMaterialId() != 0) {
                            currentCellPane.setStyle(occupiedCellStyle);
                        } else {
                            currentCellPane.setStyle(emptyCellStyle);
                        }
                    }
                    if (cellDao.get(Integer.parseInt(cellPane.getId())).getMaterialId() != 0) {
                        cellPane.setStyle(occupiedPressedCellStyle);
                    } else {
                        cellPane.setStyle(emptyPressedCellStyle);
                    }
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

                if (cell.getMaterialId() != 0) {
                    if (!cell.getMaterialId().equals(material.getMaterialId())) {
                        return false;
                    }
                    if (cellTypeDao.get(cell.getCellId(), material.getTypeId()).getCapacity() - cell.getOccupancy() <= 0) {
                        return false;
                    }
                }

                boolean result = false;
                List<CellType> cellTypeList = cellTypeDao.getAllByCell(cell.getCellId());
                for (CellType cellType : cellTypeList) {
                    if (cellType.getTypeId().equals(material.getTypeId())) {
                        result = true;
                    }
                }

                return result;

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

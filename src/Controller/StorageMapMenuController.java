package Controller;

import DAO.*;
import DTO.*;
import DTO.Cell;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.util.*;

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
    private Button filterButton;
    @FXML
    private Button autoButton;
    @FXML
    private Button storeButton;

    @FXML
    private Label filterLabel;

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
    private boolean isFilterOn = false;
    private final int numRows = 3;

    private final PostDaoImpl postDao = new PostDaoImpl();
    private final AreaDaoImpl areaDao = new AreaDaoImpl();
    private final CellDaoImpl cellDao = new CellDaoImpl();
    private final CellTypeDaoImpl cellTypeDao = new CellTypeDaoImpl();
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
        setAccess();

        shipmentComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        storeButton.setDisable(true);
                        autoButton.setDisable(false);
                        setMaterialTable();
                    }
                });

        materialTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    if (isFilterOn) {
                        setAreas();
                    } else {
                        enableStoreButton();
                    }
                }

            });

        backButton.setOnAction(event -> Main.newWindow("/View/AuthorizationMenu.fxml"));
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
            autoButton.setDisable(true);

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
            autoButton.setDisable(true);

            shipmentMode = "sending";
            storeButton.setText("\uD83D\uDCE4");
            shipmentComboBox.setItems(FXCollections.observableArrayList(sendingDao.getAll()));
        });

        filterButton.setOnAction(event -> {
            if (isFilterOn) {
                isFilterOn = false;
                filterLabel.setText("Викл");
                filterLabel.setTextFill(Color.RED);
                setAreas();
            } else {
                isFilterOn = true;
                filterLabel.setText("Вкл");
                filterLabel.setTextFill(Color.GREEN);
                setAreas();
            }
        });

        storeButton.setOnAction(event -> {
            Cell cell = cellDao.get(Integer.parseInt(currentCellPane.getId()));
            StorageMapEditController.initializeData(shipmentMode, cell, materialTable.getSelectionModel().getSelectedItem());
            Main.newModalWindow("/View/StorageMapEdit.fxml");
        });

        autoButton.setOnAction(event -> {
            autoStore();
        });
    }

    private void setAccess() {
        Post post = postDao.get(Main.getEmployee().getPostId());

        storageButton.setVisible(post.isStorageAccess());
        storageMapButton.setVisible(post.isStorageMapAccess());
        shipmentButton.setVisible(post.isShipmentAccess());
        materialButton.setVisible(post.isMaterialAccess());
        employeeButton.setVisible(post.isEmployeeAccess());

        storeButton.setVisible(post.isStorageMapEdit());
    }

    private void setAreas() {
        areasVBox.getChildren().clear();
        currentCellPane = null;
        storeButton.setDisable(true);

        if (isFilterOn) {
            if (materialTable.getSelectionModel().getSelectedItem() == null) {
                return;
            }
        }

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

            List<Cell> tempCellList = cellDao.getAllByArea(area.getAreaId());
            int cellIndex = 0;
            int columnIndex = 0;
            for (int i = 0; i < tempCellList.size(); i++) {
                if (isFilterOn) {
                    if (!checkMatching(tempCellList.get(i), materialTable.getSelectionModel().getSelectedItem())) {
                        continue;
                    }
                }

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

                if (cellIndex % numRows == 0) columnIndex++;
                gridPane.add(cellPane, (cellIndex % numRows), columnIndex);
                cellIndex++;
            }

            if (!gridPane.getChildren().isEmpty()) {
                Label groupLabel = new Label(area.getName());
                groupLabel.prefHeight(25);
                groupLabel.setPadding(new Insets(5, 10, 5, 10));
                groupLabel.setStyle("-fx-font: 20 arial;");
                AnchorPane areaPane = new AnchorPane(gridPane);
                areaPane.setId(String.valueOf(area.getAreaId()));
                VBox vBox = new VBox(new AnchorPane(groupLabel), areaPane);
                vBox.setStyle("-fx-border-width: 2; -fx-border-radius: 10; -fx-border-color: grey;");
                areasVBox.getChildren().add(new AnchorPane(vBox));
            }
        }
    }

    private void setMaterialTable() {
        List<ShipmentMaterial> tempMats = null;

        if (shipmentComboBox.getSelectionModel().getSelectedItem() != null) {
            switch (shipmentMode) {
                case "supply" ->
                        tempMats = supplyMaterialDao.getAllByShipment(shipmentComboBox.getSelectionModel().getSelectedItem().getShipmentId());
                case "sending" ->
                        tempMats = sendingMaterialDao.getAllByShipment(shipmentComboBox.getSelectionModel().getSelectedItem().getShipmentId());
            }

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
    }

    private void enableStoreButton() {
        if(currentCellPane != null && materialTable.getSelectionModel().getSelectedItem() != null) {
            Cell cell = cellDao.get(Integer.parseInt(currentCellPane.getId()));
            storeButton.setDisable(!checkMatching(cell, materialTable.getSelectionModel().getSelectedItem()));
        }
    }

    private boolean checkMatching(Cell cell, ShipmentMaterial shipmentMaterial) {
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

    private void autoStore() {
        if (shipmentComboBox.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        String info = null;
        String successText = "";
        String errorText = "";
        switch (shipmentMode) {
            case "supply" -> {
                info = fromSupplyToStorage();
                successText = "Товари успішно розміщені на складі";
                errorText = "На складі недостатньо вільного місця";
            }
            case "sending" -> {
                info = fromStorageToSending();
                successText = "Товари успішно укомплектовані у відправку";
                errorText = "На складі недостатньо необхідних товарів";
            }
        }

        if (info != null) {
            InfoMenuController.initializeData(successText, shipmentComboBox.getSelectionModel().getSelectedItem().getName(), info);
            Main.newModalWindow("/View/InfoMenu.fxml");
            setMaterialTable();
            setAreas();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
            alert.setTitle("Storage Keeper");
            alert.setHeaderText(errorText);
            alert.showAndWait();
        }
    }

    private String fromSupplyToStorage() {
        StringBuilder info = new StringBuilder();

        Shipment shipment = shipmentComboBox.getSelectionModel().getSelectedItem();
        List<ShipmentMaterial> shipmentMaterialList = supplyMaterialDao.getAllByShipment(shipment.getShipmentId());
        List<Cell> cellList = cellDao.getAll();
        List<ShipmentMaterial> updatedShipmentMaterialList = new ArrayList<>();
        List<Cell> updatedCellList = new ArrayList<>();

        info.append("\t\t\tПостачання №")
                .append(shipment.getShipmentId())
                .append(" \"")
                .append(shipment.getName())
                .append("\"");

        for (ShipmentMaterial shipmentMaterial : shipmentMaterialList) {
            boolean isEnoughSpace = false;
            int shipmentMaterialUnloadedAmount = shipmentMaterial.getAmount() - shipmentMaterial.getLoadedAmount();

            if (shipmentMaterialUnloadedAmount == 0) {
                continue;
            }

            for (Cell cell : cellList) {
                if (checkMatching(cell, shipmentMaterial)) {
                    cell.setMaterialId(shipmentMaterial.getMaterialId());

                    int typeId = materialDao.get(shipmentMaterial.getMaterialId()).getTypeId();
                    int cellCapacity = cellTypeDao.get(cell.getCellId(), typeId).getCapacity();
                    int cellFreeSpace = cellCapacity - cell.getOccupancy();
                    shipmentMaterialUnloadedAmount = shipmentMaterial.getAmount() - shipmentMaterial.getLoadedAmount();

                    if (shipmentMaterialUnloadedAmount <= cellFreeSpace) {
                        info.append("\r\n")
                                .append(materialDao.get(shipmentMaterial.getMaterialId()).getName())
                                .append(" [")
                                .append(shipmentMaterialUnloadedAmount)
                                .append(" ")
                                .append(unitDao.get(materialDao.get(shipmentMaterial.getMaterialId()).getUnitId()).getName())
                                .append("]")
                                .append("\t――>\t")
                                .append("Ділянка \"")
                                .append(areaDao.get(cell.getAreaId()).getName())
                                .append("\", комірка \"")
                                .append(cell.getName())
                                .append("\"");

                        shipmentMaterial.setLoadedAmount(shipmentMaterial.getAmount());
                        cell.setOccupancy(cell.getOccupancy() + shipmentMaterialUnloadedAmount);
                        isEnoughSpace = true;
                        updatedShipmentMaterialList.add(shipmentMaterial);
                        updatedCellList.add(cell);
                        break;
                    } else {
                        info.append("\r\n")
                                .append(materialDao.get(shipmentMaterial.getMaterialId()).getName())
                                .append(" [")
                                .append(cellCapacity - cell.getOccupancy())
                                .append(" ")
                                .append(unitDao.get(materialDao.get(shipmentMaterial.getMaterialId()).getUnitId()).getName())
                                .append("]")
                                .append("\t――>\t")
                                .append("Ділянка \"")
                                .append(areaDao.get(cell.getAreaId()).getName())
                                .append("\", комірка \"")
                                .append(cell.getName())
                                .append("\"");

                        shipmentMaterial.setLoadedAmount(shipmentMaterial.getLoadedAmount() + cellFreeSpace);
                        cell.setOccupancy(cellCapacity);
                        updatedCellList.add(cell);
                    }
                }
            }

            if (!isEnoughSpace) {
                return null;
            }
        }

        shipment.setStatus("loaded");
        supplyDao.update(shipment);
        for (ShipmentMaterial shipmentMaterial : updatedShipmentMaterialList) {
            supplyMaterialDao.update(shipmentMaterial);
        }
        for (Cell cell : updatedCellList) {
            cellDao.update(cell);
        }

        return info.toString();
    }

    private String fromStorageToSending() {
        StringBuilder info = new StringBuilder();

        Shipment shipment = shipmentComboBox.getSelectionModel().getSelectedItem();
        List<ShipmentMaterial> shipmentMaterialList = sendingMaterialDao.getAllByShipment(shipment.getShipmentId());
        List<Cell> cellList = cellDao.getAll();
        List<ShipmentMaterial> updatedShipmentMaterialList = new ArrayList<>();
        List<Cell> updatedCellList = new ArrayList<>();

        info.append("\t\t\tВідправка №")
                .append(shipment.getShipmentId())
                .append(" \"")
                .append(shipment.getName())
                .append("\"");

        for (ShipmentMaterial shipmentMaterial : shipmentMaterialList) {
            boolean isEnoughMaterial = false;
            int shipmentMaterialUnloadedAmount = shipmentMaterial.getAmount() - shipmentMaterial.getLoadedAmount();

            if (shipmentMaterialUnloadedAmount == 0) {
                continue;
            }

            for (Cell cell : cellList) {
                if (checkMatching(cell, shipmentMaterial)) {
                    shipmentMaterialUnloadedAmount = shipmentMaterial.getAmount() - shipmentMaterial.getLoadedAmount();

                    if (shipmentMaterialUnloadedAmount <= cell.getOccupancy()) {
                        info.append("\r\nДілянка \"")
                                .append(areaDao.get(cell.getAreaId()).getName())
                                .append("\", комірка \"")
                                .append(cell.getName())
                                .append("\"\t――>\t")
                                .append(materialDao.get(shipmentMaterial.getMaterialId()).getName())
                                .append(" [")
                                .append(shipmentMaterialUnloadedAmount)
                                .append(" ")
                                .append(unitDao.get(materialDao.get(shipmentMaterial.getMaterialId()).getUnitId()).getName())
                                .append("]");

                        shipmentMaterial.setLoadedAmount(shipmentMaterial.getAmount());
                        cell.setOccupancy(cell.getOccupancy() - shipmentMaterialUnloadedAmount);
                        isEnoughMaterial = true;
                        updatedShipmentMaterialList.add(shipmentMaterial);
                        updatedCellList.add(cell);
                        break;
                    } else {
                        info.append("\r\nДілянка \"")
                                .append(areaDao.get(cell.getAreaId()).getName())
                                .append("\", комірка \"")
                                .append(cell.getName())
                                .append("\"\t――>\t")
                                .append(materialDao.get(shipmentMaterial.getMaterialId()).getName())
                                .append(" [")
                                .append(cell.getOccupancy())
                                .append(" ")
                                .append(unitDao.get(materialDao.get(shipmentMaterial.getMaterialId()).getUnitId()).getName())
                                .append("]");

                        shipmentMaterial.setLoadedAmount(shipmentMaterial.getLoadedAmount() + cell.getOccupancy());
                        cell.setMaterialId(0);
                        cell.setOccupancy(0);
                        updatedCellList.add(cell);
                    }
                }
            }

            if (!isEnoughMaterial) {
                return null;
            }
        }

        shipment.setStatus("loaded");
        sendingDao.update(shipment);
        for (ShipmentMaterial shipmentMaterial : updatedShipmentMaterialList) {
            sendingMaterialDao.update(shipmentMaterial);
        }
        for (Cell cell : updatedCellList) {
            cellDao.update(cell);
        }

        return info.toString();
    }
}

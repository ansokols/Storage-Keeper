package Controller;

import DAO.AreaDaoImpl;
import DAO.CellDaoImpl;
import DAO.CellTypeDaoImpl;
import DAO.TypeDaoImpl;
import DTO.Area;
import DTO.Cell;
import DTO.CellType;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;

public class StorageMenuController {
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
    private Button newAreaButton;
    @FXML
    private Button newCellButton;
    @FXML
    private Button newTypeButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Area> areaTable;
    @FXML
    private TableColumn<Area, String> areaNameColumn;

    @FXML
    private TableView<DTO.Cell> cellTable;
    @FXML
    private TableColumn<DTO.Cell, String> cellNameColumn;

    @FXML
    private TableView<CellType> typeTable;
    @FXML
    private TableColumn<CellType, String> typeNameColumn;
    @FXML
    private TableColumn<CellType, Integer> typeCapacityColumn;


    private AreaDaoImpl areaDao = new AreaDaoImpl();
    private CellDaoImpl cellDao = new CellDaoImpl();
    private CellTypeDaoImpl cellTypeDao = new CellTypeDaoImpl();
    private TypeDaoImpl typeDao = new TypeDaoImpl();


    @FXML
    void initialize() {
        Main.setStorageMenuController(this);
        setAreaTable();

        areaTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if(newValue != null) {
                        setCellTable();
                        newCellButton.setDisable(false);
                        newTypeButton.setDisable(true);
                        editButton.setDisable(false);
                        deleteButton.setDisable(false);
                    }
                });

        areaTable.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                cellTable.getSelectionModel().clearSelection();
                typeTable.getSelectionModel().clearSelection();
                typeTable.setItems(null);
                newTypeButton.setDisable(true);
            }
        });

        cellTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if(newValue != null) {
                        setTypeTable();
                        newTypeButton.setDisable(false);
                    }
                });

        cellTable.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                typeTable.getSelectionModel().clearSelection();
            }
        });

        backButton.setOnAction(event -> Main.newWindow("/View/StorageMapMenu.fxml"));

        storageMapButton.setOnAction(event -> Main.newWindow("/View/StorageMapMenu.fxml"));
        shipmentButton.setOnAction(event -> Main.newWindow("/View/SupplyMenu.fxml"));
        materialButton.setOnAction(event -> Main.newWindow("/View/MaterialMenu.fxml"));
        employeeButton.setOnAction(event -> Main.newWindow("/View/EmployeeMenu.fxml"));

        newAreaButton.setOnAction(event -> {
            StorageAreaEditController.initializeData(null);
            Main.newModalWindow("/View/StorageAreaEdit.fxml");
        });

        newCellButton.setOnAction(event -> {
            if(areaTable.getSelectionModel().getSelectedItem() != null) {
                StorageCellEditController.initializeData(areaTable.getSelectionModel().getSelectedItem(), null);
                Main.newModalWindow("/View/StorageCellEdit.fxml");
            }
        });

        newTypeButton.setOnAction(event -> {
            if(cellTable.getSelectionModel().getSelectedItem() != null) {
                StorageTypeEditController.initializeData(cellTable.getSelectionModel().getSelectedItem(), null);
                Main.newModalWindow("/View/StorageTypeEdit.fxml");
            }
        });

        editButton.setOnAction(event -> {
            if(typeTable.getSelectionModel().getSelectedItem() != null) {
                if (cellTable.getSelectionModel().getSelectedItem().getOccupancy() == 0) {
                    StorageTypeEditController.initializeData(
                            cellTable.getSelectionModel().getSelectedItem(),
                            typeTable.getSelectionModel().getSelectedItem()
                    );
                    Main.newModalWindow("/View/StorageTypeEdit.fxml");
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
                    alert.setTitle("Storage Keeper");
                    alert.setHeaderText("Не можна редагувати цей тип товару комірки");
                    alert.setContentText("У крмірці вже розміщенно товар");
                    alert.showAndWait();
                }
            }
            else if(cellTable.getSelectionModel().getSelectedItem() != null) {
                StorageCellEditController.initializeData(
                            areaTable.getSelectionModel().getSelectedItem(),
                            cellTable.getSelectionModel().getSelectedItem()
                );
                Main.newModalWindow("/View/StorageCellEdit.fxml");
            }
            else if(areaTable.getSelectionModel().getSelectedItem() != null) {
                StorageAreaEditController.initializeData(areaTable.getSelectionModel().getSelectedItem());
                Main.newModalWindow("/View/StorageAreaEdit.fxml");
            }
        });

        deleteButton.setOnAction(event -> {
            if(typeTable.getSelectionModel().getSelectedItem() != null) {
                if (cellTable.getSelectionModel().getSelectedItem().getOccupancy() == 0) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
                    alert.setTitle("Storage Keeper");
                    alert.setHeaderText("Ви впевнені, що бажаєте прибрати тип товару із комірки?");
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.get() == ButtonType.OK) {
                        cellTypeDao.delete(typeTable.getSelectionModel().getSelectedItem());
                        typeTable.getItems().remove(typeTable.getSelectionModel().getSelectedIndex());
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
                    alert.setTitle("Storage Keeper");
                    alert.setHeaderText("Не можна прибрати цей тип товару із комірки");
                    alert.setContentText("У комірці вже розміщенно товар");
                    alert.showAndWait();
                }
            }
            else if(cellTable.getSelectionModel().getSelectedItem() != null) {
                if (cellTable.getSelectionModel().getSelectedItem().getOccupancy() == 0) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
                    alert.setTitle("Storage Keeper");
                    alert.setHeaderText("Ви впевнені, що бажаєте видалити комірку?");
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.get() == ButtonType.OK) {
                        cellDao.delete(cellTable.getSelectionModel().getSelectedItem());
                        cellTable.getItems().remove(cellTable.getSelectionModel().getSelectedIndex());
                        typeTable.setItems(null);
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
                    alert.setTitle("Storage Keeper");
                    alert.setHeaderText("Не можна видалити цю комірку");
                    alert.setContentText("У комірці вже розміщенно товар");
                    alert.showAndWait();
                }
            }
            else if(areaTable.getSelectionModel().getSelectedItem() != null) {
                if (cellDao.getAllByArea(areaTable.getSelectionModel().getSelectedItem().getAreaId())  == null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
                    alert.setTitle("Storage Keeper");
                    alert.setHeaderText("Ви впевнені, що бажаєте видалити ділянку?");
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.get() == ButtonType.OK) {
                        areaDao.delete(areaTable.getSelectionModel().getSelectedItem());
                        areaTable.getItems().remove(areaTable.getSelectionModel().getSelectedIndex());
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
                    alert.setTitle("Storage Keeper");
                    alert.setHeaderText("Не можна видалити цю ділянку");
                    alert.setContentText("Будь ласка, спочатку видаліть з цієї ділянки усі комірки");
                    alert.showAndWait();
                }
            }
        });
    }

    public void setAreaTable() {
        int selectedIndex = areaTable.getSelectionModel().getSelectedIndex();

        areaNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName())
        );

        areaTable.setItems(FXCollections.observableArrayList(areaDao.getAll()));
        areaTable.getSelectionModel().select(selectedIndex);
    }

    public void setCellTable() {
        int selectedIndex = cellTable.getSelectionModel().getSelectedIndex();

        cellNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName())
        );

        cellTable.setItems(FXCollections.observableArrayList(
                cellDao.getAllByArea(areaTable.getSelectionModel().getSelectedItem().getAreaId())
        ));
        cellTable.getSelectionModel().select(selectedIndex);
    }

    public void setTypeTable() {
        int selectedIndex = typeTable.getSelectionModel().getSelectedIndex();

        typeNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(typeDao.get(cellData.getValue().getTypeId()).getName())
        );
        typeCapacityColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getCapacity()).asObject()
        );

        typeTable.setItems(FXCollections.observableArrayList(
                cellTypeDao.getAllByCell(cellTable.getSelectionModel().getSelectedItem().getCellId())
        ));
        typeTable.getSelectionModel().select(selectedIndex);
    }

    public void addArea(Area area) {
        areaTable.getItems().add(area);
    }
    public void addCell(Cell cell) {
        cellTable.getItems().add(cell);
    }
    public void addType(CellType cellType) {
        typeTable.getItems().add(cellType);
    }
}

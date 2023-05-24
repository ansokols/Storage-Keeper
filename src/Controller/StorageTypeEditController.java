package Controller;

import DAO.CellTypeDaoImpl;
import DAO.TypeDaoImpl;
import DTO.Cell;
import DTO.CellType;
import DTO.Type;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

public class StorageTypeEditController {
    @FXML
    private Button backButton;
    @FXML
    private Button doneButton;

    @FXML
    private Label titleLabel;

    @FXML
    private ComboBox<Type> typeComboBox;
    @FXML
    private TextField capacityField;


    private static Cell cell;
    private static CellType cellType;
    private TypeDaoImpl typeDao = new TypeDaoImpl();
    private CellTypeDaoImpl cellTypeDao = new CellTypeDaoImpl();


    @FXML
    private void initialize() {
        typeComboBox.setItems(FXCollections.observableArrayList(typeDao.getAll()));

        if(cellType == null) {
            titleLabel.setText("Додавання нового типу товару до комірки");
        } else {
            titleLabel.setText("Редагування типу товару в комірці");
            setData();
        }

        backButton.setOnAction(event -> backButton.getScene().getWindow().hide());

        doneButton.setOnAction(event -> {
            if (isInputValid()) {
                if (isTypeValid()) {
                    CellType newCellType = new CellType(
                            null,
                            Integer.valueOf(capacityField.getText().trim()),
                            cell.getCellId(),
                            typeComboBox.getValue().getTypeId()

                    );
                    if (cellType == null) {
                        Integer id = cellTypeDao.save(newCellType);
                        newCellType.setCellTypeId(id);

                        Main.getStorageMenuController().addType(newCellType);
                    } else {
                        newCellType.setCellTypeId(cellType.getCellTypeId());
                        cellTypeDao.update(newCellType);

                        Main.getStorageMenuController().setTypeTable();
                    }
                    doneButton.getScene().getWindow().hide();
                }
            }
        });
    }

    public static void initializeData(Cell cell, CellType cellType) {
        StorageTypeEditController.cell = cell;
        StorageTypeEditController.cellType = cellType;
    }

    private void setData() {
        typeComboBox.setValue(typeDao.get(cellType.getTypeId()));
        capacityField.setText(cellType.getCapacity().toString());
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (typeComboBox.getValue() == null) {
            errorMessage += "Не вказано тип!\n";
        }
        if (capacityField.getText().trim().length() == 0) {
            errorMessage += "Не вказано місткість!\n";
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

    private boolean isTypeValid() {
        String errorMessage = "";
        String input = capacityField.getText().trim();
        String pattern = "[-+]?\\d+";

        if (!input.matches(pattern) || Integer.parseInt(capacityField.getText().trim()) < 0) {
            errorMessage += "Місткість вказана неправильно!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
            alert.setTitle("Storage Keeper");
            alert.setHeaderText("Будь ласка, заповніть усі поля правильно");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }
    }
}


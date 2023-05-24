package Controller;

import DAO.CellDaoImpl;
import DTO.Area;
import DTO.Cell;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

public class StorageCellEditController {
    @FXML
    private Button backButton;
    @FXML
    private Button doneButton;

    @FXML
    private Label titleLabel;

    @FXML
    private TextField nameField;


    private static Area area;
    private static Cell cell;
    private CellDaoImpl cellDao = new CellDaoImpl();


    @FXML
    private void initialize() {
        if(cell == null) {
            titleLabel.setText("Створення нової комірки");
        } else {
            titleLabel.setText("Редагування комірки");
            setData();
        }

        backButton.setOnAction(event -> backButton.getScene().getWindow().hide());

        doneButton.setOnAction(event -> {
            if (isInputValid()) {
                if (cell == null) {
                    Cell newCell = new Cell(
                            null,
                            nameField.getText().trim(),
                            0,
                            area.getAreaId(),
                            null

                    );

                    Integer id = cellDao.save(newCell);
                    newCell.setCellId(id);

                    Main.getStorageMenuController().addCell(newCell);
                } else {
                    Cell newCell = new Cell(
                            cell.getCellId(),
                            nameField.getText().trim(),
                            cell.getOccupancy(),
                            area.getAreaId(),
                            cell.getMaterialId()

                    );

                    cellDao.update(newCell);

                    Main.getStorageMenuController().setCellTable();
                }
                doneButton.getScene().getWindow().hide();
            }
        });
    }

    public static void initializeData(Area area, Cell cell) {
        StorageCellEditController.area = area;
        StorageCellEditController.cell = cell;
    }

    private void setData() {
        nameField.setText(cell.getName());
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText().trim().length() == 0) {
            errorMessage += "Не вказано назву!\n";
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
}

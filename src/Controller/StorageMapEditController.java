package Controller;

import DAO.*;
import DTO.*;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.util.function.UnaryOperator;

public class StorageMapEditController {
    @FXML
    private Button backButton;
    @FXML
    private Button doneButton;

    @FXML
    private Label titleLabel;
    @FXML
    private Label materialLabel;
    @FXML
    private Label leftLabel;
    @FXML
    private Label rightLabel;

    @FXML
    private TextField amountTextField;

    @FXML
    private ImageView leftImageView;
    @FXML
    private ImageView gifImageView;
    @FXML
    private ImageView rightImageView;

    private static String shipmentMode;
    private static Cell cell;
    private static CellType cellType;
    private static Material material;
    private static ShipmentMaterial shipmentMaterial;

    private final AreaDaoImpl areaDao = new AreaDaoImpl();
    private final CellDaoImpl cellDao = new CellDaoImpl();
    private final CellTypeDaoImp cellTypeDao = new CellTypeDaoImp();
    private final MaterialDaoImpl materialDao = new MaterialDaoImpl();
    private final SendingMaterialDaoImpl sendingMaterialDao = new SendingMaterialDaoImpl();
    private final SupplyMaterialDaoImpl supplyMaterialDao = new SupplyMaterialDaoImpl();

    @FXML
    void initialize() {
        material = materialDao.get(shipmentMaterial.getMaterialId());
        cellType = cellTypeDao.get(cell.getCellId(), material.getTypeId());
        setData();
        amountTextField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), null, integerFilter));

        backButton.setOnAction(event -> backButton.getScene().getWindow().hide());

        doneButton.setOnAction(event -> {
            if (amountCheck()) {
                saveData();
            }
        });
    }

    public static void initializeData(String shipmentMode, Cell cell, ShipmentMaterial shipmentMaterial) {
        StorageMapEditController.shipmentMode = shipmentMode;
        StorageMapEditController.cell = cell;
        StorageMapEditController.shipmentMaterial = shipmentMaterial;
    }

    private void setData() {
        switch (shipmentMode) {
            case "supply" -> {
                titleLabel.setText("Завантаження на склад");
                materialLabel.setText(material.getName() + " (" + material.getManufacturer() + ")  →  " + cell.getName() + " (" + areaDao.get(cell.getAreaId()).getName() + ")");
                leftLabel.setText(String.valueOf(shipmentMaterial.getAmount() - shipmentMaterial.getLoadedAmount()));
                rightLabel.setText(cell.getOccupancy().toString() + " / " + cellTypeDao.get(cell.getCellId(), cellType.getTypeId()).getCapacity().toString());
                leftImageView.setImage(new Image(new File("images" + File.separator + "cart.png").toURI().toString()));
                gifImageView.setImage(new Image(new File("images" + File.separator + "gif3.gif").toURI().toString()));
                rightImageView.setImage(new Image(new File("images" + File.separator + "storage.png").toURI().toString()));
            }
            case "sending" -> {
                titleLabel.setText("Вивантаження зі складу");
                materialLabel.setText(cell.getName() + " (" + areaDao.get(cell.getAreaId()).getName() + ")  →  " + material.getName() + " (" + material.getManufacturer() + ")");
                leftLabel.setText(cell.getOccupancy().toString());
                rightLabel.setText(shipmentMaterial.getLoadedAmount().toString() + " / " + shipmentMaterial.getAmount().toString());
                leftImageView.setImage(new Image(new File("images" + File.separator + "storage.png").toURI().toString()));
                gifImageView.setImage(new Image(new File("images" + File.separator + "gif3.gif").toURI().toString()));
                rightImageView.setImage(new Image(new File("images" + File.separator + "cart.png").toURI().toString()));
            }
        }
    }

    private void saveData(){
        //TODO Изменение статуса
        Material material = materialDao.get(shipmentMaterial.getMaterialId());

        switch (shipmentMode) {
            case "supply" -> {
                cell.setOccupancy(cell.getOccupancy() + Integer.parseInt(amountTextField.getText()));
                cell.setMaterialId(material.getMaterialId());
                material.setAmount(material.getAmount() + Integer.parseInt(amountTextField.getText()));
                shipmentMaterial.setLoadedAmount(shipmentMaterial.getLoadedAmount() + Integer.parseInt(amountTextField.getText()));

                supplyMaterialDao.update(shipmentMaterial);
                materialDao.update(material);
                cellDao.update(cell);
                backButton.fire();
            }
            case "sending" -> {
                cell.setOccupancy(cell.getOccupancy() - Integer.parseInt(amountTextField.getText()));
                if (cell.getOccupancy() == 0) {
                    cell.setMaterialId(0);
                }
                material.setAmount(material.getAmount() - Integer.parseInt(amountTextField.getText()));
                shipmentMaterial.setLoadedAmount(shipmentMaterial.getLoadedAmount() + Integer.parseInt(amountTextField.getText()));

                sendingMaterialDao.update(shipmentMaterial);
                materialDao.update(material);
                cellDao.update(cell);
                backButton.fire();
            }
        }
    }

    private final UnaryOperator<TextFormatter.Change> integerFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("-?([1-9][0-9]*)?")) {
            return change;
        }
        return null;
    };

    private boolean amountCheck() {
        if (amountTextField.getText().equals("")) {
            return false;
        }

        switch (shipmentMode) {
            case "supply" -> {
                if (Integer.parseInt(amountTextField.getText()) > cellType.getCapacity() - cell.getOccupancy()) {
                    return false;
                }
                if (Integer.parseInt(amountTextField.getText()) > shipmentMaterial.getAmount() - shipmentMaterial.getLoadedAmount()) {
                    return false;
                }
            }
            case "sending" -> {
                if (Integer.parseInt(amountTextField.getText()) > cell.getOccupancy()) {
                    return false;
                }
                if (Integer.parseInt(amountTextField.getText()) > shipmentMaterial.getAmount() - shipmentMaterial.getLoadedAmount()) {
                    return false;
                }
            }
        }
        return true;
    }
}

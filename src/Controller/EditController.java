package Controller;

import Database.ShipmentDataAccessor;
import Model.Cell;
import Model.Material;
import Model.ShipmentMaterial;

import Model.StaticData;
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

public class EditController {
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
    private static ShipmentMaterial shipmentMaterial;

    @FXML
    void initialize() {
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
        EditController.shipmentMode = shipmentMode;
        EditController.cell = cell;
        EditController.shipmentMaterial = shipmentMaterial;
    }

    private void setData() {
        Material material = StaticData.getMaterialHashMap().get(shipmentMaterial.getMaterialId());

        switch (shipmentMode) {
            case "supply":
                titleLabel.setText("Погрузка на склад");
                materialLabel.setText(material.getName() + " (" + material.getManufacturer() + ")  →  " + cell.getName() + " (" + StaticData.getAreaHashMap().get(cell.getAreaId()).getName() + ")");
                leftLabel.setText(String.valueOf(shipmentMaterial.getAmount() - shipmentMaterial.getLoadedAmount()));
                rightLabel.setText(cell.getOccupancy().toString() + " / " + cell.getCapacity().toString());

                leftImageView.setImage(new Image(new File("images" + File.separator + "cart.png").toURI().toString()));
                gifImageView.setImage(new Image(new File("images" + File.separator + "gif3.gif").toURI().toString()));
                rightImageView.setImage(new Image(new File("images" + File.separator + "storage.png").toURI().toString()));
                break;

            case "sending":
                titleLabel.setText("Выгрузка со склада");
                materialLabel.setText(cell.getName() + " (" + StaticData.getAreaHashMap().get(cell.getAreaId()).getName() + ")  →  " + material.getName() + " (" + material.getManufacturer() + ")");
                leftLabel.setText(cell.getOccupancy().toString());
                rightLabel.setText(shipmentMaterial.getLoadedAmount().toString() + " / " + shipmentMaterial.getAmount().toString());

                leftImageView.setImage(new Image(new File("images" + File.separator + "storage.png").toURI().toString()));
                gifImageView.setImage(new Image(new File("images" + File.separator + "gif3.gif").toURI().toString()));
                rightImageView.setImage(new Image(new File("images" + File.separator + "cart.png").toURI().toString()));
                break;
        }
    }

    private void saveData(){
        Material material = StaticData.getMaterialHashMap().get(shipmentMaterial.getMaterialId());
        ShipmentDataAccessor shipmentDataAccessor = new ShipmentDataAccessor();

        switch (shipmentMode) {
            case "supply":
                cell.setOccupancy(cell.getOccupancy() + Integer.parseInt(amountTextField.getText()));
                material.setAmount(material.getAmount() + Integer.parseInt(amountTextField.getText()));
                shipmentMaterial.setLoadedAmount(shipmentMaterial.getLoadedAmount() + Integer.parseInt(amountTextField.getText()));

                StaticData.setCell(cell);
                StaticData.setMaterial(material);
                shipmentDataAccessor.updateSupplyMaterial(shipmentMaterial);

                backButton.fire();
                break;

            case "sending":
                cell.setOccupancy(cell.getOccupancy() - Integer.parseInt(amountTextField.getText()));
                material.setAmount(material.getAmount() - Integer.parseInt(amountTextField.getText()));
                shipmentMaterial.setLoadedAmount(shipmentMaterial.getLoadedAmount() + Integer.parseInt(amountTextField.getText()));

                StaticData.setCell(cell);
                StaticData.setMaterial(material);
                shipmentDataAccessor.updateSendingMaterial(shipmentMaterial);

                backButton.fire();
                break;
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
            case "supply":
                if (Integer.parseInt(amountTextField.getText()) > cell.getCapacity() - cell.getOccupancy()) {
                    return false;
                }
                if (Integer.parseInt(amountTextField.getText()) > shipmentMaterial.getAmount() - shipmentMaterial.getLoadedAmount()) {
                    return false;
                }
                break;

            case "sending":
                if (Integer.parseInt(amountTextField.getText()) > cell.getOccupancy()) {
                    return false;
                }
                if (Integer.parseInt(amountTextField.getText()) > shipmentMaterial.getAmount() - shipmentMaterial.getLoadedAmount()) {
                    return false;
                }
                break;
        }
        return true;
    }
}

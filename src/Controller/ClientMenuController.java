package Controller;

import DAO.ClientDaoImpl;
import DTO.Shipper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;

public class ClientMenuController {
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
    private Button newClientButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Shipper> clientTable;
    @FXML
    private TableColumn<Shipper, String> nameColumn;
    @FXML
    private TableColumn<Shipper, String> addressColumn;
    @FXML
    private TableColumn<Shipper, String> phoneNumberColumn;
    @FXML
    private TableColumn<Shipper, String> contactPersonColumn;


    private ClientDaoImpl clientDao = new ClientDaoImpl();


    @FXML
    void initialize() {
        Main.setClientMenuController(this);
        setClientTable();

        clientTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    editButton.setDisable(false);
                    deleteButton.setDisable(false);
                });

        backButton.setOnAction(event -> Main.newWindow("/View/SendingMenu.fxml"));

        storageMapButton.setOnAction(event -> Main.newWindow("/View/StorageMapMenu.fxml"));
        shipmentButton.setOnAction(event -> Main.newWindow("/View/SendingMenu.fxml"));
        materialButton.setOnAction(event -> Main.newWindow("/View/MaterialMenu.fxml"));
        employeeButton.setOnAction(event -> Main.newWindow("/View/EmployeeMenu.fxml"));

        newClientButton.setOnAction(event -> {
            ClientEditController.initializeData(null);
            Main.newModalWindow("/View/ClientEdit.fxml");
        });

        editButton.setOnAction(event -> {
            if(clientTable.getSelectionModel().getSelectedItem() != null) {
                ClientEditController.initializeData(clientTable.getSelectionModel().getSelectedItem());
                Main.newModalWindow("/View/ClientEdit.fxml");
            }
        });

        deleteButton.setOnAction(event -> {
            if(clientTable.getSelectionModel().getSelectedItem() != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
                alert.setTitle("Storage Keeper");
                alert.setHeaderText("Ви впевнені, що бажаєте видалити замовника?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get() == ButtonType.OK) {
                    clientDao.delete(clientTable.getSelectionModel().getSelectedItem());
                    clientTable.getItems().remove(clientTable.getSelectionModel().getSelectedIndex());
                }
            }
        });
    }

    public void setClientTable() {
        int selectedIndex = clientTable.getSelectionModel().getSelectedIndex();

        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName())
        );
        addressColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAddress())
        );
        phoneNumberColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPhoneNumber())
        );
        contactPersonColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getContactPerson())
        );

        clientTable.setItems(FXCollections.observableArrayList(clientDao.getAll()));
        clientTable.getSelectionModel().select(selectedIndex);
    }

    public void addClient(Shipper client) {
        clientTable.getItems().add(client);
    }
}


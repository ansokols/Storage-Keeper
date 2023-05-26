package Controller;

import DAO.PostDaoImpl;
import DAO.TypeDaoImpl;
import DTO.Post;
import DTO.Type;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;

public class TypeMenuController {
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
    private Button newTypeButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Type> typeTable;
    @FXML
    private TableColumn<Type, String> nameColumn;


    private final PostDaoImpl postDao = new PostDaoImpl();
    private final TypeDaoImpl typeDao = new TypeDaoImpl();


    @FXML
    void initialize() {
        Main.setTypeMenuController(this);
        setTypeTable();
        setAccess();

        typeTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    editButton.setDisable(false);
                    deleteButton.setDisable(false);
                });

        backButton.setOnAction(event -> Main.newWindow("/View/MaterialMenu.fxml"));

        storageMapButton.setOnAction(event -> Main.newWindow("/View/StorageMapMenu.fxml"));
        shipmentButton.setOnAction(event -> Main.newWindow("/View/SupplyMenu.fxml"));
        materialButton.setOnAction(event -> Main.newWindow("/View/MaterialMenu.fxml"));
        employeeButton.setOnAction(event -> Main.newWindow("/View/EmployeeMenu.fxml"));

        newTypeButton.setOnAction(event -> {
            TypeEditController.initializeData(null);
            Main.newModalWindow("/View/TypeEdit.fxml");
        });

        editButton.setOnAction(event -> {
            if(typeTable.getSelectionModel().getSelectedItem() != null) {
                TypeEditController.initializeData(typeTable.getSelectionModel().getSelectedItem());
                Main.newModalWindow("/View/TypeEdit.fxml");
            }
        });

        deleteButton.setOnAction(event -> {
            if(typeTable.getSelectionModel().getSelectedItem() != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
                alert.setTitle("Storage Keeper");
                alert.setHeaderText("Ви впевнені, що бажаєте видалити тип товару?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get() == ButtonType.OK) {
                    typeDao.delete(typeTable.getSelectionModel().getSelectedItem());
                    typeTable.getItems().remove(typeTable.getSelectionModel().getSelectedIndex());
                }
            }
        });
    }

    private void setAccess() {
        Post post = postDao.get(Main.getEmployee().getPostId());

        backButton.setVisible(post.isMaterialAccess());
        storageMapButton.setVisible(post.isStorageMapAccess());

        shipmentButton.setVisible(post.isShipmentAccess());
        materialButton.setVisible(post.isMaterialAccess());
        employeeButton.setVisible(post.isEmployeeAccess());

        newTypeButton.setVisible(post.isTypeEdit());
        editButton.setVisible(post.isTypeEdit());
        deleteButton.setVisible(post.isTypeEdit());
    }

    public void setTypeTable() {
        int selectedIndex = typeTable.getSelectionModel().getSelectedIndex();

        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName())
        );

        typeTable.setItems(FXCollections.observableArrayList(typeDao.getAll()));
        typeTable.getSelectionModel().select(selectedIndex);
    }

    public void addType(Type type) {
        typeTable.getItems().add(type);
    }
}

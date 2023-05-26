package Controller;

import DAO.PostDaoImpl;
import DTO.Post;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;

public class PostMenuController {
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
    private Button newPostButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Post> postTable;
    @FXML
    private TableColumn<Post, String> nameColumn;
    @FXML
    private TableColumn<Post, Boolean> employeeAccessColumn;
    @FXML
    private TableColumn<Post, Boolean> employeeEditColumn;
    @FXML
    private TableColumn<Post, Boolean> materialAccessColumn;
    @FXML
    private TableColumn<Post, Boolean> materialEditColumn;
    @FXML
    private TableColumn<Post, Boolean> postAccessColumn;
    @FXML
    private TableColumn<Post, Boolean> postEditColumn;
    @FXML
    private TableColumn<Post, Boolean> shipmentAccessColumn;
    @FXML
    private TableColumn<Post, Boolean> shipmentEditColumn;
    @FXML
    private TableColumn<Post, Boolean> shipperAccessColumn;
    @FXML
    private TableColumn<Post, Boolean> shipperEditColumn;
    @FXML
    private TableColumn<Post, Boolean> storageMapAccessColumn;
    @FXML
    private TableColumn<Post, Boolean> storageMapEditColumn;
    @FXML
    private TableColumn<Post, Boolean> storageAccessColumn;
    @FXML
    private TableColumn<Post, Boolean> storageEditColumn;
    @FXML
    private TableColumn<Post, Boolean> typeAccessColumn;
    @FXML
    private TableColumn<Post, Boolean> typeEditColumn;


    private final PostDaoImpl postDao = new PostDaoImpl();


    @FXML
    void initialize() {
        Main.setPostMenuController(this);
        setPostTable();
        setAccess();

        postTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    editButton.setDisable(false);
                    deleteButton.setDisable(false);
                });

        backButton.setOnAction(event -> Main.newWindow("/View/EmployeeMenu.fxml"));

        storageMapButton.setOnAction(event -> Main.newWindow("/View/StorageMapMenu.fxml"));
        shipmentButton.setOnAction(event -> Main.newWindow("/View/SupplyMenu.fxml"));
        materialButton.setOnAction(event -> Main.newWindow("/View/MaterialMenu.fxml"));
        employeeButton.setOnAction(event -> Main.newWindow("/View/EmployeeMenu.fxml"));

        newPostButton.setOnAction(event -> {
            PostEditController.initializeData(null);
            Main.newModalWindow("/View/PostEdit.fxml");
        });

        editButton.setOnAction(event -> {
            if(postTable.getSelectionModel().getSelectedItem() != null) {
                PostEditController.initializeData(postTable.getSelectionModel().getSelectedItem());
                Main.newModalWindow("/View/PostEdit.fxml");
            }
        });

        deleteButton.setOnAction(event -> {
            if(postTable.getSelectionModel().getSelectedItem() != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
                alert.setTitle("Storage Keeper");
                alert.setHeaderText("Ви впевнені, що бажаєте видалити посаду?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get() == ButtonType.OK) {
                    postDao.delete(postTable.getSelectionModel().getSelectedItem());
                    postTable.getItems().remove(postTable.getSelectionModel().getSelectedIndex());
                }
            }
        });
    }

    private void setAccess() {
        Post post = postDao.get(Main.getEmployee().getPostId());

        backButton.setVisible(post.isEmployeeAccess());
        storageMapButton.setVisible(post.isStorageMapAccess());

        shipmentButton.setVisible(post.isShipmentAccess());
        materialButton.setVisible(post.isMaterialAccess());
        employeeButton.setVisible(post.isEmployeeAccess());

        newPostButton.setVisible(post.isPostEdit());
        editButton.setVisible(post.isPostEdit());
        deleteButton.setVisible(post.isPostEdit());
    }

    public void setPostTable() {
        int selectedIndex = postTable.getSelectionModel().getSelectedIndex();

        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName())
        );
        employeeAccessColumn.setCellValueFactory(cellData ->
                new SimpleBooleanProperty(cellData.getValue().isEmployeeAccess())
        );
        employeeEditColumn.setCellValueFactory(cellData ->
                new SimpleBooleanProperty(cellData.getValue().isEmployeeEdit())
        );
        materialAccessColumn.setCellValueFactory(cellData ->
                new SimpleBooleanProperty(cellData.getValue().isMaterialAccess())
        );
        materialEditColumn.setCellValueFactory(cellData ->
                new SimpleBooleanProperty(cellData.getValue().isMaterialEdit())
        );
        postAccessColumn.setCellValueFactory(cellData ->
                new SimpleBooleanProperty(cellData.getValue().isPostAccess())
        );
        postEditColumn.setCellValueFactory(cellData ->
                new SimpleBooleanProperty(cellData.getValue().isPostEdit())
        );
        shipmentAccessColumn.setCellValueFactory(cellData ->
                new SimpleBooleanProperty(cellData.getValue().isShipmentAccess())
        );
        shipmentEditColumn.setCellValueFactory(cellData ->
                new SimpleBooleanProperty(cellData.getValue().isShipmentEdit())
        );
        shipperAccessColumn.setCellValueFactory(cellData ->
                new SimpleBooleanProperty(cellData.getValue().isShipperAccess())
        );
        shipperEditColumn.setCellValueFactory(cellData ->
                new SimpleBooleanProperty(cellData.getValue().isShipperEdit())
        );
        storageMapAccessColumn.setCellValueFactory(cellData ->
                new SimpleBooleanProperty(cellData.getValue().isStorageMapAccess())
        );
        storageMapEditColumn.setCellValueFactory(cellData ->
                new SimpleBooleanProperty(cellData.getValue().isStorageMapEdit())
        );
        storageAccessColumn.setCellValueFactory(cellData ->
                new SimpleBooleanProperty(cellData.getValue().isStorageAccess())
        );
        storageEditColumn.setCellValueFactory(cellData ->
                new SimpleBooleanProperty(cellData.getValue().isStorageEdit())
        );
        typeAccessColumn.setCellValueFactory(cellData ->
                new SimpleBooleanProperty(cellData.getValue().isTypeAccess())
        );
        typeEditColumn.setCellValueFactory(cellData ->
                new SimpleBooleanProperty(cellData.getValue().isTypeEdit())
        );

        postTable.setItems(FXCollections.observableArrayList(postDao.getAll()));
        postTable.getSelectionModel().select(selectedIndex);
    }

    public void addPost(Post post) {
        postTable.getItems().add(post);
    }
}

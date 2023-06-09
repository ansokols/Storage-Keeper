package Controller;

import DAO.EmployeeDaoImpl;
import DAO.PostDaoImpl;
import DTO.Employee;
import DTO.Post;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Timestamp;
import java.util.Optional;

public class EmployeeMenuController {
    @FXML
    private Button backButton;
    @FXML
    private Button postButton;

    @FXML
    private Button storageMapButton;
    @FXML
    private Button shipmentButton;
    @FXML
    private Button materialButton;
    @FXML
    private Button employeeButton;

    @FXML
    private Button newEmployeeButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, String> nameColumn;
    @FXML
    private TableColumn<Employee, String> postColumn;
    @FXML
    private TableColumn<Employee, String> emailColumn;
    @FXML
    private TableColumn<Employee, String> phoneColumn;
    @FXML
    private TableColumn<Employee, Timestamp> enrollmentDateColumn;
    @FXML
    private TableColumn<Employee, Timestamp> dissmissalDateColumn;


    private final EmployeeDaoImpl employeeDao = new EmployeeDaoImpl();
    private final PostDaoImpl postDao = new PostDaoImpl();


    @FXML
    void initialize() {
        Main.setEmployeeMenuController(this);
        setEmployeeTable();
        setAccess();

        employeeTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    editButton.setDisable(false);
                    deleteButton.setDisable(false);
                });

        backButton.setOnAction(event -> Main.newWindow("/View/AuthorizationMenu.fxml"));
        postButton.setOnAction(event -> Main.newWindow("/View/PostMenu.fxml"));

        storageMapButton.setOnAction(event -> Main.newWindow("/View/StorageMapMenu.fxml"));
        shipmentButton.setOnAction(event -> Main.newWindow("/View/SupplyMenu.fxml"));
        materialButton.setOnAction(event -> Main.newWindow("/View/MaterialMenu.fxml"));
        employeeButton.setOnAction(event -> Main.newWindow("/View/EmployeeMenu.fxml"));

        newEmployeeButton.setOnAction(event -> {
            EmployeeEditController.initializeData(null);
            Main.newModalWindow("/View/EmployeeEdit.fxml");
        });

        editButton.setOnAction(event -> {
            if(employeeTable.getSelectionModel().getSelectedItem() != null) {
                EmployeeEditController.initializeData(employeeTable.getSelectionModel().getSelectedItem());
                Main.newModalWindow("/View/EmployeeEdit.fxml");
            }
        });

        deleteButton.setOnAction(event -> {
            if(employeeTable.getSelectionModel().getSelectedItem() != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
                alert.setTitle("Storage Keeper");
                alert.setHeaderText("Ви впевнені, що бажаєте видалити співробітника?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get() == ButtonType.OK) {
                    employeeDao.delete(employeeTable.getSelectionModel().getSelectedItem());
                    employeeTable.getItems().remove(employeeTable.getSelectionModel().getSelectedIndex());
                }
            }
        });
    }

    private void setAccess() {
        Post post = postDao.get(Main.getEmployee().getPostId());

        postButton.setVisible(post.isPostAccess());
        storageMapButton.setVisible(post.isStorageMapAccess());
        shipmentButton.setVisible(post.isShipmentAccess());
        materialButton.setVisible(post.isMaterialAccess());
        employeeButton.setVisible(post.isEmployeeAccess());

        newEmployeeButton.setVisible(post.isEmployeeEdit());
        editButton.setVisible(post.isEmployeeEdit());
        deleteButton.setVisible(post.isEmployeeEdit());
    }

    public void setEmployeeTable() {
        int selectedIndex = employeeTable.getSelectionModel().getSelectedIndex();

        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName())
        );
        postColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(postDao.get(cellData.getValue().getPostId()).getName())
        );
        emailColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEmail())
        );
        phoneColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPhoneNumber())
        );
        enrollmentDateColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getEnrollmentDate())
        );
        dissmissalDateColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getDismissalDate())
        );

        employeeTable.setItems(FXCollections.observableArrayList(employeeDao.getAll()));
        employeeTable.getSelectionModel().select(selectedIndex);
    }

    public void addEmployee(Employee employee) {
        employeeTable.getItems().add(employee);
    }
}

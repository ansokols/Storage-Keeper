package Controller;

import DAO.PostDaoImpl;
import DTO.Post;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

public class PostEditController {
    @FXML
    private Button backButton;
    @FXML
    private Button doneButton;

    @FXML
    private Label titleLabel;

    @FXML
    private TextField nameField;
    @FXML
    private RadioButton employeeAccessButton;
    @FXML
    private RadioButton employeeEditButton;
    @FXML
    private RadioButton materialAccessButton;
    @FXML
    private RadioButton materialEditButton;
    @FXML
    private RadioButton postAccessButton;
    @FXML
    private RadioButton postEditButton;
    @FXML
    private RadioButton shipmentAccessButton;
    @FXML
    private RadioButton shipmentEditButton;
    @FXML
    private RadioButton shipperAccessButton;
    @FXML
    private RadioButton shipperEditButton;
    @FXML
    private RadioButton storageMapAccessButton;
    @FXML
    private RadioButton storageMapEditButton;
    @FXML
    private RadioButton storageAccessButton;
    @FXML
    private RadioButton storageEditButton;
    @FXML
    private RadioButton typeAccessButton;
    @FXML
    private RadioButton typeEditButton;


    private static Post post;
    private final PostDaoImpl postDao = new PostDaoImpl();


    @FXML
    private void initialize() {
        if (post == null) {
            titleLabel.setText("Створення нової посади");
        } else {
            titleLabel.setText("Редагування посади");
            setData();
        }

        backButton.setOnAction(event -> backButton.getScene().getWindow().hide());

        doneButton.setOnAction(event -> {
            if (isInputValid()) {
                Post newPost = new Post(
                        null,
                        nameField.getText().trim(),
                        employeeAccessButton.isSelected(),
                        employeeEditButton.isSelected(),
                        materialAccessButton.isSelected(),
                        materialEditButton.isSelected(),
                        postAccessButton.isSelected(),
                        postEditButton.isSelected(),
                        shipmentAccessButton.isSelected(),
                        shipmentEditButton.isSelected(),
                        shipperAccessButton.isSelected(),
                        shipperEditButton.isSelected(),
                        storageMapAccessButton.isSelected(),
                        storageMapEditButton.isSelected(),
                        storageAccessButton.isSelected(),
                        storageEditButton.isSelected(),
                        typeAccessButton.isSelected(),
                        typeEditButton.isSelected()
                );

                if (post == null) {
                    Integer id = postDao.save(newPost);
                    newPost.setPostId(id);

                    Main.getPostMenuController().addPost(newPost);
                } else {
                    newPost.setPostId(post.getPostId());
                    postDao.update(newPost);

                    Main.getPostMenuController().setPostTable();
                }
                doneButton.getScene().getWindow().hide();
            }
        });
    }

    public static void initializeData(Post post) {
        PostEditController.post = post;
    }

    private void setData() {
        nameField.setText(post.getName());
        employeeAccessButton.setSelected(post.isEmployeeAccess());
        employeeEditButton.setSelected(post.isEmployeeEdit());
        materialAccessButton.setSelected(post.isMaterialAccess());
        materialEditButton.setSelected(post.isMaterialEdit());
        postAccessButton.setSelected(post.isPostAccess());
        postEditButton.setSelected(post.isPostEdit());
        shipmentAccessButton.setSelected(post.isShipmentAccess());
        shipmentEditButton.setSelected(post.isShipmentEdit());
        shipperAccessButton.setSelected(post.isShipperAccess());
        shipperEditButton.setSelected(post.isShipperEdit());
        storageMapAccessButton.setSelected(post.isStorageMapAccess());
        storageMapEditButton.setSelected(post.isStorageMapEdit());
        storageAccessButton.setSelected(post.isStorageAccess());
        storageEditButton.setSelected(post.isStorageEdit());
        typeAccessButton.setSelected(post.isTypeAccess());
        typeEditButton.setSelected(post.isTypeEdit());
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
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
            alert.setTitle("Storage Keeper");
            alert.setHeaderText("Будь ласка, заповніть усі поля");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }
    }
}
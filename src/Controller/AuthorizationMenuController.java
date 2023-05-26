package Controller;

import DAO.AuthorizationDao;
import DAO.AuthorizationDaoImpl;
import DAO.PostDaoImpl;
import DTO.Employee;
import DTO.Post;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;


public class AuthorizationMenuController {
    @FXML
    private Button enterButton;

    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;


    private final AuthorizationDao authorizationDao = new AuthorizationDaoImpl();
    private final PostDaoImpl postDao = new PostDaoImpl();


    @FXML
    void initialize() {
        enterButton.setOnAction(event -> {
            if(!loginField.getText().trim().equals("") && !passwordField.getText().trim().equals("")) {
                Employee employee = authorizationDao.checkPassword(loginField.getText().trim(), passwordField.getText().trim());
                if (employee == null) {
                    Main.setEmployee(null);

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
                    alert.setTitle("Ante Room");
                    alert.setHeaderText("Введено неправильний логін або пароль");
                    alert.showAndWait();
                } else {
                    String startMenuRoute = getStartMenuRoute(employee);

                    if (startMenuRoute != null) {
                        Main.setEmployee(employee);
                        Main.newWindow(startMenuRoute);
                    } else {
                        Main.setEmployee(null);

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
                        alert.setTitle("Ante Room");
                        alert.setHeaderText("На жаль, у вас немає доступу до системи");
                        alert.showAndWait();
                    }

                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
                alert.setTitle("Ante Room");
                alert.setHeaderText("Будь ласка, заповніть усі поля");
                alert.showAndWait();
            }
        });
    }

    private String getStartMenuRoute(Employee employee) {
        Post post = postDao.get(employee.getPostId());
        String startMenuRoute = null;

        if (post.isStorageMapAccess()) {
            startMenuRoute = "/View/StorageMapMenu.fxml";
        } else if (post.isShipmentAccess()) {
            startMenuRoute = "/View/SupplyMenu.fxml";
        } else if (post.isMaterialAccess()) {
            startMenuRoute = "/View/MaterialMenu.fxml";
        } else if (post.isEmployeeAccess()) {
            startMenuRoute = "/View/EmployeeMenu.fxml";
        } else if (post.isStorageAccess()) {
            startMenuRoute = "/View/StorageMenu.fxml";
        } else if (post.isShipperAccess()) {
            startMenuRoute = "/View/SupplierMenu.fxml";
        } else if (post.isTypeAccess()) {
            startMenuRoute = "/View/TypeMenu.fxml";
        } else if (post.isPostAccess()) {
            startMenuRoute = "/View/PostMenu.fxml";
        }

        return startMenuRoute;
    }
}

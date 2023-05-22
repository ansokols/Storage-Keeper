package Controller;

import DAO.EmployeeDaoImpl;
import DAO.PostDaoImpl;
import DTO.Employee;
import DTO.Post;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Timestamp;

public class EmployeeEditController {
    @FXML
    private Button backButton;
    @FXML
    private Button doneButton;

    @FXML
    private Label titleLabel;

    @FXML
    private TextField nameField;
    @FXML
    private ComboBox<Post> postComboBox;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private DatePicker enrollmentDatePicker;
    @FXML
    private DatePicker dismissalDatePicker;
    @FXML
    private TextField loginField;
    @FXML
    private TextField passwordField;


    private static Employee employee;
    private EmployeeDaoImpl employeeDao = new EmployeeDaoImpl();
    private PostDaoImpl postDao = new PostDaoImpl();


    @FXML
    private void initialize() {
        postComboBox.setItems(FXCollections.observableArrayList(postDao.getAll()));

        if(employee == null) {
            titleLabel.setText("Створення нового співробітника");
        } else {
            titleLabel.setText("Редагування співробітника");
            setData();
        }

        backButton.setOnAction(event -> backButton.getScene().getWindow().hide());

        doneButton.setOnAction(event -> {
            if (isInputValid()) {
                Employee newEmployee = new Employee(
                        null,
                        nameField.getText().trim(),
                        emailField.getText().trim(),
                        phoneField.getText().trim(),
                        Timestamp.valueOf(enrollmentDatePicker.getValue().atStartOfDay()),
                        null,
                        loginField.getText().trim(),
                        passwordField.getText().trim(),
                        postComboBox.getValue().getPostId()
                );
                if (dismissalDatePicker.getValue() != null) {
                    newEmployee.setDismissalDate(Timestamp.valueOf(dismissalDatePicker.getValue().atStartOfDay()));
                }

                if (employee == null) {
                    Integer id = employeeDao.save(newEmployee);
                    newEmployee.setEmployeeId(id);

                    Main.getEmployeeMenuController().addEmployee(newEmployee);
                } else {
                    newEmployee.setEmployeeId(employee.getEmployeeId());
                    employeeDao.update(newEmployee);

                    Main.getEmployeeMenuController().setEmployeeTable();
                }
                doneButton.getScene().getWindow().hide();
            }
        });
    }

    public static void initializeData(Employee employee) {
        EmployeeEditController.employee = employee;
    }

    private void setData() {
        nameField.setText(employee.getName());
        postComboBox.setValue(postDao.get(employee.getPostId()));
        emailField.setText(employee.getEmail());
        phoneField.setText(employee.getPhoneNumber());
        enrollmentDatePicker.setValue(employee.getEnrollmentDate().toLocalDateTime().toLocalDate());
        if (employee.getDismissalDate() != null) {
            dismissalDatePicker.setValue(employee.getDismissalDate().toLocalDateTime().toLocalDate());
        }
        loginField.setText(employee.getLogin());
        passwordField.setText(employee.getPassword());
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText().trim().length() == 0) {
            errorMessage += "Не вказано ім'я!\n";
        }
        if (emailField.getText().trim().length() == 0) {
            errorMessage += "Не вказано емейл!\n";
        }
        if (phoneField.getText().trim().length() == 0) {
            errorMessage += "Не вказано телефон!\n";
        }
        if (enrollmentDatePicker.getValue() == null) {
            errorMessage += "Не вказано дату прийняття!\n";
        }
        if (loginField.getText().trim().length() == 0) {
            errorMessage += "Не вказано логін!\n";
        }
        if (passwordField.getText().trim().length() == 0) {
            errorMessage += "Не вказано пароль!\n";
        }
        if (postComboBox.getValue() == null) {
            errorMessage += "Не вказано посаду!\n";
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

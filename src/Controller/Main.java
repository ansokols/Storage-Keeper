package Controller;

import DTO.Employee;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


public class Main extends Application {

    private static Stage primaryStage;
    private static Employee employee;

    private static StorageMenuController storageMenuController;
    private static SupplyMenuController supplyMenuController;
    private static SendingMenuController sendingMenuController;
    private static SupplierMenuController supplierMenuController;
    private static ClientMenuController clientMenuController;
    private static MaterialMenuController materialMenuController;
    private static EmployeeMenuController employeeMenuController;
    private static PostMenuController postMenuController;
    private static TypeMenuController typeMenuController;


    @Override
    public void start(Stage primaryStage) {
        Main.primaryStage = primaryStage;
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/View/AuthorizationMenu.fxml"));
            //root = FXMLLoader.load(getClass().getResource("/View/StorageMapMenu.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setTitle("Storage Keeper");
        primaryStage.getIcons().add(new Image("file:images" + File.separator + "logo.png"));
        primaryStage.setScene(new Scene(root, 1024, 640)); //1216, 839
        primaryStage.setMinWidth(854);
        primaryStage.setMinHeight(409);
        primaryStage.show();
    }

    public static void newWindow(String link) {
        Parent root = null;
        try {
            root = FXMLLoader.load(Main.class.getResource(link));
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setScene(new Scene(root, primaryStage.getWidth() - 16, primaryStage.getHeight() - 39));
    }

    public static void newModalWindow(String link) {
        Stage stage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(Main.class.getResource(link));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Storage Keeper");
        stage.getIcons().add(new Image("file:Images" + File.separator + "logo.png"));
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Employee getEmployee() {
        return employee;
    }

    public static void setEmployee(Employee employee) {
        Main.employee = employee;
    }

    public static StorageMenuController getStorageMenuController() {
        return storageMenuController;
    }

    public static void setStorageMenuController(StorageMenuController storageMenuController) {
        Main.storageMenuController = storageMenuController;
    }

    public static SupplyMenuController getSupplyMenuController() {
        return supplyMenuController;
    }

    public static void setSupplyMenuController(SupplyMenuController supplyMenuController) {
        Main.supplyMenuController = supplyMenuController;
    }

    public static SendingMenuController getSendingMenuController() {
        return sendingMenuController;
    }

    public static void setSendingMenuController(SendingMenuController sendingMenuController) {
        Main.sendingMenuController = sendingMenuController;
    }

    public static SupplierMenuController getSupplierMenuController() {
        return supplierMenuController;
    }

    public static void setSupplierMenuController(SupplierMenuController supplierMenuController) {
        Main.supplierMenuController = supplierMenuController;
    }

    public static ClientMenuController getClientMenuController() {
        return clientMenuController;
    }

    public static void setClientMenuController(ClientMenuController clientMenuController) {
        Main.clientMenuController = clientMenuController;
    }

    public static MaterialMenuController getMaterialMenuController() {
        return materialMenuController;
    }

    public static void setMaterialMenuController(MaterialMenuController materialMenuController) {
        Main.materialMenuController = materialMenuController;
    }

    public static EmployeeMenuController getEmployeeMenuController() {
        return employeeMenuController;
    }

    public static void setEmployeeMenuController(EmployeeMenuController employeeMenuController) {
        Main.employeeMenuController = employeeMenuController;
    }

    public static PostMenuController getPostMenuController() {
        return postMenuController;
    }

    public static void setPostMenuController(PostMenuController postMenuController) {
        Main.postMenuController = postMenuController;
    }

    public static TypeMenuController getTypeMenuController() {
        return typeMenuController;
    }

    public static void setTypeMenuController(TypeMenuController typeMenuController) {
        Main.typeMenuController = typeMenuController;
    }
}

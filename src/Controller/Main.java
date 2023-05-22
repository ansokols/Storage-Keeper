package Controller;

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

    private static EmployeeMenuController employeeMenuController;
    private static MaterialMenuController materialMenuController;
    private static SupplyMenuController supplyMenuController;
    private static SendingMenuController sendingMenuController;
    private static ShipperMenuController shipperMenuController;
    private static StorageMapMenuController storageMapMenuController;
    private static StorageMenuController storageMenuController;


    @Override
    public void start(Stage primaryStage) {
        Main.primaryStage = primaryStage;
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/View/StorageMapMenu.fxml"));
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


    public static EmployeeMenuController getEmployeeMenuController() {
        return employeeMenuController;
    }

    public static void setEmployeeMenuController(EmployeeMenuController employeeMenuController) {
        Main.employeeMenuController = employeeMenuController;
    }

    public static MaterialMenuController getMaterialMenuController() {
        return materialMenuController;
    }

    public static void setMaterialMenuController(MaterialMenuController materialMenuController) {
        Main.materialMenuController = materialMenuController;
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

    public static ShipperMenuController getShipperMenuController() {
        return shipperMenuController;
    }

    public static void setShipperMenuController(ShipperMenuController shipperMenuController) {
        Main.shipperMenuController = shipperMenuController;
    }

    public static StorageMapMenuController getStorageMapMenuController() {
        return storageMapMenuController;
    }

    public static void setStorageMapMenuController(StorageMapMenuController storageMapMenuController) {
        Main.storageMapMenuController = storageMapMenuController;
    }

    public static StorageMenuController getStorageMenuController() {
        return storageMenuController;
    }

    public static void setStorageMenuController(StorageMenuController storageMenuController) {
        Main.storageMenuController = storageMenuController;
    }
}

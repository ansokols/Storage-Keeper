import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/View/storage.fxml"));
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

    public static void main(String[] args) {
        launch(args);
    }
}

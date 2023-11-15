package andyanderson.appointments;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Main class to launch the application
 * @author Andy Anderson
 */
public class Main extends Application {
    public static Stage stage;

    /**
     * Method to launch the GUI
     * @param stage stage
     */
    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Scheduling Utility");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Main method to launch application and connect to DBMS
     * @param args args
     */
    public static void main(String[] args) {
        Database.openConnection();
        launch(args);
        Database.closeConnection();
    }
}

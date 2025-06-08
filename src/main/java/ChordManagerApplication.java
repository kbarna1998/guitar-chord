import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChordManagerApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/ChordManager.fxml"));
        stage.setTitle("Guitar Chord Manager");
        stage.setScene(new Scene(root));
        stage.show();
    }
}

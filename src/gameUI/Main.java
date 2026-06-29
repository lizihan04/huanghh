package gameUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/main_ui.fxml"));
        Scene scene = new Scene(root,800,600);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args){
        Application.launch(args);
    }
}
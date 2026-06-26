package gameUI;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;


public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Button btOK = new Button("OK");
        Scene scene = new Scene(btOK,200,250);
        stage.setTitle("MyJavaFX");
        stage.setScene(scene);
        stage.show();
    }
    public static void mian(String[] args){
        Application.launch(args);
    }
}

package gameUI.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import java.io.IOException;

public class Map {
    private gameUI.controller.Main main;
    public void setMainController(Main main) {
        this.main = main;
    }
    //森林地图
    @FXML
    public void loadForestMap(ActionEvent event) throws IOException{
        main.loadMapPage("../fxml/forest.fxml");
        closeCurrentWindow(event);
    }
    //岩石区地图
    @FXML
    public void loadRockMap(ActionEvent event) throws IOException{
        main.loadMapPage("../fxml/rock.fxml");
        closeCurrentWindow(event);
    }
    //沙滩地图
    @FXML
    public void loadBeachMap(ActionEvent event) throws IOException{
        main.loadMapPage("../fxml/beach.fxml");
        closeCurrentWindow(event);
    }
    //大海地图
    @FXML
    public void loadSeaMap(ActionEvent event) throws IOException{
        main.loadMapPage("../fxml/sea.fxml");
        closeCurrentWindow(event);
    }
    private void closeCurrentWindow(ActionEvent event){
        Stage stage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
}

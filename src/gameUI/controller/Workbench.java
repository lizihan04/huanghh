package gameUI.controller;

import com.survivalgame.service.Recipe2;
import javafx.fxml.FXML;

public class Workbench {
    private gameUI.controller.Main main;
    private Recipe2 recipe = new Recipe2();
    public void setMainController(Main main) {
        this.main = main;
    }
    @FXML
    public void makeAxe(){
        recipe.axe();
    }
    @FXML
    public void makeShellAxe(){
        recipe.beiRen();
    }
    @FXML
    public void makeHammer(){
        recipe.hammer();
    }
    @FXML
    public void makeDirk(){
        recipe.stoneSword();
    }
    @FXML
    public void makeClub(){
        recipe.woodStick();
    }
    //碎片配方
//    @FXML
//    public void makeFragment(){
//        recipe.axe();
//    }

}

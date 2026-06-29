package gameUI.controller;

import com.survivalgame.service.Recipe2;
import entity.Player2;
import javafx.fxml.FXML;

public class Workbench {
    private Player2 player = Player2.getInstance();
    private gameUI.controller.Main main;
    private Recipe2 recipe = new Recipe2();
    public void setMainController(Main main) {
        this.main = main;
    }
    @FXML
    public void makeAxe(){
        recipe.axe();
        // ai: 合成后刷新背包数据与主界面属性
        com.survivalgame.service.GameLogic.getInstance().bagUpDate();
        if (this.main != null) this.main.refreshUI();
    }
    @FXML
    public void makeShellAxe(){
        recipe.beiRen();
        // ai: 合成后刷新背包数据与主界面属性
        com.survivalgame.service.GameLogic.getInstance().bagUpDate();
        if (this.main != null) this.main.refreshUI();
    }
    @FXML
    public void makeHammer(){
        recipe.hammer();
        // ai: 合成后刷新背包数据与主界面属性
        com.survivalgame.service.GameLogic.getInstance().bagUpDate();
        if (this.main != null) this.main.refreshUI();
    }
    @FXML
    public void makeDirk(){
        recipe.stoneSword();
        // ai: 合成后刷新背包数据与主界面属性
        com.survivalgame.service.GameLogic.getInstance().bagUpDate();
        if (this.main != null) this.main.refreshUI();
    }
    @FXML
    public void makeClub(){
        recipe.woodStick();
        // ai: 合成后刷新背包数据与主界面属性
        com.survivalgame.service.GameLogic.getInstance().bagUpDate();
        if (this.main != null) this.main.refreshUI();
    }
    //碎片配方
    @FXML
    public void makeFragment(){
        recipe.fragment();
        player.setFragment(player.getFragment() + 1);
        com.survivalgame.service.GameLogic.getInstance().bagUpDate();
//        if (this.main != null)
            this.main.refreshUI();
    }

}

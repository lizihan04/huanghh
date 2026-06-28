package gameUI.controller;

import entity.Player2;
import javafx.stage.Stage;

public class DoRest {
    private Player2 player = Player2.getInstance();
    private Main main;
    private Stage currentStage;

    public void setMainController(Main main) {
        this.main = main;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    // 通用方法：进食后刷新+关闭窗口
    private void afterRest() {
        if (main != null) {
            main.refreshUI(); // 刷新主界面属性
        }
        if (currentStage != null) {
            currentStage.close(); // 关闭进食窗口
        }
    }
    public void doRest(){
        if(player.getActionPoint() > 0)
            player.rest();
        afterRest();
    }
}

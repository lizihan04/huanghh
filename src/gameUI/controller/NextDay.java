package gameUI.controller;

import entity.Player2;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class NextDay {
    private Main main;
    private Stage currentStage;
    private Player2 player = Player2.getInstance();

    public void setMainController(Main main) {
        this.main = main;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    private void afterClick() {
        if (main != null) {
            main.refreshUI(); // 刷新主界面属性
        }
        if (currentStage != null) {
            currentStage.close(); // 关闭进食窗口
        }
    }
    @FXML
    public void nextDay(){
        player.next_day();
        afterClick();
    }
}

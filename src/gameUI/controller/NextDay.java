package gameUI.controller;

import com.survivalgame.service.GameLogic;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class NextDay {
    private Main main;
    private Stage currentStage;

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
            currentStage.close(); // 关闭窗口
        }
    }

    @FXML
    public void nextDay(){
        GameLogic.getInstance().nextDay();
        afterClick();
    }
}

package gameUI.controller;

import com.survivalgame.service.GameLogic;
import entity.Player2;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
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
        if (GameLogic.getInstance().gameEnd() && !Player2.getInstance().isGameWin()) {
            showFailureAlert();
        }
        afterClick();
    }

    private void showFailureAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("荒野求生失败");
        alert.setHeaderText(null);
        alert.setContentText("荒野求生失败");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
        Platform.exit();
        System.exit(0);
    }
}

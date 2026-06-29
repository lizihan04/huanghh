package gameUI.controller;

import com.survivalgame.service.GameLogic;
import entity.Player2;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Eat {
    private Main main;
    private Stage currentStage;
    private Player2 player = Player2.getInstance();

    public void setMainController(Main main) {
        this.main = main;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    // 通用方法：进食后刷新+关闭窗口
    private void afterEat() {
        if (main != null) {
            main.refreshUI(); // 刷新主界面属性
        }
        if (GameLogic.getInstance().gameEnd() && !Player2.getInstance().isGameWin()) {
            showFailureAlert();
        }
        if (currentStage != null) {
            currentStage.close(); // 关闭进食窗口
        }
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

    // 确认进食按钮触发的方法
    @FXML
    public void eatCoco() {
        if(player.getActionPoint() > 0)
            player.eatCoco();
        afterEat();
    }
    @FXML
    public void eatFish() {
        if(player.getActionPoint() > 0)
            player.eatFish();
        afterEat();
    }
    @FXML
    public void eatPig() {
        if(player.getActionPoint() > 0)
            player.eatPork();
        afterEat();
    }
    @FXML
    public void eatRabbit() {
        if(player.getActionPoint() > 0)
            player.eatRabbitMeat();
        afterEat();
    }

}
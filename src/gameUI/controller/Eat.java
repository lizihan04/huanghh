package gameUI.controller;

import entity.Food2;
import entity.Item2;
import entity.Player2;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
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


    // 确认进食按钮触发的方法
    @FXML
    public void eatCoco() {
        player.eatCoco();
    }
    @FXML
    public void eatFish() {
        player.eatFish();
    }
    @FXML
    public void eatPig() {
       player.eatPork();
    }
    @FXML
    public void eatRabbit() {
        player.eatRabbitMeat();
    }

}
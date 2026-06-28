package gameUI.controller;

import com.survivalgame.service.GameLogic;
import entity.Player2;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.Node.BASELINE_OFFSET_SAME_AS_HEIGHT;

public class Beach {
    private gameUI.controller.Main main;
    private Player2 player = Player2.getInstance();
    private GameLogic gameLogic = GameLogic.getInstance();
    public void setMainController(Main main) {
        this.main = main;
    }

    public void exploreBeach() throws IOException {
        // 1. 加载界面
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/event.fxml"));
        Parent root = loader.load();

        Event eventController = loader.getController();
        eventController.setMainController(this.main);

        // 2. 执行沙滩探索逻辑（触发属性变化）
        if(player.getActionPoint() > 0)
            eventController.executeExploreLogic("beach");
        // 2. 创建新窗口
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL); //单弹窗操作
        stage.setTitle("提醒");
        stage.show(); // 显示窗口
    }
}

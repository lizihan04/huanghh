package gameUI.controller;

import com.survivalgame.service.GameLogic;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Event {
    @FXML
    private Label words1;
    @FXML
    private Label words2;
    private gameUI.controller.Main main;
    private GameLogic gameLogic = GameLogic.getInstance();
    public void setMainController(Main main) {
        this.main = main;
    }
    public void executeExploreLogic(String areaType) {
        String eventResult = "";
        // 根据探索区域执行对应随机事件
        switch (areaType) {
            case "forest":
                eventResult = gameLogic.forestRandomEvent();
                break;
            case "beach":
                eventResult = gameLogic.beachRandomEvent();
                break;
            case "rocky":
                eventResult = gameLogic.rockyRandomEvent();
                break;
            case "sea":
                eventResult = gameLogic.seaRandomEvent();
                break;
            default:
                eventResult = "无效的探索区域！";
        }
        // 显示事件结果到弹窗的Label
        words1.setText("探索结果");
        words2.setText(eventResult);
        // 关键：执行完逻辑后刷新主页面属性
        if (main != null) {
            main.refreshUI();
        }
    }
}

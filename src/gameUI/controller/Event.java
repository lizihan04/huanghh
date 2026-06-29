package gameUI.controller;

import com.survivalgame.service.GameLogic;
import entity.Item2;
import entity.Player2;
import entity.Tool2;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class Event {
    @FXML
    private Label words1;
    @FXML
    private Label words2;
    @FXML
    private Label weaponTip;
    @FXML
    private HBox weaponHolder;
    @FXML
    private Button handButton;
    @FXML
    private Button axeButton;
    @FXML
    private Button shellAxeButton;
    @FXML
    private Button hammerButton;
    @FXML
    private Button clubButton;
    @FXML
    private Button dirkButton;

    private gameUI.controller.Main main;
    private GameLogic gameLogic = GameLogic.getInstance();

    public void setMainController(Main main) {
        this.main = main;
    }

    public void executeExploreLogic(String areaType) {
        gameLogic.clearCurrentMonster();
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
        if (gameLogic.getCurrentMonster() != null) {
            showWeaponOptions();
        } else {
            hideWeaponOptions();
        }
        // 关键：执行完逻辑后刷新主页面属性
        if (main != null) {
            main.refreshUI();
        }
    }

    public void showMessage(String title, String message) {
        gameLogic.clearCurrentMonster();
        words1.setText(title);
        words2.setText(message);
        hideWeaponOptions();
    }

    private void showWeaponOptions() {
        weaponTip.setVisible(true);
        weaponHolder.setVisible(true);
        handButton.setVisible(true);
        updateWeaponButton(axeButton, "斧头");
        updateWeaponButton(shellAxeButton, "贝刃");
        updateWeaponButton(hammerButton, "锤子");
        updateWeaponButton(clubButton, "木棍");
        updateWeaponButton(dirkButton, "石剑");
    }

    private void hideWeaponOptions() {
        weaponTip.setVisible(false);
        weaponHolder.setVisible(false);
    }

    private void updateWeaponButton(Button button, String weaponName) {
        int count = 0;
        Player2 player = Player2.getInstance();
        Item2[] backpack = player.getBackpackArr();
        for (Item2 item : backpack) {
            if (item instanceof Tool2 && item.getItemName().equals(weaponName)) {
                count = item.getOwnCount();
                break;
            }
        }
        button.setText(weaponName + "(" + count + ")");
        button.setDisable(count <= 0);
    }

    @FXML
    public void handleWeaponSelect(ActionEvent event) {
        String weaponName = ((Button) event.getSource()).getText();
        if (weaponName.contains("(")) {
            weaponName = weaponName.substring(0, weaponName.indexOf("("));
        }
        String battleResult = gameLogic.fightMonsterWithWeapon(weaponName);
        words2.setText(battleResult);
        hideWeaponOptions();
        if (main != null) {
            main.refreshUI();
        }
    }
}

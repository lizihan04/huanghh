package gameUI.controller;
import entity.Item2;
import entity.Player2;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Bag {

    private gameUI.controller.Main main;
    private Player2 player;    // 只声明，不在这里初始化！
    private Item2[] backpackArr;

    // 背包物品数量展示
    @FXML private Label coco;
    @FXML private Label fish;
    @FXML private Label pig;
    @FXML private Label rabbit;
    @FXML private Label mineral;
    @FXML private Label shell;
    @FXML private Label stone;
    @FXML private Label vine;
    @FXML private Label wood;
    @FXML private Label debris;
    @FXML private Label axe;
    @FXML private Label shell_axe;
    @FXML private Label hammer;
    @FXML private Label club;
    @FXML private Label dirk;

    @FXML
    public void initialize(){
        main.refreshUI();
    }

    public void setMainController(Main main) {
        this.main = main;
        this.player = Player2.getInstance();  // 在这里拿才安全！
        this.backpackArr = player.getBackpackArr();
    }

    public void refreshUI() {
        if (backpackArr == null || backpackArr.length < 15) {
            System.out.println("背包数组为空或长度不够！");
            return;
        }

        coco.setText(String.valueOf(backpackArr[0].getOwnCount()));
        fish.setText(String.valueOf(backpackArr[1].getOwnCount()));
        pig.setText(String.valueOf(backpackArr[2].getOwnCount()));
        rabbit.setText(String.valueOf(backpackArr[3].getOwnCount()));
        mineral.setText(String.valueOf(backpackArr[4].getOwnCount()));
        shell.setText(String.valueOf(backpackArr[5].getOwnCount()));
        stone.setText(String.valueOf(backpackArr[6].getOwnCount()));
        vine.setText(String.valueOf(backpackArr[7].getOwnCount()));
        wood.setText(String.valueOf(backpackArr[8].getOwnCount()));
        debris.setText(String.valueOf(backpackArr[9].getOwnCount()));
        axe.setText(String.valueOf(backpackArr[10].getOwnCount()));
        shell_axe.setText(String.valueOf(backpackArr[11].getOwnCount()));
        hammer.setText(String.valueOf(backpackArr[12].getOwnCount()));
        club.setText(String.valueOf(backpackArr[13].getOwnCount()));
        dirk.setText(String.valueOf(backpackArr[14].getOwnCount()));
    }
}
package gameUI.controller;

import entity.Player;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Objects;

public class Main {
    //属性栏
    @FXML
    private Label hpLabel;
    @FXML
    private Label hungerLabel;
    @FXML
    private Label thirstLabel;
    @FXML
    private Label fatigueLabel;
    @FXML
    private Label actionPointLabel;

    private Player player = Player.getInstance();

    //初始化属性
    @FXML
    public void initialize(){
        player.initPlayer();
        refreshUI();
    }
    // 刷新所有属性显示
    public void refreshUI() {
        hpLabel.setText("生命值：" + player.getHp());
        hungerLabel.setText("饥饿值：" + player.getHunger());
        thirstLabel.setText("饥渴值：" + player.getThirst());
        fatigueLabel.setText("疲惫值：" + player.getFatigue());
        actionPointLabel.setText("行动点：" + player.getActionPoint());
    }

    //页面操作
    @FXML
    private ScrollPane mainScrollPane;
    @FXML
    private AnchorPane mapDisplayPane;
    //背包选择器
    @FXML
    public void openBagChooseWindow() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/bag.fxml"));
        Parent root = loader.load();
        Bag bag = loader.getController();
        //把主控制器传给弹窗，实现选中后回调加载地图
        bag.setMainController(this);
        //创建模态弹窗
        Stage chooseStage = new Stage();
        chooseStage.setScene(new Scene(root));
        chooseStage.setTitle("背包");
        chooseStage.initModality(Modality.APPLICATION_MODAL); //单弹窗操作
        chooseStage.setResizable(false);
        chooseStage.showAndWait();
    }
    //工作台
    @FXML
    public void openWorkbenchWindow() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/workbench.fxml"));
        Parent root = loader.load();
        Workbench workbench= loader.getController();
        //把主控制器传给弹窗，实现选中后回调加载地图
        workbench.setMainController(this);
        //创建模态弹窗
        Stage chooseStage = new Stage();
        chooseStage.setScene(new Scene(root));
        chooseStage.setTitle("工作台");
        chooseStage.initModality(Modality.APPLICATION_MODAL); //单弹窗操作
        chooseStage.setResizable(false);
        chooseStage.showAndWait();
    }
    //地图选择器
    @FXML
    public void openMapChooseWindow() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/map.fxml"));
        Parent root = loader.load();
        Map map = loader.getController();
        //把主控制器传给弹窗，实现选中后回调加载地图
        map.setMainController(this);
        //创建模态弹窗
        Stage chooseStage = new Stage();
        chooseStage.setScene(new Scene(root));
        chooseStage.setTitle("选择探索区域");
        chooseStage.initModality(Modality.APPLICATION_MODAL); //单弹窗操作
        chooseStage.setResizable(false);
        chooseStage.showAndWait();
    }
    //对外暴露的方法：切换加载不同地图
    public void loadMapPage(String fxmlPath) throws IOException{
        //清空上一张地图
        mapDisplayPane.getChildren().clear();
        //加载选中的地图FXML
        Parent mapRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
        mapDisplayPane.getChildren().add(mapRoot);
    }
    //休息
    @FXML
    public void openRestWindow() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/rest.fxml"));
        Parent root = loader.load();
        Rest rest = loader.getController();
        //把主控制器传给弹窗，实现选中后回调加载地图
        rest.setMainController(this);
        //创建模态弹窗
        Stage chooseStage = new Stage();
        chooseStage.setScene(new Scene(root));
        chooseStage.setTitle("休息中");
        chooseStage.initModality(Modality.APPLICATION_MODAL); //单弹窗操作
        chooseStage.setResizable(false);
        chooseStage.showAndWait();
    }
    //下一天
    @FXML
    public void openNextDayWindow() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/nextday.fxml"));
        Parent root = loader.load();
        NextDay nextday = loader.getController();
        //把主控制器传给弹窗，实现选中后回调加载地图
        nextday.setMainController(this);
        //创建模态弹窗
        Stage chooseStage = new Stage();
        chooseStage.setScene(new Scene(root));
        chooseStage.setTitle("下一天");
        chooseStage.initModality(Modality.APPLICATION_MODAL); //单弹窗操作
        chooseStage.setResizable(false);
        chooseStage.showAndWait();
    }

    //帮助
    @FXML
    public void openHelpWindow() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/help.fxml"));
        Parent root = loader.load();
        Help help = loader.getController();
        //把主控制器传给弹窗，实现选中后回调加载地图
        help.setMainController(this);
        //创建模态弹窗
        Stage chooseStage = new Stage();
        chooseStage.setScene(new Scene(root));
        chooseStage.setTitle("指引");
        chooseStage.initModality(Modality.APPLICATION_MODAL); //单弹窗操作
        chooseStage.setResizable(false);
        chooseStage.showAndWait();
    }
}
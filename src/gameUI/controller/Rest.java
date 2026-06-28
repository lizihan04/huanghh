package gameUI.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Rest {

    private gameUI.controller.Main main;
    // 移除currentStage：无需传递，改为内部创建新Stage
    private Stage restSubStage;
    private Stage eatSubStage;

    public void setMainController(Main main) {
        this.main = main;
    }

    @FXML
    private AnchorPane mapDisplayPane;

    // 修复doRest方法：创建新的模态窗口
    @FXML
    public void doRest() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/doRest.fxml"));
        Parent root = loader.load();

        // 获取DoRest控制器并执行休息逻辑
        DoRest doRestController = loader.getController();
        doRestController.setMainController(main);

        // 创建新的模态窗口（而非替换原有Stage）
        restSubStage = new Stage();
        doRestController.setCurrentStage(restSubStage);

        restSubStage.setScene(new Scene(root));
        restSubStage.setTitle("休息中！");
        restSubStage.initModality(Modality.APPLICATION_MODAL); // 模态属性在show前设置
        restSubStage.setResizable(false);
        restSubStage.showAndWait();

        // 休息后刷新主界面属性
        if (main != null) {
            main.refreshUI();
        }
    }

    // 修复openEatWindow方法：创建新的模态窗口
    @FXML
    public void openEatWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/eat.fxml"));
        Parent root = loader.load();

        Eat eatController = loader.getController();
        eatController.setMainController(main);
        // 给Eat控制器传递新创建的Stage（而非原有Stage）
        eatSubStage = new Stage();
        eatController.setCurrentStage(eatSubStage);

        eatSubStage.setScene(new Scene(root));
        eatSubStage.setTitle("吃吃吃！");
        eatSubStage.initModality(Modality.APPLICATION_MODAL);
        eatSubStage.setResizable(false);
        eatSubStage.showAndWait();

        // 进食后刷新主界面属性
        if (main != null) {
            main.refreshUI();
        }
    }

//    private gameUI.controller.Main main;
//    private Stage currentStage;
//
//    public void setMainController(Main main) {
//        this.main = main;
//    }
//
//    // 注入当前窗口的Stage（需要在fxml中对应配置，或通过按钮事件获取）
//    public void setCurrentStage(Stage currentStage) {
//        this.currentStage = currentStage;
//    }
//
//    @FXML
//    private AnchorPane mapDisplayPane;
//
//    //rest bottom
//    @FXML
//    public void doRest() throws IOException{
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/doRest.fxml"));
//        Parent root = loader.load();
//        // 替换当前窗口的Scene为doRest.fxml的内容
//        currentStage.setScene(new Scene(root));
//        currentStage.setTitle("休息中！"); // 修改窗口标题
//        currentStage.initModality(Modality.APPLICATION_MODAL);
//    }
//    //eat bottom
//    @FXML
//    public void openEatWindow() throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/eat.fxml"));
//        Parent root = loader.load();
//
//        Eat eatController = loader.getController();
//        eatController.setMainController(main);
//        eatController.setCurrentStage(currentStage);
//
//        // 替换当前窗口的Scene为eat.fxml的内容
//        currentStage.setScene(new Scene(root));
//        currentStage.setTitle("吃吃吃！"); // 修改窗口标题
//        currentStage.initModality(Modality.APPLICATION_MODAL);
//    }

}

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
    private Stage currentStage;

    public void setMainController(Main main) {
        this.main = main;
    }

    // 注入当前窗口的Stage（需要在fxml中对应配置，或通过按钮事件获取）
    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    @FXML
    private AnchorPane mapDisplayPane;

    //rest bottom
    @FXML
    public void doRest() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/doRest.fxml"));
        Parent root = loader.load();
        // 替换当前窗口的Scene为doRest.fxml的内容
        currentStage.setScene(new Scene(root));
        currentStage.setTitle("休息中！"); // 修改窗口标题
        currentStage.initModality(Modality.APPLICATION_MODAL);
    }
    //eat bottom
    @FXML
    public void openEatWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/eat.fxml"));
        Parent root = loader.load();
        // 替换当前窗口的Scene为eat.fxml的内容
        currentStage.setScene(new Scene(root));
        currentStage.setTitle("吃吃吃！"); // 修改窗口标题
        currentStage.initModality(Modality.APPLICATION_MODAL);
    }

}

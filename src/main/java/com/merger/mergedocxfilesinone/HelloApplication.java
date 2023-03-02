package com.merger.mergedocxfilesinone;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        HelloController controller = fxmlLoader.getController();
        controller.setStage(stage);
        stage.setTitle("Hello!");
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        stage.setX(Screen.getPrimary().getBounds().getWidth()/2 - stage.getWidth()/2);
        stage.setY(Screen.getPrimary().getBounds().getHeight()/2 - stage.getHeight()/2*1.8);
//                                                        640-548/2=640-250=390

    }


    public static void main(String[] args) {
        launch();
    }
}
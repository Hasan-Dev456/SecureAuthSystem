package com.secureauth;

import com.secureauth.ui.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(LoginView.create(stage), 400, 450);

        scene.getStylesheets().add(
                getClass().getResource("/com/secureauth/styles/style.css").toExternalForm()
        );

        stage.setTitle("Secure Auth System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
package org.downloader.instadownloader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class InstaDownloader extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InstaDownloader.class.getResource("interface.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("InstaDownloader");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
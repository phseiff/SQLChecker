package de.unifrankfurt.dbis;

import de.unifrankfurt.dbis.GUI.ExceptionAlert;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


public class GUIApp extends Application {
    private static Stage primaryStage;
    private static HostServices hostServices;


    @Override
    public void start(Stage primaryStage) throws IOException {
        GUIApp.primaryStage = primaryStage;
        GUIApp.hostServices = getHostServices();
        Thread.currentThread().setUncaughtExceptionHandler(GUIApp::showError);


        URL fxml = getClass().getResource("/mainPane.fxml");
        Parent root = FXMLLoader.load(fxml);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.getIcons().add(
                new Image(
                        getClass().getResourceAsStream("/images/sql-icon.png")));

        primaryStage.show();
    }

    private static void showError(Thread t, Throwable e) {
        if (Platform.isFxApplicationThread()) {
            ExceptionAlert alert = new ExceptionAlert(e);
            alert.showAndWait();
        } else {
            e.printStackTrace();

        }
    }

    public static Stage getPrimaryStage() {
        return GUIApp.primaryStage;
    }

    public static HostServices getHostServicesStatic() {
        return hostServices;
    }
}
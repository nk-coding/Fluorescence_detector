package frontend;

import frontend.views.*;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Main extends Application {
    private final static int INITIAL_WIDTH = 800;
    private final static int INITIAL_HEIGHT = 600;
    private MainController controller;

    @Override
    public void start(Stage primaryStage){
        primaryStage.setWidth(INITIAL_WIDTH);
        primaryStage.setHeight(INITIAL_HEIGHT);
        controller = new MainController();
        controller.getScene().getStylesheets().add("resources/style.css");
        primaryStage.setScene(controller.getScene());
        //ChangingLeftBox lf = new ChangingLeftBox();
        //primaryStage.setScene(new Scene(lf));
        //new Thread(lf).start();
        primaryStage.show();

    }

    @Override
    public void stop(){
        controller.stop();
        System.exit(0);
    }

    public static void createExceptionWindow(Exception e){
        Alert result = new Alert(Alert.AlertType.ERROR);
        result.setTitle("An unexpected error has ocurred");
        result.setContentText(e.getMessage());
        result.showAndWait();
    }

    public static void main(String[] args){
        launch(args);
    }
}

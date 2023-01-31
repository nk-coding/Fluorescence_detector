package frontend.views;

import backend.experiment.Experiment;
import backend.experiment.ExperimentConcentration;
import frontend.Main;
import frontend.views.components.CancelButton;
import frontend.views.components.ParamConfView;
import frontend.views.components.ViewsEnum;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import jssc.SerialPortException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class ModeMenu extends VBox {
    //ImageViews instead of buttons because the former is easier to style, IMO.
    protected ImageView startButton;
    protected CancelButton cancelButton;
    protected ImageView leftImage;
    protected ImageView directoryButton;
    protected ImageView savePrefButton;

    private boolean isPathSelected = false;
    protected ViewsEnum experimentView;
    protected String directoryPath;


    public ModeMenu(MainController controller){
        experimentView = ViewsEnum.CONCETRATION_VIEW;
        startButton = new ImageView(ImagesClass.getSTART());
        startButton.setPreserveRatio(true);
        startButton.setFitHeight(40);

        startButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
            if(isPathSelected){
                try{
                    this.startExperiment(controller);
                    controller.startChangingBox();
                } catch (NumberFormatException exp){
                    //Show error
                    Alert formattingErr = new Alert(Alert.AlertType.ERROR);
                    formattingErr.setTitle("Error");
                    String errmsg = "Error parsing the values. Make sure that: \n\n" +
                            "All values are positive numbers. \n\n" +
                            "All configuration values are integer. \n\n" +
                            "Time between values is between 0 and 127. \n\n" +
                            "Values per point is between 0 and 127. \n\n" +
                            "Delay on is between 0 and 16777215. \n\n" +
                            "Delay off is between 0 and 16383.";
                    formattingErr.setContentText(errmsg);
                    formattingErr.showAndWait();
                }
            } else{
                Alert noDirAlert = new Alert(Alert.AlertType.ERROR);
                noDirAlert.setTitle("Error");
                noDirAlert.setContentText("Please select a directory");
                noDirAlert.showAndWait();
            }

        });

        cancelButton = new CancelButton(controller,40);


        leftImage = new ImageView(ImagesClass.getMeasureFilled());
        leftImage.setPreserveRatio(true);
        leftImage.setFitHeight(120);


        directoryButton = new ImageView(ImagesClass.getDIRECTORY());
        directoryButton.setPreserveRatio(true);
        directoryButton.setFitHeight(40);

        savePrefButton = new ImageView(ImagesClass.getSAVEPREF());
        savePrefButton.setPreserveRatio(true);
        savePrefButton.setFitHeight(40);
    }

    private void initInitialPath(Label dirLabel) {
        String potentialPath = System.getProperty("user.dir");
        if (Files.exists(Paths.get(potentialPath))) {
            directoryPath = potentialPath;
            dirLabel.setText(potentialPath);
            isPathSelected = true;
        }
    }

    protected abstract void startExperiment(MainController controller) throws NumberFormatException;

    protected void init(ParamConfView param){
        this.setAlignment(Pos.CENTER);
        HBox container = new HBox();


        VBox leftBox = new VBox(leftImage);
        leftBox.getStyleClass().add("leftBox");
        HBox.setHgrow(leftBox, Priority.SOMETIMES);
        leftBox.setAlignment(Pos.CENTER);

        VBox rightBox = new VBox();
        rightBox.setMaxWidth(400);
        rightBox.getStyleClass().add("rightBox");
        HBox.setHgrow(rightBox, Priority.SOMETIMES);
        rightBox.setAlignment(Pos.CENTER);

        HBox directoryBox = new HBox();
        directoryBox.setAlignment(Pos.CENTER);

        VBox directoryButtonBox = new VBox(directoryButton);
        directoryButtonBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(directoryButtonBox,Priority.SOMETIMES);
        Label dirLabel = new Label("Select a directory");
        initInitialPath(dirLabel);
        VBox temp = new VBox(dirLabel);
        temp.setAlignment(Pos.CENTER_LEFT);
        temp.setMaxWidth(200);
        HBox.setHgrow(temp,Priority.SOMETIMES);
        directoryBox.getChildren().addAll(directoryButtonBox,temp);

        directoryButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
            DirectoryChooser dirChoser = new DirectoryChooser();
            File path =  dirChoser.showDialog(new Stage());
            if(!(path == null)){
                dirLabel.setText(path.getPath());
                directoryPath = path.getPath();
                isPathSelected = true;
            }
        });


        HBox buttonsBox = new HBox();
        VBox.setVgrow(buttonsBox,Priority.SOMETIMES);

        VBox startBox = new VBox(startButton);
        startBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(startBox,Priority.SOMETIMES);

        VBox saveBox = new VBox(savePrefButton);
        saveBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(saveBox,Priority.SOMETIMES);

        savePrefButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
            param.savePref();
        });

        VBox cancelBox = new VBox(cancelButton);
        cancelBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(cancelBox,Priority.SOMETIMES);

        buttonsBox.getChildren().addAll(startBox,saveBox,cancelBox);

        VBox gridBox = new VBox(param);
        gridBox.setAlignment(Pos.CENTER);
        rightBox.getChildren().addAll(directoryBox,gridBox,buttonsBox);

        container.getChildren().addAll(leftBox,rightBox);
        this.getChildren().add(container);
    }

    public String getDirectoryPath() {
        return directoryPath;
    }
}

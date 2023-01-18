package frontend.views.components;

import backend.experiment.Configuration;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class ParamConfView extends GridPane {

    protected static final String RESISTANCE = "Resistance";
    //protected static final String GAMMA = "Gamma";
    //protected static final String DEV_GAMMA = "Gamma's deviation";
    protected static final String TIME_BETWEEEN_VALUES = "Time between values";
    protected static final String VALUES_PER_POINT = "Values per point";
    protected static final String DELAY_ON = "Delay on";
    protected static final String DELAY_OFF = "Delay off";

    protected PrefObject prefs;
    protected String filePath;


    protected TextField resistance;
    //protected TextField gamma;
    //protected TextField devGamma;

    protected TextField timeBetV;
    protected TextField valPerPoint;
    protected TextField delayOn;
    protected TextField delayOff;

    public ParamConfView(){

        try{

            Path path = Paths.get(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
            filePath = path.toString() + "/preferences.ser";
            if(!Files.exists(Paths.get(filePath))){
                FileOutputStream fileOut = new FileOutputStream(filePath);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(PrefObject.getDefault());
                out.close();
                fileOut.close();
            }

            FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            prefs = (PrefObject) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException | URISyntaxException e) {
            prefs = PrefObject.getDefault();
        }

        this.getStyleClass().add("param");
        this.setMaxWidth(300);

        Label paramLabel = new Label("Parameters");
        paramLabel.getStyleClass().add("header");
        this.add(paramLabel,0,0);


        Label configLabel = new Label("Configuration");
        configLabel.getStyleClass().add("header");
        this.add(configLabel,1,0);

        BorderPane resistancePane = this.createInputField(RESISTANCE,prefs.getResistance());
        resistance = (TextField) resistancePane.getRight();
        this.add(resistancePane,0,1);

        BorderPane timeBetVPane = this.createInputField(TIME_BETWEEEN_VALUES,prefs.getTimeBetweenValues());
        timeBetV = (TextField) timeBetVPane.getRight();
        this.add(timeBetVPane,1,1);

        BorderPane valPerPointPane = this.createInputField(VALUES_PER_POINT,prefs.getValuesPerPoint());
        valPerPoint = (TextField) valPerPointPane.getRight();
        this.add(valPerPointPane,1,2);

        BorderPane delayOnPane = this.createInputField(DELAY_ON,prefs.getDelayOn());
        delayOn = (TextField) delayOnPane.getRight();
        this.add(delayOnPane,1,3);

        BorderPane delayOffPane = this.createInputField(DELAY_OFF,prefs.getDelayOff());
        delayOff = (TextField) delayOffPane.getRight();
        this.add(delayOffPane,1,4);

    }

    public Configuration getConfiguration() throws NumberFormatException{
        byte timeBetV = Byte.parseByte(this.timeBetV.getText());
        byte valPerPoint = Byte.parseByte(this.valPerPoint.getText());
        int delayOn = Integer.parseInt(this.delayOn.getText());
        int delayOff = Integer.parseInt(this.delayOff.getText());

        return new Configuration(timeBetV,valPerPoint,delayOn,delayOff);
    }

    protected void updatePref() throws NumberFormatException{
        prefs.setResistance(Double.parseDouble(this.resistance.getText()));

        prefs.setTimeBetweenValues(Byte.parseByte(this.timeBetV.getText()));
        prefs.setValuesPerPoint(Byte.parseByte(this.valPerPoint.getText()));
        prefs.setDelayOn(Integer.parseInt(this.delayOn.getText()));
        prefs.setDelayOff(Integer.parseInt(this.delayOff.getText()));
    };


    public void savePref(){
        this.updatePref();
        try{
            FileOutputStream fileOut = new FileOutputStream(filePath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(prefs);
            out.close();
            fileOut.close();
            Alert prefsSaved = new Alert(Alert.AlertType.INFORMATION);
            prefsSaved.setHeaderText("Preferences saved successfully");
            prefsSaved.showAndWait();
        } catch (IOException e){
            Alert problemWhileSavingAlert = new Alert(Alert.AlertType.ERROR);
            problemWhileSavingAlert.setTitle("Error");
            problemWhileSavingAlert.setContentText("An error occurred while trying to save the preferences: \n"
                    +e.toString());
            problemWhileSavingAlert.show();
        }
    }

    protected BorderPane createInputField(String name, double defaultVal){
        Label label = new Label(name+": ");
        TextField field = (int) defaultVal == defaultVal ? new TextField(Integer.toString((int) defaultVal)) :
                new TextField(Double.toString(defaultVal));
        field.setMaxWidth(75);
        BorderPane result = new BorderPane();
        result.setLeft(label);
        result.setRight(field);
        result.getStyleClass().add("inputPair");
        return result;
    }
}

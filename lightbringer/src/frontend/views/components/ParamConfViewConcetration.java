package frontend.views.components;

import backend.experiment.Configuration;
import backend.experiment.ParametersConcExperiment;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class ParamConfViewConcetration extends ParamConfView{

    protected static final String BLANK_RESISTANCE = "Resistance of blank";
    protected static final String BLANK_DEV_RESISTANCE = "Deviation of the blank's resistance";
    protected static final String GAMMA = "Gamma";
    protected static final String DEV_GAMMA = "Gamma's deviation";
    protected static final String K = "k";
    protected static final String DEV_K = "k's deviation";
    protected static final String NUMBER_OF_MEASUREMENTS = "Number of measurements";
    protected static final String TIME_BETWEEN_MEASUREMENTS = "Time between measurements";

    protected TextField gamma;
    protected TextField devGamma;

    protected TextField k;
    protected TextField devK;

    protected TextField blankResistance;
    protected TextField blankDevResistance;

    protected TextField numberMes;
    protected TextField timeBetM;

    public ParamConfViewConcetration(){

        BorderPane gammaPane = this.createInputField(GAMMA,prefs.getGamma());
        gamma = (TextField) gammaPane.getRight();
        this.add(gammaPane,0,2);

        BorderPane devGammaPane = this.createInputField(DEV_GAMMA,prefs.getDevGamma());
        devGamma= (TextField) devGammaPane.getRight();
        this.add(devGammaPane,0,3);

        BorderPane kPane = this.createInputField(K,prefs.getK());
        k = (TextField) kPane.getRight();
        this.add(kPane,0,4);


        BorderPane devKPane = this.createInputField(DEV_K,prefs.getDevK());
        devK = (TextField) devKPane.getRight();
        this.add(devKPane,0,5);

        BorderPane blankResistancePane = this.createInputField(BLANK_RESISTANCE,prefs.getBlankResistance());
        blankResistance = (TextField) blankResistancePane.getRight();
        this.add(blankResistancePane,0,6);

        BorderPane blankDevResistancePane = this.createInputField(BLANK_DEV_RESISTANCE,prefs.getBlankDevResistance());
        blankDevResistance = (TextField) blankDevResistancePane.getRight();
        this.add(blankDevResistancePane,0,7);

        BorderPane numberMesPane = this.createInputField(NUMBER_OF_MEASUREMENTS,prefs.getNumberOfMeasurements());
        numberMes = (TextField) numberMesPane.getRight();
        this.add(numberMesPane,1,5);

        BorderPane timeBetMPane = this.createInputField(TIME_BETWEEN_MEASUREMENTS,prefs.getTimeBetweenMeasurements());
        timeBetM = (TextField) timeBetMPane.getRight();
        this.add(timeBetMPane,1,6);


    }

    public ParametersConcExperiment getParameters() throws NumberFormatException{
        double resistance = Double.parseDouble(this.resistance.getText());
        double gamma = Double.parseDouble(this.gamma.getText());
        double devGamma = Double.parseDouble(this.devGamma.getText());
        double k = Double.parseDouble(this.k.getText());
        double devK = Double.parseDouble(this.devK.getText());
        double blankResistance = Double.parseDouble(this.blankResistance.getText());
        double blankDevResistance = Double.parseDouble(this.blankDevResistance.getText());
        return new ParametersConcExperiment(resistance,blankResistance,blankDevResistance,k,devK,gamma,devGamma);
    }

    @Override
    public Configuration getConfiguration() throws NumberFormatException{
        Configuration result = super.getConfiguration();
        result.setDelayBetweenMeasurements(Integer.parseInt(this.timeBetM.getText()));
        result.setNumberOfMeasurments(Integer.parseInt(this.numberMes.getText()));
        return result;
    }

    @Override
    protected void updatePref(){
        super.updatePref();

        prefs.setBlankResistance(Double.parseDouble(this.blankResistance.getText()));
        prefs.setBlankDevResistance(Double.parseDouble(this.blankDevResistance.getText()));
        prefs.setGamma(Double.parseDouble(this.gamma.getText()));
        prefs.setDevGamma(Double.parseDouble(this.devGamma.getText()));
        prefs.setK(Double.parseDouble(this.k.getText()));
        prefs.setDevK(Double.parseDouble(this.devK.getText()));

        prefs.setNumberOfMeasurements(Integer.parseInt(this.numberMes.getText()));
        prefs.setTimeBetweenMeasurements(Integer.parseInt(this.timeBetM.getText()));
    }
}

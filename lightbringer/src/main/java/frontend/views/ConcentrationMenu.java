package frontend.views;

import backend.experiment.ExperimentCalibrate;
import backend.experiment.ExperimentConcentration;
import backend.experiment.listener.LightBringerNotFoundException;
import frontend.Main;
import frontend.views.components.ParamConfViewConcetration;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import jssc.SerialPortException;

public class ConcentrationMenu extends ModeMenu {
    ParamConfViewConcetration pcView;

    public ConcentrationMenu(MainController controller){
        super(controller);

        pcView = new ParamConfViewConcetration();
        this.init(pcView);

    }

    @Override
    protected void startExperiment(MainController controller) {
        try{
            ExperimentConcentration exp = new ExperimentConcentration(this.pcView.getConfiguration(),this.pcView.getParameters());
            controller.getConcentrationView().startChanging(exp);
            controller.appClosedProperty().addListener(e -> {
                try{
                    exp.disconnectArduino();
                } catch (SerialPortException e1) {
                    Main.createExceptionWindow(e1);
                }
            });
            exp.start();
            ConcentrationView view = controller.getConcentrationView();
            view.setExperiment(exp);
            view.setDirPath(this.getDirectoryPath());
            view.updateFileName();
            controller.changeView(experimentView);
        } catch (LightBringerNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lightbringer communication error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (SerialPortException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Serial error");
            alert.setContentText("There was an unexpected error regarding the SerialPort. Make sure Lightbringer is connected" +
                    "and the port is not being used.");
            alert.showAndWait();
        }
    }
}

package frontend.views;

import backend.experiment.ExperimentCalibrate;
import backend.experiment.listener.LightBringerNotFoundException;
import frontend.Main;
import frontend.views.components.ParamConfViewCalibrate;
import frontend.views.components.ViewsEnum;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import jssc.SerialPortException;

public class CalibrateMenu extends ModeMenu{
    ParamConfViewCalibrate pcView;

    public CalibrateMenu(MainController controller){
        super(controller);
        this.experimentView = ViewsEnum.CALIBRATION_VIEW;

        this.leftImage.setImage(ImagesClass.getCalibrateFilled());

        pcView = new ParamConfViewCalibrate();
        this.init(pcView);


    }

    @Override
    protected void startExperiment(MainController controller) throws NumberFormatException{
        try{
            ExperimentCalibrate exp = new ExperimentCalibrate(pcView.getConfiguration(),pcView.getParameters());
            controller.getCalibrateView().startChanging(exp);
            controller.appClosedProperty().addListener(e -> {
                try{
                    System.out.println("adios");
                    exp.disconnectArduino();
                } catch (SerialPortException e1) {
                    Main.createExceptionWindow(e1);
                }
            });
            exp.start();
            CalibrateView view = controller.getCalibrateView();
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

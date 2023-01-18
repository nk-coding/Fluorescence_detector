package frontend.views;

import backend.experiment.Experiment;
import backend.experiment.ExperimentCalibrate;
import backend.experiment.StateExperiment;
import backend.measurement.MeasurementCalibration;
import frontend.Main;
import frontend.views.components.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class CalibrateView extends ExperimentView {

    private ImageView currentLeftImgView;

    //private ExperimentCalibrate exp;

    public CalibrateView(MainController controller, ChangingLeftBox leftBox){
        super(controller,leftBox);
        this.setAlignment(Pos.CENTER);

        currentLeftImgView = new ImageView(ImagesClass.getLoading0());
        currentLeftImgView.setPreserveRatio(true);
        currentLeftImgView.setFitHeight(300);

        //created here for adding the event
        TextField concetrationField = new TextField();
        measureButton.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e ->{

            try {
                if(exp.isRdyNextMeasurement()){
                    double conc = Double.parseDouble(concetrationField.getText());
                    Thread thread = new Thread( () ->{
                        try {
                            ((ExperimentCalibrate) exp).measure(conc);
                        }  catch (Exception e1){
                            Platform.runLater( () ->{
                                Main.createExceptionWindow(e1);
                            });
                        }
                    });
                    thread.start();
                    System.out.println("adios");
                } else{

                    Alert warning = new Alert(Alert.AlertType.WARNING);
                    warning.setContentText("Lightbriger currently busy");
                    warning.show();
                }
            } catch (NumberFormatException e1) {
                Alert formattingErr = new Alert(Alert.AlertType.ERROR);
                formattingErr.setTitle("Error");
                String errmsg = "Error parsing the value. Make sure that the text is a number";
                formattingErr.setContentText(errmsg);
                formattingErr.showAndWait();
            }
        });

        graphButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
            if(!controller.getPlotView().isUpdated()){
                controller.getPlotView().setIsUpdated(true);
                controller.getPlotView().initChartCalibration(exp.getMeasurementsList());
            }
            controller.changeView(ViewsEnum.PLOT_VIEW);
        });

        tableButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
            if(!controller.getTableView().isUpdated()){
                controller.getTableView().setIsUpdated(true);
                controller.getTableView().initTableCalibration(exp.getMeasurementsList());
                //controller.getTableView().setChangingLeft(this.leftBox);
            }
            controller.changeView(ViewsEnum.TABLE_VIEW);
        });


        container = new HBox();

        VBox rightBox = new VBox();
        HBox.setHgrow(rightBox, Priority.SOMETIMES);
        rightBox.setAlignment(Pos.CENTER);


        VBox valueBox = new VBox();
        valueBox.setAlignment(Pos.BOTTOM_CENTER);
        VBox.setVgrow(valueBox,Priority.SOMETIMES);

        HBox labelField = new HBox();
        labelField.setAlignment(Pos.CENTER);
        labelField.setSpacing(5);
        labelField.getChildren().addAll(new Label("Concentration (nM):"),concetrationField);

        VBox measureBox = new VBox();
        measureBox.getStyleClass().add("padding25");
        //VBox.setVgrow(measureBox,Priority.SOMETIMES);
        measureBox.setAlignment(Pos.CENTER);
        measureBox.getChildren().addAll(measureButton);
        valueBox.getChildren().addAll(labelField,measureBox);



        HBox buttonsBox = new HBox();
        //VBox.setVgrow(buttonsBox,Priority.SOMETIMES);

        VBox tableBox = new VBox(tableButton);
        tableBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(tableBox,Priority.SOMETIMES);

        VBox graphBox = new VBox(graphButton);
        graphBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(graphBox,Priority.SOMETIMES);

        VBox cancelBox = new VBox(cancelButton);
        cancelBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(cancelBox,Priority.SOMETIMES);

        buttonsBox.getChildren().addAll(tableBox,graphBox,cancelBox);

        rightBox.getChildren().addAll(valueBox, buttonsBox);

        container.getChildren().addAll(rightBox);
        this.getChildren().add(container);
    }


    @Override
    public void updateFileName(){
        super.updateFileName();
        this.fileName = "Calibration_"+this.fileName;
    }

    @Override
    protected String helpGetHeading(String result) {
        result = "Concentration (nm)" + result;
        result += "Resistance (Ohm), Resistance Standard Error (Ohm)";
        return result;
    }

    @Override
    public void startChanging(Experiment exp) {
        ChangeListener<StateExperiment> listener= (observable, oldValue, newValue) -> {
            switch (newValue){
                case MEASURING:
                    this.leftBox.changeState(ChangingLeftBox.State.RUNNING);
                    break;
                case READY:
                    this.leftBox.changeState(ChangingLeftBox.State.READY_MEASURE);
                    break;
            }
        };
        exp.getPropertyCurrState().addListener(listener);
        cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
            this.leftBox.changeState(ChangingLeftBox.State.KILL);
            exp.getPropertyCurrState().removeListener(listener);
        });
    }

}

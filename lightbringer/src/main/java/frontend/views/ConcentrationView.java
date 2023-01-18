package frontend.views;

import backend.experiment.Experiment;
import backend.experiment.ExperimentConcentration;
import backend.experiment.StateExperiment;
import backend.measurement.MeasurementCalibration;
import frontend.Main;
import frontend.views.components.ChangingLeftBox;
import frontend.views.components.ViewsEnum;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ConcentrationView extends ExperimentView {

    private ImageView currentLeftImgView;


    double heightButtons = 80;
    //private ExperimentConcentration exp;

    public ConcentrationView(MainController controller, ChangingLeftBox leftBox){
        super(controller,leftBox);
        this.setAlignment(Pos.CENTER);

        currentLeftImgView = new ImageView(ImagesClass.getLoading0());
        currentLeftImgView.setPreserveRatio(true);
        currentLeftImgView.setFitHeight(300);

        measureButton.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e ->{
            try {
                if(exp.isRdyNextMeasurement()){
                    ExperimentConcentration expC = ((ExperimentConcentration) exp);
                    if(expC.isLoopRunning()){
                        Alert warning = new Alert(Alert.AlertType.WARNING);
                        warning.setContentText("Experiment already running");
                        warning.show();
                    } else{
                        Thread thread = new Thread( () ->{
                            try {
                                expC.measureLoop();
                            } catch (Exception e1) {
                                Platform.runLater(() ->{
                                    Main.createExceptionWindow(e1);
                                });
                            }
                        });
                        thread.start();
                    };
                    /*if(!(expC.getBlank() == null)){
                        if(expC.isLoopRunning()){
                            Alert warning = new Alert(Alert.AlertType.WARNING);
                            warning.setContentText("Experiment already running");
                            warning.show();
                        } else{
                            Thread thread = new Thread( () ->{
                                try {
                                    expC.measureLoop();
                                } catch (Exception e1) {
                                    Platform.runLater(() ->{
                                        Main.createExceptionWindow(e1);
                                    });
                                }
                            });
                            thread.start();
                        };
                    } else{
                        expC.measureBlank();
                    }*/
                } else{
                    Alert warning = new Alert(Alert.AlertType.WARNING);
                    warning.setContentText("Lightbriger currently busy");
                    warning.show();
                }
            } catch (Exception e1) {
                Main.createExceptionWindow(e1);
            }
        });

        graphButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
            if(!controller.getPlotView().isUpdated()){
                controller.getPlotView().setIsUpdated(true);
                controller.getPlotView().initChartConcentration(exp.getMeasurementsList());
            }
            controller.changeView(ViewsEnum.PLOT_VIEW);
        });

        tableButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
            if(!controller.getTableView().isUpdated()){
                controller.getTableView().setIsUpdated(true);
                controller.getTableView().initTableConcentration(exp.getMeasurementsList());
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


        VBox measureBox = new VBox();
        measureBox.getStyleClass().add("padding25");
        measureBox.setAlignment(Pos.CENTER);
        measureBox.getChildren().addAll(measureButton);
        valueBox.getChildren().addAll(measureBox);



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
        this.fileName = "ConcetrationExp_"+this.fileName;
    }

    @Override
    protected String helpGetHeading(String result) {
        result ="Time (min)," + result;
        result += ", Resistance (Ohm), Resistance Standard Error (Ohm), Concentration (nM), Concentration Standard Error (nM)" + "\n";
        ExperimentConcentration experimentConcentration = (ExperimentConcentration) this.exp;
        //result += experimentConcentration.getBlank().toCSVString();
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
                    if(!this.leftBox.isMeasureReached()){
                        this.leftBox.changeState(ChangingLeftBox.State.READY_MEASURE);
                    }
                    break;
                case DONE:
                    this.leftBox.changeState(ChangingLeftBox.State.DONE);
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

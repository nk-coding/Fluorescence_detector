package frontend.views;

import backend.experiment.Experiment;
import backend.measurement.Measurement;
import backend.measurement.MeasurementCalibration;
import frontend.Main;
import frontend.views.components.CancelButton;
import frontend.views.components.ChangingLeft;
import frontend.views.components.ChangingLeftBox;
import frontend.views.components.ViewsEnum;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jssc.SerialPortException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class ExperimentView  extends VBox implements ChangingLeft {

    protected Experiment exp;

    protected double heightButtons = 80;

    protected ChangingLeftBox leftBox;
    protected CancelButton cancelButton;
    protected ImageView measureButton;
    protected ImageView tableButton;
    protected ImageView graphButton;
    HBox container;


    protected String dirPath;
    protected String fileName;

    public ExperimentView(MainController controller, ChangingLeftBox leftBox){
        this.leftBox = leftBox;
        this.dirPath = dirPath;
        cancelButton = new CancelButton(controller,heightButtons);
        cancelButton.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e ->{
            try {
                exp.disconnectArduino();
                controller.getTableView().setIsUpdated(false);
                controller.getPlotView().setIsUpdated(false);
            } catch (SerialPortException e1) {
                Main.createExceptionWindow(e1);
            }
        });

        tableButton = new ImageView(ImagesClass.getTABLE());
        tableButton.setPreserveRatio(true);
        tableButton.setFitHeight(heightButtons);

        graphButton= new ImageView(ImagesClass.getGRAPH());
        graphButton.setPreserveRatio(true);
        graphButton.setFitHeight(heightButtons);

        measureButton = new ImageView(ImagesClass.getMeasureEmpty());
        measureButton.setPreserveRatio(true);
        measureButton.setFitHeight(100);

        measureButton.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_PRESSED, e ->{
            measureButton.setImage(ImagesClass.getMeasureFilled());
        });
        measureButton.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_RELEASED, e ->{
            measureButton.setImage(ImagesClass.getMeasureEmpty());
        });

    }

    public void setExperiment(Experiment exp){
        this.exp = exp;
        this.exp.getMeasurementsList().addListener( (ListChangeListener<Measurement>) (e ->{
            e.next();
            List<? extends Measurement> temp = e.getAddedSubList();
            try {
                this.writeData(temp);
            } catch (IOException e1) {
                Main.createExceptionWindow(e1);
            }
        }));
    };

    public Experiment getExp() {
        return exp;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public void updateFileName(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date date = new Date();
        this.fileName = "Lightbringer_"+dateFormat.format(date)+".csv";
    }

    private void writeData(List<? extends Measurement> list) throws IOException {
        String filePath = dirPath+"\\" + fileName;
        File file = new File(filePath);
        boolean didExist = file.exists();
        BufferedWriter bf = new BufferedWriter(new FileWriter(file,true));
        System.out.println("writing to" + filePath);
        if(!didExist){
            bf.append(this.getHeading());
            bf.newLine();
        }
        for(Measurement mes : list){
            bf.append(mes.toCSVString());
            bf.newLine();
        }
        bf.close();
    }

    private String getHeading(){
        String result = "";
        for(int i = 0; i < this.exp.getConf().getNumberOfValuesPerMeasurement(); i++){
            result += i+",";
        }
        result += "Average,Standard Error";
        result = this.helpGetHeading(result);
        return result;
    };

    protected abstract String helpGetHeading(String result);

    public abstract void startChanging(Experiment exp);

    @Override
    public void setChangingLeft(ChangingLeftBox leftBox) {
        if(!this.container.getChildren().contains(leftBox)){
            this.container.getChildren().add(0,leftBox);
        }
    }
}

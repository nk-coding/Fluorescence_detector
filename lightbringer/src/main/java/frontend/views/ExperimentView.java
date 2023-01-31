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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        Path filePath = Paths.get(dirPath, fileName);
        boolean didExist = Files.exists(filePath);
        StringBuilder csvBuilder = new StringBuilder();
        if (didExist) {
            Files.readAllLines(filePath).forEach(line -> {
                csvBuilder.append(line);
                csvBuilder.append(System.lineSeparator());
            });
        }
        System.out.println("writing to" + filePath);
        if(!didExist){
            csvBuilder.append(this.getHeading());
            csvBuilder.append(System.lineSeparator());
        }
        for(Measurement mes : list){
            csvBuilder.append(mes.toCSVString());
            csvBuilder.append(System.lineSeparator());
        }
        String csvString = csvBuilder.toString();
        Files.writeString(filePath, csvString);
        writeAsXlsx(csvString);
    }

    private void writeAsXlsx(String csv) {
        Path filePath = Paths.get(dirPath, fileName.replace(".csv", ".xlsx"));
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet();
            int rowNr = 0;
            for (String line : csv.split("\\R")) {
                Row row = sheet.createRow(rowNr++);
                int colNr = 0;
                for (String cellValue : line.split(",")) {
                    double numericValue = Double.NaN;
                    boolean isNumeric;
                    try {
                        numericValue = Double.parseDouble(cellValue);
                        isNumeric = true;
                    } catch (NumberFormatException e) {
                        isNumeric = false;
                    }
                    Cell cell = row.createCell(colNr++, isNumeric ? CellType.NUMERIC : CellType.STRING);
                    if (isNumeric) {
                        cell.setCellValue(numericValue);
                    } else {
                        cell.setCellValue(cellValue);
                    }
                }
            }
            workbook.write(Files.newOutputStream(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getHeading(){
        String result = "";
        for(int i = 0; i < this.exp.getConf().getNumberOfValuesPerMeasurement(); i++){
            result += i+",";
        }
        result += ",Average,Standard Error,";
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

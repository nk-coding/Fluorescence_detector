package frontend.views;

import frontend.views.components.CancelButton;
import frontend.views.components.ChangingLeft;
import frontend.views.components.ChangingLeftBox;
import frontend.views.components.datarepresentation.Plottable;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PlotView extends VBox implements ChangingLeft {
    protected CancelButton cancelButton;
    protected ImageView leftImage;
    protected LineChart<Number,Number> plot;
    //private ObservableList<MeasurementConcentration> data;
    private boolean isUpdated = false;
    private VBox rightBox;
    protected ChangingLeftBox leftBox;
    private HBox container;

    public PlotView(MainController controller, ChangingLeftBox leftBox){

        this.setAlignment(Pos.CENTER);
        container = new HBox();

        cancelButton = new CancelButton(controller,20);
        cancelButton.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e ->{
        });

        leftImage = new ImageView(ImagesClass.getMeasureFilled());
        leftImage.setPreserveRatio(true);
        leftImage.setFitHeight(120);

        this.leftBox = leftBox;
        leftBox.setMaxWidth(400);
        leftBox.getStyleClass().add("leftBox");
        HBox.setHgrow(leftBox, Priority.SOMETIMES);
        leftBox.setAlignment(Pos.CENTER);

        rightBox = new VBox();
        rightBox.setMaxWidth(360);
        rightBox.getStyleClass().add("rightBox");
        HBox.setHgrow(rightBox, Priority.SOMETIMES);
        rightBox.setAlignment(Pos.CENTER_LEFT);

        HBox buttonsBox = new HBox(cancelButton);
        buttonsBox.setPadding(new Insets(0,0,10,0));
        buttonsBox.setAlignment(Pos.CENTER_RIGHT);


        rightBox.getChildren().addAll(buttonsBox);

        container.getChildren().addAll(rightBox);
        this.getChildren().add(container);

    }

    private void setPlot(LineChart<Number,Number> plot){
        plot.setMaxHeight(240);
        this.rightBox.getChildren().remove(this.plot);
        this.plot = plot;
        this.rightBox.getChildren().add(this.plot);

    }

    public void initChartCalibration(ObservableList data){
        final NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Concentration (nM)");
        xAxis.setAutoRanging(true);

        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Resistance (Ohm)");
        yAxis.setAutoRanging(true);

        LineChart<Number,Number> result = new LineChart<>(xAxis,yAxis);
        result.setAnimated(false);
        result.setTitle("Concentration vs Resistance");

        XYChart.Series<Number,Number> series = new XYChart.Series<>();
        ObservableList<Plottable> currentData = (ObservableList<Plottable>) data;
        for (Plottable mes : currentData){
            series.getData().add(new XYChart.Data<>(mes.getXValue(),mes.getYValue()));
        }

        data.addListener( (ListChangeListener<Plottable>) (e ->{
            System.out.print("Segura de lo que paso");
            e.next();
            Platform.runLater(() ->{
                for (Plottable mes : e.getAddedSubList())
                    series.getData().add(new XYChart.Data<>(mes.getXValue(),mes.getYValue()));
            });
        }));
        result.getData().add(series);

        this.setPlot(result);
    }

    public void initChartConcentration(ObservableList data){
        final NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Time (min)");
        xAxis.setAutoRanging(true);
        xAxis.setTickUnit(1);

        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Concentration (nM)");
        yAxis.setAutoRanging(true);

        LineChart<Number,Number> result = new LineChart<>(xAxis,yAxis);
        result.setAnimated(false);
        result.setTitle("Concentration over time");

        XYChart.Series<Number,Number> series = new XYChart.Series<>();
        ObservableList<Plottable> currentData = (ObservableList<Plottable>) data;
        for (Plottable mes : currentData){
            series.getData().add(new XYChart.Data<>(mes.getXValue(),mes.getYValue()));
        }

        data.addListener( (ListChangeListener<Plottable>) (e ->{
            System.out.print("Segura de lo que paso");
            e.next();
            Platform.runLater(() ->{
                for (Plottable mes : e.getAddedSubList())
                    series.getData().add(new XYChart.Data<>(mes.getXValue(),mes.getYValue()));
            });
        }));
        result.getData().add(series);

        this.setPlot(result);
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(boolean updated) {
        isUpdated = updated;
    }

    @Override
    public void setChangingLeft(ChangingLeftBox leftBox) {
        if(!this.container.getChildren().contains(leftBox)){
            this.container.getChildren().add(0,leftBox);
        }
    }
}

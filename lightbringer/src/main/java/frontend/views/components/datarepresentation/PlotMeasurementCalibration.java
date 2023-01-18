package frontend.views.components.datarepresentation;

import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;

public class PlotMeasurementCalibration extends LineChart<Number,Number> {


    public PlotMeasurementCalibration(Axis<Number> numberAxis, Axis<Number> numberAxis2) {
        super(numberAxis, numberAxis2);
    }
}

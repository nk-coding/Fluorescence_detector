package frontend.views.components.datarepresentation;

import backend.measurement.MeasurementCalibration;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class TableMeasurementCalibration extends TableView {
    ObservableList<MeasurementCalibration> data;


    public TableMeasurementCalibration(ObservableList data){
        this.data = data;
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<MeasurementCalibration,Double> concentrationColumn = new TableColumn<>("Concentration");
        TableColumn<MeasurementCalibration,Double> resistanceColumn = new TableColumn<>("Resistance");
        TableColumn<MeasurementCalibration,Double> deviationColumn = new TableColumn<>("Deviation");

        concentrationColumn.setCellValueFactory(new Callback<CellDataFeatures<MeasurementCalibration, Double>, ObservableValue<Double>>() {
            public ObservableValue<Double> call(CellDataFeatures<MeasurementCalibration, Double> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue().getConcetration());
            }
        });

        resistanceColumn.setCellValueFactory(new Callback<CellDataFeatures<MeasurementCalibration, Double>, ObservableValue<Double>>() {
            public ObservableValue<Double> call(CellDataFeatures<MeasurementCalibration, Double> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue().getResistance());
            }
        });

        deviationColumn.setCellValueFactory(new Callback<CellDataFeatures<MeasurementCalibration, Double>, ObservableValue<Double>>() {
            public ObservableValue<Double> call(CellDataFeatures<MeasurementCalibration, Double> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue().getRelativeDevResistance());
            }
        });
        this.setItems(this.data);
        this.getColumns().addAll(concentrationColumn,resistanceColumn,deviationColumn);
    }

    /*
    public TableMeasurementCalibration(){
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        MeasurementBaseVolt base = new MeasurementBaseVolt();
        base.saveValue(100);
        base.saveValue(200);
        MeasurementCalibration mes1 = new MeasurementCalibration(3,0.8,150000,
                base);
        mes1.saveValue(400);
        mes1.saveValue(500);
        MeasurementCalibration mes2 = new MeasurementCalibration(3,0.8,150000,
                base);
        mes2.saveValue(400);
        mes2.saveValue(500);
        data = FXCollections.observableArrayList(mes1,mes2);
        TableColumn<MeasurementCalibration,Double> concentrationColumn = new TableColumn<>("Concentration");
        TableColumn<MeasurementCalibration,Double> resistanceColumn = new TableColumn<>("Resistance");
        TableColumn<MeasurementCalibration,Double> deviationColumn = new TableColumn<>("Deviation");

        concentrationColumn.setCellValueFactory(new Callback<CellDataFeatures<MeasurementCalibration, Double>, ObservableValue<Double>>() {
            public ObservableValue<Double> call(CellDataFeatures<MeasurementCalibration, Double> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue().getConcetration());
            }
        });

        resistanceColumn.setCellValueFactory(new Callback<CellDataFeatures<MeasurementCalibration, Double>, ObservableValue<Double>>() {
            public ObservableValue<Double> call(CellDataFeatures<MeasurementCalibration, Double> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue().getResistance());
            }
        });

        deviationColumn.setCellValueFactory(new Callback<CellDataFeatures<MeasurementCalibration, Double>, ObservableValue<Double>>() {
            public ObservableValue<Double> call(CellDataFeatures<MeasurementCalibration, Double> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue().getRelativeDevResistance());
            }
        });
        this.setItems(data);
        this.getColumns().addAll(concentrationColumn,resistanceColumn,deviationColumn);
    }*/



}

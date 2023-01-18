package frontend.views.components.datarepresentation;

import backend.measurement.MeasurementBaseVolt;
import backend.measurement.MeasurementCalibration;
import backend.measurement.MeasurementConcentration;
import backend.measurement.MeasurementConcentrationBlank;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class TableMeasurementConcentration extends TableView{

    ObservableList<MeasurementConcentration> data;

    public  TableMeasurementConcentration(ObservableList data){
        this.data = data;
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<MeasurementConcentration,Double> concentrationColumn = new TableColumn<>("Concentration");
        TableColumn<MeasurementConcentration,Double> deviationColumn = new TableColumn<>("Deviation");
        TableColumn<MeasurementConcentration,Double> timeColumn = new TableColumn<>("Time");

        timeColumn.setCellValueFactory(new Callback<CellDataFeatures<MeasurementConcentration, Double>, ObservableValue<Double>>() {
            public ObservableValue<Double> call(CellDataFeatures<MeasurementConcentration, Double> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue().getTime());
            }
        });

        concentrationColumn.setCellValueFactory(new Callback<CellDataFeatures<MeasurementConcentration, Double>, ObservableValue<Double>>() {
            public ObservableValue<Double> call(CellDataFeatures<MeasurementConcentration, Double> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue().getConcentration());
            }
        });

        deviationColumn.setCellValueFactory(new Callback<CellDataFeatures<MeasurementConcentration, Double>, ObservableValue<Double>>() {
            public ObservableValue<Double> call(CellDataFeatures<MeasurementConcentration, Double> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue().getDevConcentration());
            }
        });
        this.setItems(this.data);
        this.getColumns().addAll(timeColumn,concentrationColumn,deviationColumn);
    }


    //Debugging Constructor
    /*public TableMeasurementConcentration(){
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        MeasurementBaseVolt base = new MeasurementBaseVolt();
        base.saveValue(100);
        base.saveValue(200);
        MeasurementConcentrationBlank blank = new MeasurementConcentrationBlank(3.758,0.135,0.8,1500000,base);
        blank.saveValue(300);
        blank.saveValue(400);
        MeasurementConcentration mes1 = new MeasurementConcentration(3.758,0.135,0.8,150000,base,blank,3);
        mes1.saveValue(400);
        mes1.saveValue(500);
        MeasurementConcentration mes2 = new MeasurementConcentration(3.758,0.135,0.8,150000,base,blank,4);
        mes2.saveValue(400);
        mes2.saveValue(500);
        data = FXCollections.observableArrayList(mes1,mes2);
        TableColumn<MeasurementConcentration,Double> concentrationColumn = new TableColumn<>("Concentration");
        TableColumn<MeasurementConcentration,Double> deviationColumn = new TableColumn<>("Deviation");

        concentrationColumn.setCellValueFactory(new Callback<CellDataFeatures<MeasurementConcentration, Double>, ObservableValue<Double>>() {
            public ObservableValue<Double> call(CellDataFeatures<MeasurementConcentration, Double> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue().getConcentration());
            }
        });

        deviationColumn.setCellValueFactory(new Callback<CellDataFeatures<MeasurementConcentration, Double>, ObservableValue<Double>>() {
            public ObservableValue<Double> call(CellDataFeatures<MeasurementConcentration, Double> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue().getDevConcentration());
            }
        });
        this.setItems(data);
        this.getColumns().addAll(concentrationColumn,deviationColumn);
    }*/

}

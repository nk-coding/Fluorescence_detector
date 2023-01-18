package frontend.views;

import frontend.Main;
import frontend.views.components.CancelButton;
import frontend.views.components.ChangingLeft;
import frontend.views.components.ChangingLeftBox;
import frontend.views.components.datarepresentation.Tableable;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class TableModeView extends VBox implements ChangingLeft {
    protected CancelButton cancelButton;
    protected ImageView leftImage;

    private boolean isUpdated = false;
    private VBox rightBox;
    private TableView<Tableable> table;
    protected ChangingLeftBox leftBox;
    private HBox container;

    public TableModeView(MainController controller, ChangingLeftBox leftBox){
        this.setAlignment(Pos.CENTER);
        container = new HBox();

        cancelButton = new CancelButton(controller,20);

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



    private void setTable(TableView<Tableable> table){
        table.setMaxHeight(240);
        this.rightBox.getChildren().remove(this.table);
        this.table = table;
        this.rightBox.getChildren().add(this.table);
    }

    public void initTableCalibration(ObservableList data){
        TableView<Tableable> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Tableable, Double> columnConcentration = new TableColumn<>(Tableable.Title.CONCENTRATION.getString());
        columnConcentration.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Tableable, Double>, ObservableValue<Double>>() {
            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<Tableable, Double> param) {
                try{
                    return new ReadOnlyObjectWrapper<>(param.getValue().getValueColumn(Tableable.Title.CONCENTRATION));
                } catch (Tableable.UnsupportedColumnException e){
                    Main.createExceptionWindow(e);
                    return new ReadOnlyObjectWrapper<>(-1.0);
                }
            }
        });
        table.getColumns().add(columnConcentration);

        TableColumn<Tableable, Double> columnResistance = new TableColumn<>(Tableable.Title.RESISTANCE.getString());
        columnResistance.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Tableable, Double>, ObservableValue<Double>>() {
            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<Tableable, Double> param) {
                try{
                    return new ReadOnlyObjectWrapper<>(param.getValue().getValueColumn(Tableable.Title.RESISTANCE));
                } catch (Tableable.UnsupportedColumnException e){
                    Main.createExceptionWindow(e);
                    return new ReadOnlyObjectWrapper<>(-1.0);
                }
            }
        });
        table.getColumns().add(columnResistance);

        table.setItems(data);
        this.setTable(table);
    }

    public void initTableConcentration(ObservableList data){
        TableView<Tableable> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Tableable, Double> columnTime = new TableColumn<>(Tableable.Title.TIME.getString());
        columnTime.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Tableable, Double>, ObservableValue<Double>>() {
            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<Tableable, Double> param) {
                try{
                    return new ReadOnlyObjectWrapper<>(param.getValue().getValueColumn(Tableable.Title.TIME));
                } catch (Tableable.UnsupportedColumnException e){
                    Main.createExceptionWindow(e);
                    return new ReadOnlyObjectWrapper<>(-1.0);
                }
            }
        });
        table.getColumns().add(columnTime);

        TableColumn<Tableable, Double> columnConcentration = new TableColumn<>(Tableable.Title.CONCENTRATION.getString());
        columnConcentration.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Tableable, Double>, ObservableValue<Double>>() {
            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<Tableable, Double> param) {
                try{
                    System.out.println("hola: "+param.getValue().getValueColumn(Tableable.Title.CONCENTRATION));
                    return new ReadOnlyObjectWrapper<>(param.getValue().getValueColumn(Tableable.Title.CONCENTRATION));
                } catch (Tableable.UnsupportedColumnException e){
                    Main.createExceptionWindow(e);
                    return new ReadOnlyObjectWrapper<>(-1.0);
                }
            }
        });

        TableColumn<Tableable, Double> columnConcentrationDev = new TableColumn<>(Tableable.Title.DEVIATION.getString());
        columnConcentrationDev.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Tableable, Double>, ObservableValue<Double>>() {
            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<Tableable, Double> param) {
                try{
                    return new ReadOnlyObjectWrapper<>(param.getValue().getValueColumn(Tableable.Title.DEVIATION));
                } catch (Tableable.UnsupportedColumnException e){
                    Main.createExceptionWindow(e);
                    return new ReadOnlyObjectWrapper<>(-1.0);
                }
            }
        });

        table.getColumns().add(columnConcentration);
        table.setItems(data);
        this.setTable(table);
    }

    public TableView<Tableable> getTable() {
        return table;
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

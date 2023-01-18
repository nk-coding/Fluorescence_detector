package frontend.views;

import frontend.views.components.ChangingLeftBox;
import frontend.views.components.ViewsEnum;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.Stack;

public class MainController{

    private SimpleBooleanProperty appClosed = new SimpleBooleanProperty(false);

    private Stack<Parent> prevViewsStack ;
    private Scene scene;

    private StartMenu startMenu;
    private ConcentrationMenu concentrationMenu;
    private CalibrateMenu calibrateMenu;
    private ConcentrationView concentrationView;
    private CalibrateView calibrateView;
    private TableModeView tableView;
    private PlotView plotView;
    private ChangingLeftBox leftBox;

    public MainController() {
        leftBox = new ChangingLeftBox();
        startMenu = new StartMenu(this);
        concentrationMenu = new ConcentrationMenu(this);
        calibrateMenu = new CalibrateMenu(this);
        concentrationView = new ConcentrationView(this,leftBox);
        calibrateView = new CalibrateView(this, leftBox);
        tableView = new TableModeView(this, leftBox);
        plotView = new PlotView(this, leftBox);

        scene = new Scene(startMenu);
        prevViewsStack = new Stack<>();
    }

    public void changeView(ViewsEnum newView){
        Parent prevView = this.scene.getRoot();
        switch (newView){
            case CONCENTRATION_MENU:
                this.scene.setRoot(concentrationMenu);
                break;
            case START_MENU:
                this.scene.setRoot(startMenu);
                break;
            case CALIBRATION_MENU:
                this.scene.setRoot(calibrateMenu);
                break;
            case CONCETRATION_VIEW:
                this.concentrationView.setChangingLeft(this.leftBox);
                this.scene.setRoot(concentrationView);
                break;
            case CALIBRATION_VIEW:
                this.calibrateView.setChangingLeft(this.leftBox);
                this.scene.setRoot(calibrateView);
                break;
            case TABLE_VIEW:
                this.tableView.setChangingLeft(this.leftBox);
                this.scene.setRoot(tableView);
                break;
            case PLOT_VIEW:
                this.plotView.setChangingLeft(this.leftBox);
                this.scene.setRoot(plotView);
                break;
        }
        prevViewsStack.push(prevView);
    }

    public void startChangingBox(){
        new Thread(leftBox).start();
    }

    public Scene getScene() {
        return scene;
    }

    public void returnPreviousView() {
        Parent prevView = prevViewsStack.pop();
        if(prevView == this.calibrateView){
            this.calibrateView.setChangingLeft(this.leftBox);
        } else if(prevView == this.concentrationView){
            this.concentrationView.setChangingLeft(this.leftBox);
        }
        this.scene.setRoot(prevView);
    }

    public void stop(){
        this.appClosed.set(true);
    }

    public SimpleBooleanProperty appClosedProperty() {
        return appClosed;
    }

    public ConcentrationView getConcentrationView() {
        return concentrationView;
    }

    public CalibrateView getCalibrateView() {
        return calibrateView;
    }

    public TableModeView getTableView() {
        return tableView;
    }

    public PlotView getPlotView() {
        return plotView;
    }

}

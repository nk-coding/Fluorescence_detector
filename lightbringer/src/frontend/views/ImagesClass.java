package frontend.views;

import frontend.Main;
import javafx.scene.image.Image;

public abstract class ImagesClass {

    private final static Image CASCAID_LOGO_IMAGE = new Image(Main.class.getClassLoader().getResourceAsStream("resources/ON_OFF_1.png"));
    private final static Image MEASURE_EMPTY = new Image(Main.class.getClassLoader().getResourceAsStream("resources/Measure_click.png"));
    private final static Image MEASURE_FILLED = new Image(Main.class.getClassLoader().getResourceAsStream("resources/Measure.png"));
    private final static Image CALIBRATE_EMPTY = new Image(Main.class.getClassLoader().getResourceAsStream("resources/Calibration_click.png"));
    private final static Image CALIBRATE_FILLED = new Image(Main.class.getClassLoader().getResourceAsStream("resources/Calibration.png"));
    private final static Image CANCEL = new Image(Main.class.getClassLoader().getResourceAsStream("resources/Quit.png"));
    private final static Image START = new Image(Main.class.getClassLoader().getResourceAsStream("resources/start.png"));
    private final static Image LOADING_0 = new Image(Main.class.getClassLoader().getResourceAsStream("resources/Loading_00.png"));
    private final static Image LOADING_25 = new Image(Main.class.getClassLoader().getResourceAsStream("resources/Loading_025.png"));
    private final static Image LOADING_50 = new Image(Main.class.getClassLoader().getResourceAsStream("resources/Loading_05.png"));
    private final static Image LOADING_75 = new Image(Main.class.getClassLoader().getResourceAsStream("resources/Loading_075.png"));
    private final static Image TABLE = new Image(Main.class.getClassLoader().getResourceAsStream("resources/table.png"));
    private final static Image GRAPH = new Image(Main.class.getClassLoader().getResourceAsStream("resources/plot.png"));
    private final static Image DIRECTORY = new Image(Main.class.getClassLoader().getResourceAsStream("resources/cd.png"));
    private final static Image PRESS_MEASURE = new Image(Main.class.getClassLoader().getResourceAsStream("resources/status_sample.png"));
    private final static Image SAVEPREF = new Image(Main.class.getClassLoader().getResourceAsStream("resources/save.png"));

    public static Image getCascaidLogoImage() {
        return CASCAID_LOGO_IMAGE;
    }

    public static Image getMeasureEmpty() {
        return MEASURE_EMPTY;
    }

    public static Image getMeasureFilled() {
        return MEASURE_FILLED;
    }

    public static Image getCalibrateEmpty() {
        return CALIBRATE_EMPTY;
    }

    public static Image getCalibrateFilled() {
        return CALIBRATE_FILLED;
    }

    public static Image getCANCEL() {
        return CANCEL;
    }

    public static Image getSTART() {
        return START;
    }

    public static Image getLoading0() {
        return LOADING_0;
    }

    public static Image getLoading25() {
        return LOADING_25;
    }

    public static Image getLoading50() {
        return LOADING_50;
    }

    public static Image getLoading75() {
        return LOADING_75;
    }

    public static Image getTABLE() {
        return TABLE;
    }

    public static Image getGRAPH() {
        return GRAPH;
    }

    public static Image getDIRECTORY() {
        return DIRECTORY;
    }

    public static Image getPressMeasure() { return PRESS_MEASURE; }

    public static Image getSAVEPREF() {
        return SAVEPREF;
    }
}

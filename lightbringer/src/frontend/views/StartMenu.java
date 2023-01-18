package frontend.views;

import frontend.views.components.ViewsEnum;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;



public class StartMenu extends VBox{
    private ImageView cascAIDLogo;
    //ImageViews instead of buttons because the former is easier to style, IMO.
    private ImageView calibrateButton;
    private ImageView measureButton;


    public StartMenu(MainController controller){
        this.setAlignment(Pos.CENTER);

        HBox container = new HBox();
        cascAIDLogo = new ImageView(ImagesClass.getCascaidLogoImage());
        cascAIDLogo.setPreserveRatio(true);
        cascAIDLogo.setFitHeight(300);

        calibrateButton = new ImageView(ImagesClass.getCalibrateEmpty());
        calibrateButton.setPreserveRatio(true);
        calibrateButton.setFitHeight(120);

        calibrateButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
            controller.changeView(ViewsEnum.CALIBRATION_MENU);
        });
        calibrateButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e ->{
            calibrateButton.setImage(ImagesClass.getCalibrateFilled());
        });
        calibrateButton.addEventHandler(MouseEvent.MOUSE_RELEASED, e ->{
            calibrateButton.setImage(ImagesClass.getCalibrateEmpty());
        });


        measureButton = new ImageView(ImagesClass.getMeasureEmpty());
        measureButton.setPreserveRatio(true);
        measureButton.setFitHeight(120);


        measureButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
            controller.changeView(ViewsEnum.CONCENTRATION_MENU);
        });
        measureButton.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            measureButton.setImage(ImagesClass.getMeasureFilled());
        });
        measureButton.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            measureButton.setImage(ImagesClass.getMeasureEmpty());
        });

        VBox logoBox = new VBox(cascAIDLogo);
        HBox.setHgrow(logoBox, Priority.SOMETIMES);
        logoBox.setAlignment(Pos.CENTER);

        VBox paneCalibrate = new VBox(calibrateButton);
        paneCalibrate.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(paneCalibrate,Priority.SOMETIMES);

        VBox paneMeasure = new VBox(measureButton);
        paneMeasure.setAlignment(Pos.BOTTOM_CENTER);
        VBox.setVgrow(paneMeasure,Priority.SOMETIMES);

        VBox buttonsBox = new VBox(paneCalibrate,paneMeasure);

        HBox.setHgrow(buttonsBox, Priority.SOMETIMES);
        buttonsBox.setAlignment(Pos.CENTER);
        container.getChildren().addAll(logoBox,buttonsBox);
        this.getChildren().add(container);

    }
}

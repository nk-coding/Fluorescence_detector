package frontend.views.components;

import frontend.views.ImagesClass;
import frontend.views.MainController;
import javafx.scene.image.ImageView;


/**
 * A Cancel button in Lightbringers application. When pressed, the view is changed to the previous one.
 */
public class CancelButton extends ImageView {

    /**
     * Constructor for CancelButton.
     * @param controller MainController controlling the application.
     * @param fitHeight default fit height of the button.
     */
    public CancelButton(MainController controller, double fitHeight){
        super(ImagesClass.getCANCEL());
        this.setFitHeight(fitHeight);
        this.setPreserveRatio(true);
        this.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e ->{
            controller.returnPreviousView();
        });
    }

}

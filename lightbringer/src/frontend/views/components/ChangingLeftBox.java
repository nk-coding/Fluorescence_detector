package frontend.views.components;

import frontend.Main;
import frontend.views.ImagesClass;
import frontend.views.MainController;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * A VBox displaying a picture changing across tiem and depending on its state. Used to create the progress effect and
 * .
 */
public class ChangingLeftBox extends VBox implements Runnable {

    /**
     * Image array with the images
     */
    private Image[] loadingImages;
    private int posLoadingImages = 0;
    private Image readyImage;
    private Image blankImage;
    private Image doneImage;
    private State currState;
    private ImageView currImage;

    /**
     * Height of button
     */
    protected double heightButtons = 195;

    private int waitingTime = 2000;
    //private boolean blankReached = false;
    private boolean measureReached = false;

    public enum State {
        RUNNING,
        READY_MEASURE,
        //READY_BLANK,
        DONE,
        KILL;
    }

    public ChangingLeftBox(){
        this.loadingImages = new Image[] {ImagesClass.getLoading0(), ImagesClass.getLoading25(), ImagesClass.getLoading50(),
        ImagesClass.getLoading75()};
        this.readyImage = ImagesClass.getPressMeasure();
        this.blankImage = this.readyImage;
        this.doneImage = ImagesClass.getCascaidLogoImage();

        this.currImage = new ImageView(ImagesClass.getCascaidLogoImage());
        this.currImage.setPreserveRatio(true);
        this.currImage.setFitHeight(this.heightButtons);

        this.currState = State.READY_MEASURE;
        HBox.setHgrow(this, Priority.SOMETIMES);
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(currImage);
    }

    public void changeState(State state){
        this.currState = state;
    }

    public void changeRunningImage(){
        this.currImage.setImage(this.loadingImages[this.posLoadingImages]);
        this.posLoadingImages = (this.posLoadingImages + 1)  % this.loadingImages.length;
    }

    @Override
    public synchronized void run() {
        boolean isKilled = false;
        while(!isKilled){
            switch (this.currState){
                case RUNNING:
                    Platform.runLater(() ->{
                        this.changeRunningImage();
                    });
                    break;
                case READY_MEASURE:
                    this.posLoadingImages = 0;
                    this.measureReached = true;
                    Platform.runLater(() ->{
                        this.currImage.setImage(this.readyImage);
                    });
                    break;
                /*case READY_BLANK:
                    this.posLoadingImages = 0;
                    this.blankReached = true;
                    Platform.runLater(() ->{
                        this.currImage.setImage(this.blankImage);
                    });
                    break;*/
                case DONE:
                    this.posLoadingImages = 0;
                    Platform.runLater(() ->{
                        this.currImage.setImage(this.doneImage);
                    });
                    break;
                case KILL:
                    this.posLoadingImages = 0;
                    isKilled = true;
                    //this.blankReached = false;
                    this.measureReached = false;
                    Platform.runLater(() ->{
                        this.currImage.setImage(this.doneImage);
                    });
                    break;
            }
            try {
                this.wait(this.waitingTime);
            } catch (InterruptedException e) {
                Main.createExceptionWindow(e);
            }
        }
    }

    /*public boolean isBlankReached() {
        return blankReached;
    }*/

    public boolean isMeasureReached() {
        return measureReached;
    }
}

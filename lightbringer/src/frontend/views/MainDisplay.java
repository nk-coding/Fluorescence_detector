package frontend.views;

import frontend.views.components.ChangingLeftBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainDisplay extends VBox {

    ChangingLeftBox leftBox;
    RightBox rightBox;

    public MainDisplay(){
        this.leftBox = new ChangingLeftBox();
        this.rightBox = new RightBox();

        HBox container = new HBox();
        container.getChildren().addAll(leftBox,rightBox);
        this.getChildren().add(container);
    }

    public void changeRightBox(RightBox newRightBox){
        this.getChildren().remove(this.rightBox);
        this.getChildren().add(newRightBox);
    }

}

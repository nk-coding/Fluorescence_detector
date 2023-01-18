package frontend.views.components;

import backend.experiment.Parameters;
import backend.experiment.ParametersConcExperiment;

public class ParamConfViewCalibrate extends ParamConfView {

    public Parameters getParameters() throws NumberFormatException{
        double resistance = Double.parseDouble(this.resistance.getText());
        return new Parameters(resistance);
    }
}

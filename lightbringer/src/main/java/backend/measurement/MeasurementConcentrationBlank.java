package backend.measurement;

import backend.experiment.ParametersConcExperiment;

/**
 * Measurement of blank concentration. Adds a concentration value of 0 to the {@link MeasurementResistance}
 */
public class MeasurementConcentrationBlank extends MeasurementResistance {

    /**
     * Concentration, in uM, of the measurement. 0, since this is a blank measurement.
     */
    private final double concentration = 0;

    private final double devConcentration = 0;

    /**
     * Constructor for MeasurementConcentrationBlank
     * @param refResistance reference resistance.
     * @param baseVoltMeasurement Measurement of the base voltage.
     */
    public MeasurementConcentrationBlank(double refResistance, MeasurementBaseVolt baseVoltMeasurement) {
        super(refResistance, baseVoltMeasurement);
    }

    /**
     * Constructor for MeasurementResistance. Uses Parameters to encapsulate the constants needed.
     * @param param ParametersConcExperiment with gamma, k, k's standard deviation and reference resistance.
     * @param baseVoltMeasurement Measurement of the base voltage.
     */
    public MeasurementConcentrationBlank(ParametersConcExperiment param, MeasurementBaseVolt baseVoltMeasurement){
        this(param.getRefResistance(),baseVoltMeasurement);
    }

    /**
     * Gives a String representation of the Measurement formatted as an entry for a CSV table.
     * First all values are shown.
     * Then the average,
     * Then the standard deviation of the raw values.
     * Then the resistance.
     * Then the standard deviation of the resistance.
     * Then the concentration (0).
     * @return CSV-entry representation of the Measurement.
     */
    @Override
    public String toCSVString() {
        String result = "Blank,";
        result += super.toCSVString() + "," + this.getConcentration() + "," + this.getDevConcentration();
        //String result = super.toCSVString() + ",";
        //result += this.getTime() +"," + this.getConcentration() + "," + this.getDevConcentration();
        return result;
    }

    /**
     * Getter for concentration.
     * @return the concentration.
     */
    public double getConcentration() {
        return concentration;
    }

    public double getDevConcentration() {
        return devConcentration;
    }
}

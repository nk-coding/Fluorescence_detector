package backend.measurement;

import backend.experiment.Parameters;
import frontend.views.components.datarepresentation.Plottable;
import frontend.views.components.datarepresentation.Tableable;

/**
 * Measurement of a calibration point. This class adds to {@link MeasurementResistance} the concentration associated with the
 * measurement.
 */
public class MeasurementCalibration extends MeasurementResistance implements Plottable, Tableable {

    /**
     * Instance for testing.
     */
    private static final MeasurementCalibration EXAMPLE = new MeasurementCalibration(0,0,null);

    /**
     * Concentration in uM of fluorophore when the measurement was taken..
     */
    private double concetration;

    /**
     * Constructor for MeasurementCalibraiton
     * @param concetration concentation in uM.
     * @param refResistance reference resistance in Ohm.
     * @param baseVoltMeasurement Measurement of the base voltage.
     */
    public MeasurementCalibration(double concetration, double refResistance, MeasurementBaseVolt baseVoltMeasurement) {
        super(refResistance, baseVoltMeasurement);
        this.concetration = concetration;
    }

    /**
     * Constructor for MeasurementResistance. Uses Parameters to encapsulate the constants needed.
     * @param concetration concentration in uM.
     * @param param Parameters with the gamma and reference resistance.
     * @param baseVoltMeasurement Measurement of the base voltage.
     */
    public MeasurementCalibration(double concetration,Parameters param, MeasurementBaseVolt baseVoltMeasurement) {
        super(param, baseVoltMeasurement);
        this.concetration = concetration;
    }

    /**
     * Gives a String representation of the Measurement formatted as an entry for a CSV table.
     * First the concentration is shown,
     * Then all values,
     * Then the average,
     * Then the standard deviation of the raw values.
     * Then the resistance.
     * Then the standard deviation of the resistance.
     * @return CSV-entry representation of the Measurement.
     */
    @Override
    public String toCSVString(){
        String result = this.getConcetration() + ",";
        result += super.toCSVString();
        //String result = super.toCSVString() + ",";
        //result += this.getConcetration();
        return result;

    }

    /**
     * Getter for concentration.
     * @return the concentration of the Measurement.
     */
    public double getConcetration() {
        return concetration;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public double getXValue() {
        return this.getConcetration();
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public double getYValue() {
        return this.getResistance();
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public Title[] columnsList() {
        return new Title[]{Title.CONCENTRATION,Title.RESISTANCE};
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public double getValueColumn(Title title) throws UnsupportedColumnException{
        switch (title){
            case CONCENTRATION:
                return this.getConcetration();
            case RESISTANCE:
                return this.getResistance();
            default:
                throw new UnsupportedColumnException(MeasurementCalibration.class,title);
        }
    }

    /**
     * For testing
     */
    public static MeasurementCalibration getEXAMPLE() {
        return EXAMPLE;
    }
}

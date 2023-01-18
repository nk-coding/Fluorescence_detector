package backend.measurement;

/**
 * Measurement of the base voltage in Lightbringer's circuit.
 */
public class MeasurementBaseVolt extends Measurement {

    /**
     * Constructor for MeasurementBaseVolt.
     */
    public MeasurementBaseVolt() {
        super();
    }

    /**
     * Getter for the base voltage in Lightbringer's circuit. Simply the average of the measured values.
     * @return base voltage in Lightbringer's circuit
     */
    public double getBaseVolt(){
        return super.getAverage();
    }

    /**
     * Getter for the base voltage's standard deviation. Simply the standard deviation of the measured values.
     * @return base voltage's standard deviation.
     */
    public double getBaseVoltDeviation(){
        return super.getRelativeDeviation();
    }

    /**
     * For testing.
     */
    public static void test(){
        int[] values = {100,200,300,400,500};
        MeasurementBaseVolt mes = new MeasurementBaseVolt();
        for (int val : values){
            mes.saveValue(val);
        }
        System.out.println("Computed deviation: "+mes.getRelativeDeviation()+". Expected: "+0.2357);
        System.out.println("Computed average: "+mes.getAverage()+". Expected: "+300);
    }
}

package backend.measurement;

import backend.experiment.Parameters;

/**
 * Measurement of a resistance value in Lightbringe's circuit.
 */
public class MeasurementResistance extends Measurement {

    /**
     * Reference resistance.
     */
    protected final double refResistance;

    /**
     * Measurement of the base voltage.
     */
    private MeasurementBaseVolt baseVoltMeasurement;

    /**
     * Resistance calculated from the raw voltages in the valuesList. 0 if the list is empty.
     */
    private double resistance;

    /**
     * Standard deviation of the resistance. 0 if the list is empty.
     */
    private double devResistance;

    /**
     * Relative statistical standard error of the mean of the resistance. 0 if the list is empty.
     */
    private double relativeDevStatResistance;

    /**
     * Relative systematic standard error of the mean of the resistance. 0 if the list is empty.
     */
    private double relativeDevSysResistance;

    /**
     * Constructor for MeasurementResistance.
     * @param refResistance reference resistance.
     * @param baseVoltMeasurement Measurement of the base voltage.
     */
    public MeasurementResistance(double refResistance, MeasurementBaseVolt baseVoltMeasurement) {
        super();
        this.refResistance = refResistance;
        this.baseVoltMeasurement = baseVoltMeasurement;
    }

    /**
     * Constructor for MeasurementResistance. Uses Parameters to encapsulate the constants needed.
     * @param param Parameters with the gamma and reference resistance.
     * @param baseVoltMeasurement Measurement of the base voltage.
     */
    public MeasurementResistance(Parameters param, MeasurementBaseVolt baseVoltMeasurement){
        this(param.getRefResistance(),baseVoltMeasurement);
    }

    /**
     * Getter for the resistance of the measurement. If the resistance is 0, it is computed again.
     * @return resistance of the measurement.
     */
    public double getResistance() {
        if(resistance == 0){
            this.resistance = this.valueList.size() == 0 ? 0 : refResistance/(baseVoltMeasurement.getBaseVolt()/ this.getAverage() - 1);
        }
        return this.resistance;
    }

    /**
     * Getter for the relative statistical standard error of the mean of the resistance. If it is 0, it is computed again.
     * @return relativeDevStatResistance.
     */
    public double getRelativeDevStatResistance(){
        if(relativeDevStatResistance == 0){
            relativeDevStatResistance = baseVoltMeasurement.getAverage()/(baseVoltMeasurement.getAverage() - this.getAverage())
                    * Math.sqrt(Math.pow(baseVoltMeasurement.getRelativeDeviation(),2) + Math.pow(this.getRelativeDeviation(),2));
        }
        return relativeDevStatResistance;
    }

    /**
     * Getter for the relative systematic standard error of the mean of the resistance. If it is 0, it is computed again.
     * @return relativeDevStatResistance.
     */
    public double getRelativeDevSysResistance(){
        if(relativeDevSysResistance == 0){
            relativeDevSysResistance = baseVoltMeasurement.getAverage()/(baseVoltMeasurement.getAverage() - this.getAverage())
                    * Math.abs(1./baseVoltMeasurement.getAverage() + 1./this.getAverage());
        }
        return relativeDevSysResistance;
    }

    /**
     * Getter for the standard deviation of the resistance. If it is 0, it is computed again.
     * @return standard deviation of the resistance.
     */
    public double getRelativeDevResistance(){
        if(devResistance == 0){
            devResistance = this.getRelativeDevStatResistance() + this.getRelativeDevSysResistance();
        }
        return devResistance;
    }

    public double getDevResistance(){
        return this.getRelativeDevResistance() * this.getResistance();
    }

    /**
     * Gives a String representation of the Measurement formatted as an entry for a CSV table.
     * First all values are shown.
     * Then the average,
     * Then the standard deviation of the raw values.
     * Then the resistance.
     * Then the standard deviation of the resistance.
     * @return CSV-entry representation of the Measurement.
     */
    @Override
    public String toCSVString(){
        String result = super.toCSVString() + ",";
        result += this.getResistance()+ "," + this.getDevResistance();
        return result;
    }

    /**
     * For testing.
     */
    public static void test(){
        int[] values = {100,200,300,400,500};

        MeasurementBaseVolt ref = new MeasurementBaseVolt();
        MeasurementResistance mes = new MeasurementResistance(1500000,ref);
        for (int val : values){
            mes.saveValue(val+30);
            ref.saveValue(val);
        }
        System.out.println("Computed average Res: "+mes.getResistance()+". Expected: "+(-16500000));
        System.out.println("Computed average devRes Sys: "+mes.getRelativeDevSysResistance()+". Expected: "+(-0.0636));
        System.out.println("Computed average devRes Sys: "+mes.getRelativeDevStatResistance()+". Expected: "+(-3.1854));
        System.out.println("Computed average devRes: "+mes.getRelativeDevResistance()+". Expected: "+"-3.2490");
    }

}

package backend.experiment;

/**
 * Configuration represents non physical values for a give experiment (e.g. number of values per measurement, total number
 * of measurements, etc.)
 */
public class Configuration {

    /**
     * Maximum possible delay on value. Used for capping the inputted value.
     */
    private final static int MAX_VALUE_DELAY_ON = 16777215;

    /**
     * Maximum possible delay off value. Used for capping the inputted value.
     */
    private final static int MAX_VALUE_DELAY_OFF = 16383;

    /**
     * Number of total measurements in the experiment. This value can be ignored by the experiment if needed (e.g. in the
     * case of a calibration experiment)
     */
    private int numberOfMeasurments;

    /**
     * Delay (in ms) between each measurement. This value can be ignored by the experiment if needed (e.g. in the
     * case of a calibration experiment)
     */
    private int delayBetweenMeasurements;

    /**
     * Number of points taken per measurement. Meaning, the total number of values in each measurements used for calculating averages.
     * @see backend.measurement.Measurement
     */
    private byte numberOfValuesPerMeasurement; //Between 0 and 127.

    /**
     * Delay (in ms) between each point in a measurement
     * @see backend.measurement.Measurement
     */
    private byte delayBetweenValues; //Between 0 and 127.

    /**
     * Delay (in ms) between turning on the LED light of Lightbringer and starting probing the analog pin.
     */
    private int delayOn; //Between 0 and 16383

    /**
     * Delay (in ms) between the probe of the analog pin and turning off the LED light of Lightbringer.
     */
    private int delayOff; //Between 0 and 16383


    /**
     * Constructor for Configuration. Sets delayBetweenMeasurements and numberOfMeasurements to 0.
     * @param delayBetweenValues delay between the different values of a measurement.
     * @param numberOfValuesPerMeasurement total number of values in a measurement.
     * @param delayOn delay between turning on the LED and start probing.
     * @param delayOff delay between the last probe and turning off the LED.
     */
    public Configuration(byte delayBetweenValues, byte numberOfValuesPerMeasurement, int delayOn, int delayOff) throws NumberFormatException {
        this(delayBetweenValues,numberOfValuesPerMeasurement,delayOn,delayOff,0,0);

    }

    /**
     * Constructor for Configuration.
     * @param delayBetweenValues delay between the different values of a measurement.
     * @param numberOfValuesPerMeasurement total number of values in a measurement.
     * @param delayOn delay between turning on the LED and start probing.
     * @param delayOff  delay between the last probe and turning off the LED.
     * @param delayBetweenMeasurements delay between each measurement taken.
     * @param numberOfMeasurments total number of measurements taken.
     */
    public Configuration(byte delayBetweenValues, byte numberOfValuesPerMeasurement, int delayOn, int delayOff,
                         int delayBetweenMeasurements,int numberOfMeasurments) throws NumberFormatException{
        this.numberOfMeasurments = numberOfMeasurments;
        this.delayBetweenMeasurements = delayBetweenMeasurements;
        this.delayBetweenValues = delayBetweenValues;
        this.numberOfValuesPerMeasurement = numberOfValuesPerMeasurement;

        if(delayOn < 0 || delayOn > MAX_VALUE_DELAY_ON){
            throw new NumberFormatException();
        } else{
            this.delayOn = delayOn;
        }

        if(delayOff < 0 || delayOff > MAX_VALUE_DELAY_OFF){
            throw new NumberFormatException();
        } else{
            this.delayOff = delayOff;
        }
    }

    /**
     * Getter for numberOfMeasurements.
     * @return number of measurements in the configuration.
     */
    public int getNumberOfMeasurments() {
        return numberOfMeasurments;
    }

    /**
     * Getter for delayBetweenMeasurements.
     * @return delay between measurements in the configuration.
     */
    public int getDelayBetweenMeasurements() {
        return delayBetweenMeasurements;
    }

    /**
     * Getter for delayBetweenValues
     * @return delay between values in the configuration.
     */
    public byte getDelayBetweenValues() {
        return delayBetweenValues;
    }

    /**
     * Getter for numberOfValuesPerMeasurement.
     * @return number of values per measurement in the configuration.
     */
    public byte getNumberOfValuesPerMeasurement() {
        return numberOfValuesPerMeasurement;
    }

    /**
     * Getter for delayOn
     * @return delay on in the configuration.
     */
    public int getDelayOn() {
        return delayOn;
    }

    /**
     * Setter for numberOfMeasurements.
     * @param numberOfMeasurments new value.
     */
    public void setNumberOfMeasurments(int numberOfMeasurments) {
        this.numberOfMeasurments = numberOfMeasurments;
    }

    /**
     * Setter for delayBetweenMeasurements.
     * @param delayBetweenMeasurements new value.
     */
    public void setDelayBetweenMeasurements(int delayBetweenMeasurements) {
        this.delayBetweenMeasurements = delayBetweenMeasurements;
    }

    /**
     * Gets the value of delayOn as two bytes. Needed, because Lightbringer receives numbers as bytes.
     * @return byte array with the most representing byte in 0 and the least representing byte in 1 (of the binary representation
     * of delayOn).
     */
    public byte[] getDelayOnAsBytes(){
        byte[] result = new byte[3];
        result[0] = (byte) (delayOn >>16);
        result[1] = (byte) (delayOn>>8);
        result[2] = (byte) (delayOn);
        return result;
    }

    /**
     * Getter for delayOff
     * @return delayOff in the configuration.
     */
    public int getDelayOff() {
        return delayOff;
    }

    /**
     * Gets the value of delayOff as two bytes. Needed, because Lightbringer receives numbers as bytes.
     * @return byte array with the most representing byte in 0 and the least representing byte in 1 (of the binary representation
     * of delayOff).
     */
    public byte[] getDelayOffAsBytes(){
        byte[] result = new byte[2];
        result[0] = (byte) (this.delayOff >> 7);
        result[1] = (byte) (this.delayOff - result[0]*Math.pow(2,7));
        return result;
    }

    public static void test(){
        Configuration conf = new Configuration((byte) 120,(byte) 120,3000,500,15,15);
        System.out.println("Computed DelayOn byte array: ");
        for (byte val : conf.getDelayOnAsBytes()){
            System.out.println(val);
        }
        System.out.println("Expected: "+ "{23,56}");
        System.out.println("Computed DelayOff byte array: ");
        for (byte val : conf.getDelayOffAsBytes()){
            System.out.println(val);
        }
        System.out.println("Expected: "+ "{3,116}");
    }

}

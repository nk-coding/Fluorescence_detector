package backend.measurement;

import backend.experiment.ParametersConcExperiment;
import frontend.views.components.datarepresentation.Plottable;
import frontend.views.components.datarepresentation.Tableable;

/**
 * Measurement of a concentration value. Adds a concentration value to {@link MeasurementResistance} based on the
 * values in the Measurement and the blank concentration and base voltage.
 */
public class MeasurementConcentration extends MeasurementResistance implements Plottable, Tableable {


    /**
     * For testing
     */
    private static  final MeasurementConcentration EXAMPLE = new MeasurementConcentration(0,0,0,0,0,0,0,
            null,0);

    /**
     * Resistance fo the blank.
     */
    protected final double blankResistance;

    /**
     * Standard deviation of the blank's resistance.
     */
    protected final double blankDevResistance;

    /**
     * gamma constant.
     */
    protected final double gamma;

    /**
     * Absolute standard error of gamma.
     */
    protected final double devGamma;

    /**
     * k constant.
     */
    private final double k;

    /**
     * k's standard deviation.
     */
    private final double devK;

    /**
     * Concentration, in uM, of the measurement. 0 if the list is empty.
     */
    private double concentration;

    /**
     * Standard deviation of the concentration. 0 if the list is empty.
     */
    private double devConcentration;

    /**
     * Relative systematic standard error of the mean of the concentration. 0 if the list is empty.
     */
    private double devSysConcentration;

    /**
     * Relative statistical standard error of the mean of the concentration. 0 if the list is empty.
     */
    private double devStatConcentration;

    /*
    /**
     * Blank measurement to be used as baseline.
     */
    /*
    private MeasurementConcentrationBlank blank;*/

    /**
     * Time, in minutes after the first measurement of a {@link backend.experiment.ExperimentConcentration},
     * of the measurement.
     */
    private double time;

    /**
     * Constructor for MeasurementConcentration.
     * @param k k constant.
     * @param devK k's standard deviation.
     * @param gamma gamma constant.
     * @param refResistance reference resistance.
     * @param baseVoltMeasurement Measurement of the base voltage.
     * @param time time after the first measurement.
     */
    public MeasurementConcentration(double blankResistance, double blankDevResistance, double k,double devK, double gamma, double devGamma, double refResistance,
                                    MeasurementBaseVolt baseVoltMeasurement, double time) {
        super(refResistance, baseVoltMeasurement);
        this.gamma = gamma;
        this.devGamma = devGamma;
        this.k = k;
        this.devK = devK;
        this.blankResistance = blankResistance;
        this.blankDevResistance =blankDevResistance;
        this.time = time;
    }

    /**
     * Constructor for MeasurementResistance. Uses Parameters to encapsulate the constants needed.
     * @param param ParametersConcExperiment with gamma, k, k's standard deviation and reference resistance.
     * @param baseVoltMeasurement Measurement of the base voltage.
     * @param time time after the first measurement.
     */
    public MeasurementConcentration(ParametersConcExperiment param, MeasurementBaseVolt baseVoltMeasurement, double time){
        this(param.getBlankResistance(), param.getBlankResistanceDev(),param.getK(),param.getDevK(),param.getGamma(),param.getDevGamma(),param.getRefResistance(),baseVoltMeasurement,time);

    }

    /**
     * Getter for the concentration. If it is 0, it is computed again.
     * @return concentration of the Measurement.
     */
    public double getConcentration() {
        if (concentration == 0){
            this.concentration = this.valueList.size() == 0 ? 0 : (Math.pow(blankResistance / this.getResistance(), 1/gamma) -1)/k;
        }
        return concentration;
    }

    /**
     * Getter for the absolute systematic error of the mean of the concentration. If it is 0, it is computed again.
     * @return relative systematic error of the mean of the concentration.
     */
    public double getDevSysConcentration(){
        if(devSysConcentration == 0){
            devSysConcentration = Math.pow(blankResistance/this.getResistance(),1.0/gamma)
                    * 1./k
                    * (Math.abs(blankDevResistance) + Math.abs(this.getRelativeDevSysResistance())
                        + Math.abs(devGamma/gamma  * Math.log(Math.pow(blankResistance/this.getResistance(),1.0/gamma))))
                    + this.getConcentration() * Math.abs(devK/k);
        }
        return devSysConcentration;
    }

    /**
     * Getter for the absolute statistical error of the mean of the concentration. If it is 0, it is computed again.
     * @return relative statistical error of the mean of the concentration.
     */
    public double getDevStatConcentration(){
        if(devStatConcentration == 0){
            devStatConcentration = Math.pow(blankResistance/this.getResistance(),1/gamma)
                    * 1/k * Math.sqrt(Math.pow(0,2) + Math.pow(this.getRelativeDevStatResistance(),2));
        }
        return devStatConcentration;
    }

    /**
     * Getter for the absolute standard error of the concentration. If it is 0, it is computed again.
     * @return standard deviation of the concentration.
     */
    public double getDevConcentration() {
        /*if(devConcentration == 0){
            double firstTerm = Math.pow(blank.getResistance()/this.getResistance(),2/gamma);
            double secondTerm = 1/Math.pow(k*this.getConcentration(),2);
            double thirdTerm = Math.pow(blank.getRelativeDevResistance(),2) + Math.pow(this.getRelativeDevResistance(),2);
            this.devConcentration = this.valueList.size() <= 1 ? 0 : Math.sqrt(firstTerm * secondTerm * thirdTerm + Math.pow(devK,2));
        }
        return devConcentration;*/
        if(devConcentration == 0){
            devConcentration = this.getDevStatConcentration() + this.getDevSysConcentration();
        }
        return devConcentration;
    }

    /**
     * Getter for the time
     * @return time in minutes.
     */
    public double getTime() {
        return time;
    }

    /**
     * Gives a String representation of the Measurement formatted as an entry for a CSV table.
     * First the time is shown.
     * Then all values.
     * Then the average.
     * Then the standard deviation of the raw values.
     * Then the resistance.
     * Then the standard deviation of the resistance.
     * Then the concentration.
     * Then the standard deviation of the concentration.
     * @return CSV-entry representation of the Measurement.
     */
    @Override
    public String toCSVString() {
        String result = this.getTime() + ",";
        result += super.toCSVString() + "," + this.getConcentration() + "," + this.getDevConcentration();
        //String result = super.toCSVString() + ",";
        //result += this.getTime() +"," + this.getConcentration() + "," + this.getDevConcentration();
        return result;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public double getXValue() {
        return this.getTime();
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public double getYValue() {
        return this.getConcentration();
    }

    /**
     * For testing.
     */
    public static MeasurementConcentration getExample(){
        return MeasurementConcentration.EXAMPLE;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public Title[] columnsList() {
        return new Title[] {Title.TIME,Title.CONCENTRATION,Title.DEVIATION};
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public double getValueColumn(Title title) throws UnsupportedColumnException {
        switch (title){
            case TIME:
                return this.getTime();
            case CONCENTRATION:
                return this.getConcentration();
            case DEVIATION:
                return this.getDevConcentration();
            default:
                throw new UnsupportedColumnException(MeasurementConcentration.class,title);
        }
    }

    /**
     * For testing
     */
    public static void test(){
        int[] values = {100,200,300,400,500};
        MeasurementBaseVolt ref = new MeasurementBaseVolt();
        MeasurementConcentrationBlank blank = new MeasurementConcentrationBlank(1500000,ref);
        MeasurementConcentration mes = new MeasurementConcentration(1600000,0,3.758,0.135,0.8,0.3,1500000,ref,3);
        for (int val : values){
            ref.saveValue(val);
            blank.saveValue(val+15);
            mes.saveValue(val+30);
        }
        System.out.println("F: "+(Math.pow(blank.getResistance()/mes.getResistance(),1./0.8)));
        System.out.println("Blan res: "+blank.getResistance());
        System.out.println("Blan devRes Sys: "+blank.getRelativeDevSysResistance());
        System.out.println("Blan devRes Stat: "+blank.getRelativeDevStatResistance());
        System.out.println("Mes res: "+mes.getResistance());
        System.out.println("Mes devRes Sys: "+ mes.getRelativeDevSysResistance());
        System.out.println("Mes devRes Stat: "+mes.getRelativeDevStatResistance());
        System.out.println("Computed concentration: "+mes.getConcentration()+". Expected: 0.33104");
        System.out.println("Computed devConc Sys: "+mes.getDevSysConcentration()+". Expected: 0.3090");
        System.out.println("Computed devConc Stat: "+mes.getDevStatConcentration()+". Expected: 4.3276");
        System.out.println("Computed devConcentration: "+mes.getDevConcentration()+". Expected: 4.6363");
    }

}

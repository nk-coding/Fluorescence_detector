package backend.experiment;

/**
 * Parameters needed for a {@link ExperimentConcentration}
 */
public class ParametersConcExperiment extends Parameters {

    /**
     * The Resistance of the blank measurement to be considered for calculations.
     */
    protected double blankResistance;

    /**
     * The standard deviation of the blank measurement's resistance.
     */
    protected double blankResistanceDev;

    /**
     * The gamma constant. Needed to calculate concentration using measured voltages.
     */
    protected double gamma;

    /**
     * Standard devaiton of gamma.
     */
    protected double devGamma;

    /**
     * k constant, in uM^-1. Needed to calculate concentration using measured voltages.
     */
    protected double k;

    /**
     * Standard deviation, in uM^-1 of the k constant.
     */
    protected double devK;


    /**
     * Constructor for ParametersConcExperiment.
     * @param refResistance reference resistance, in Ohm.
     * @param k the k cconstant.
     * @param devK k's standard deviation.
     * @param gamma the gamma constant.
     * @param devGamma gamma's standard deviation.
     */
    public ParametersConcExperiment(double refResistance, double blankResistance, double blankResistanceDev,double k, double devK, double gamma,double devGamma) {
        super(refResistance);
        this.blankResistance = blankResistance;
        this.blankResistanceDev = blankResistanceDev;
        this.gamma = gamma;
        this.devGamma = devGamma;
        this.k= k;
        this.devK = devK;
    }

    /**
     * Getter for gamma.
     * @return gamma.
     */
    public double getGamma() {
        return gamma;
    }

    /**
     * Getter for devGamma.
     * @return devGamma.
     */
    public double getDevGamma(){ return devGamma; }

    /**
     * Getter for the k constant.
     * @return k.
     */
    public double getK() {
        return k;
    }

    /**
     * Getter for devK.
     * @return the standard deviation of k.
     */
    public double getDevK() {
        return devK;
    }

    /**
     * Getter for blankResistance.
     * @return the resistance of the blank.
     */
    public double getBlankResistance() {
        return blankResistance;
    }

    /**
     * Getter for blankResistanceDev.
     * @return the standard deviation of the resistance onf the blank.
     */
    public double getBlankResistanceDev() {
        return blankResistanceDev;
    }
}

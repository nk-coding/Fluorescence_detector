package backend.experiment;

/**
 * Parameters specifying the physical values and constants needed to calculate the different values of an {@link Experiment}.
 */
public class Parameters {

    /**
     * The reference resistance, in Ohm, in Lightbringer's circuit.
     */
    protected double refResistance;

    /**
     * Connstructor for Parameters.
     * @param refResistance reference resistance, in Ohm.
     */
    public Parameters(double refResistance){
        this.refResistance = refResistance;
    }

    /**
     * Getter for refResistance.
     * @return reference resistance.
     */
    public double getRefResistance() {
        return refResistance;
    }
}

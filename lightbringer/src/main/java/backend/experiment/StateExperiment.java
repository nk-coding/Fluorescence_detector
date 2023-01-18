package backend.experiment;

/**
 * Enum describing the current state of an {@link Experiment} regarding its progress.
 */
public enum StateExperiment {

    /**
     * The experiment is ready to take the next measurement.
     */
    READY,

    /**
     * The experiment is locked reading data, i.e. it is in the process of turning on the lamp, receiving the data, etc.
     */
    MEASURING,

    /**
     * The experiment has completed all measurements and is considered over.
     */
    DONE
}

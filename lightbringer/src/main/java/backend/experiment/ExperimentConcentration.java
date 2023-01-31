package backend.experiment;

import backend.Constants;
import backend.experiment.listener.LightBringerNotFoundException;
import backend.experiment.listener.SaveValuesEventListener;
import backend.measurement.MeasurementConcentration;
import backend.measurement.MeasurementConcentrationBlank;
import jssc.SerialPortException;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Experiment for measuring concentration of fluorescein.
 */
public class ExperimentConcentration extends Experiment {

    /**
     * {@link ParametersConcExperiment} of this concentration Experiment.
     */
    protected ParametersConcExperiment param;

    /*
    /**
     * {@link MeasurementConcentrationBlank} of this concentration experiment.
     */
    /*protected MeasurementConcentrationBlank blank;*/

    /**
     * {@link AtomicBoolean} telling whether the measurement loop (the cycle of all measurements for the experiment) has
     * already started and is currently running.
     */
    private AtomicBoolean isLoopRunning = new AtomicBoolean(false);

    /**
     * Constructor for ExperimentConcentration.
     * @param conf configuration of the experiment.
     * @param param parameters of the experiment.
     */
    public ExperimentConcentration(Configuration conf, ParametersConcExperiment param) {
        super(conf);
        this.param = param;
    }

    /*
    /**
     * Measures the blank value of the experiment.
     * @throws SerialPortException if there was an unexpected error with the port @see SerialPortException.
     */
    /*public void measureBlank() throws SerialPortException {
        blank = new MeasurementConcentrationBlank(param,baseVolt);
        System.out.println("Adding listener");
        this.arduinoPort.addEventListener(new SaveValuesEventListener(this,blank));
        this.arduinoPort.writeString("MES" + Constants.SEPARATOR);
        this.setRdyReceived(false);
        this.currState.set(StateExperiment.MEASURING);
        System.out.println("adios");
    }*/

    /**
     * Adds a new {@link MeasurementConcentration} to the list, taking the values fed by the Arduino.
     * @throws SerialPortException if there was an unexpected error with the port @see SerialPortException.
     * @throws InterruptedException if the thread where the measuring is running is unexpectedly interrupted.
     */
    public void measure() throws SerialPortException, InterruptedException {
        synchronized (this.rdyReceived){
            MeasurementConcentration mes = new MeasurementConcentration(this.param,this.baseVolt,this.calculateTimeMeasurement());
            System.out.println("Adding listener");
            this.arduinoPort.addEventListener(new SaveValuesEventListener(this,mes));
            this.arduinoPort.writeString("MES" + Constants.SEPARATOR);
            this.setRdyReceived(false);
            this.currState.set(StateExperiment.MEASURING);
            System.out.println("adios");
            this.rdyReceived.wait();
            this.measurementsList.add(mes);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() throws SerialPortException, LightBringerNotFoundException {
        super.start();
    }

    /**
     * Starts the loop (cycle) of the experiment. Once called, a new measurement will be added in the time lapse specified
     * by the configuration of the experiment.
     * @throws SerialPortException if there was an unexpected error with the port @see SerialPortException.
     * @throws InterruptedException if the thread where the measuring is running is unexpectedly interrupted.
     */
    public void measureLoop() throws SerialPortException, InterruptedException {
        if(!isLoopRunning.get()){
            isLoopRunning.set(true);
            synchronized (isLoopRunning){
                for(int i = 0; i < conf.getNumberOfMeasurments(); i++){
                    if(!arduinoPort.isOpened()){
                        return;
                    }
                    this.measure();
                    /*long wakeUpTime = System.currentTimeMillis() +
                            (conf.getDelayOn() + conf.getNumberOfValuesPerMeasurement() * conf.getDelayBetweenValues()
                                    + conf.getDelayOff() + conf.getDelayBetweenMeasurements());*/
                    long wakeUpTime = System.currentTimeMillis() + conf.getDelayBetweenMeasurements();
                    while(System.currentTimeMillis() < wakeUpTime || !this.isRdyNextMeasurement()){
                        isLoopRunning.wait(wakeUpTime - System.currentTimeMillis());
                    }
                    this.currState.set(StateExperiment.DONE);
                }
            }
            isLoopRunning.set(false);
        }
    }

    /**
     * Calculates the timestamp, in min, for a measurement.
     * @return
     */
    private double calculateTimeMeasurement(){
        return ((this.measurementsList.size()) * (conf.getDelayOn() + conf.getNumberOfValuesPerMeasurement() * conf.getDelayBetweenValues()
                + conf.getDelayOff() + conf.getDelayBetweenMeasurements())/60000.0);
    }

    /*
    /**
     * Getter blank.
     * @return the MeasurementBlank blank.
     */
    /*public MeasurementConcentrationBlank getBlank() {
        return blank;
    }*/

    /**
     * Getter for isLoopRunning.
     * @return true if the cycle has already started and is currently running. False otherwise.
     */
    public boolean isLoopRunning() {
        return isLoopRunning.get();
    }

    /**
     * For testing.
     */
    public static void test(){
        ExperimentConcentration exp = new ExperimentConcentration(new Configuration((byte) 50,(byte) 50,3000,50,50,20000),
                new ParametersConcExperiment(1500000,1600000,0,3.758,0.135,0.8,0));
        try{
            exp.start();
            Scanner sc = new Scanner(System.in);
            int i = sc.nextInt();
            if(i == 0){
                exp.measureBaseVolt();
                sc.nextInt();
                if(i == 0){
                    exp.measureLoop();
                }
            }
        } catch (SerialPortException e) {
            e.printStackTrace();
        } catch (LightBringerNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

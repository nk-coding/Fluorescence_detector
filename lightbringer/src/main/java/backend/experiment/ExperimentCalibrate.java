package backend.experiment;


import backend.Constants;
import backend.experiment.listener.LightBringerNotFoundException;
import backend.experiment.listener.SaveValuesEventListener;
import backend.measurement.Measurement;
import backend.measurement.MeasurementCalibration;
import jssc.SerialPortException;

import java.util.Scanner;

/**
 * Experiment for calibrating Lightbringer.
 */
public class ExperimentCalibrate extends Experiment {

    /**
     *   {@link Parameters} of this calibration experiment.
     */
    protected Parameters param;

    /**
     * Constructor for ExperimentCalibrate.
     * @param conf configuration of the experiment.
     * @param param parameters of the experiment.
     */
    public ExperimentCalibrate(Configuration conf, Parameters param) {
        super(conf);
        this.param = param;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() throws LightBringerNotFoundException, SerialPortException {
        super.start();
    }

    /**
     * Adds a new {@link MeasurementCalibration} to the list, using the given concentration.
     * @param concentration concentration of the measurement
     * @throws SerialPortException if there was an unexpected error with the port @see SerialPortException.
     * @throws InterruptedException if the thread where the measuring is running is unexpectedly interrupted.
     */
    public void measure(double concentration) throws SerialPortException, InterruptedException {
        synchronized (this.rdyReceived){
            MeasurementCalibration newMeasurement = new MeasurementCalibration(concentration,param,this.baseVolt);
            this.arduinoPort.addEventListener(new SaveValuesEventListener(this,newMeasurement));
            this.arduinoPort.writeString("MES" + Constants.SEPARATOR);
            this.setRdyReceived(false);
            this.currState.set(StateExperiment.MEASURING);
            this.rdyReceived.wait();
            this.measurementsList.add(newMeasurement);
        }

    }

    /**
     * For testing.
     */
    public static void test(){
        ExperimentCalibrate exp = new ExperimentCalibrate(new Configuration((byte) 50,(byte) 50,3000,50,50,30),
                new Parameters(1500000));
        try {
            exp.start();
            Scanner sc = new Scanner(System.in);
            while(true){
                int i = sc.nextInt();
                if(i == 0){
                    exp.measure(3);
                } else if(i == 1){
                    for(Measurement mes : exp.measurementsList){
                        System.out.println(mes);
                    }
                }
            }
        } catch (LightBringerNotFoundException e) {
            e.printStackTrace();
        } catch (SerialPortException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

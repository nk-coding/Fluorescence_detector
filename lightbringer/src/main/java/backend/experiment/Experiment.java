package backend.experiment;

import backend.experiment.listener.ArduinoConnectedEventListener;
import backend.experiment.listener.LightBringerNotFoundException;
import backend.experiment.listener.SaveValuesEventListener;
import backend.measurement.Measurement;
import backend.measurement.MeasurementBaseVolt;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Experiment controls the communication with the arduino, takes and saves internally measurements and represents an
 * interface to other for other classes to gain access to the measurements or Arduino processes.
 */
public class Experiment {

    /**
     * The {@link Configuration} of the experiment.
     */
    protected Configuration conf;

    /**
     * The {@link MeasurementBaseVolt} of the experiment.
     */
    protected MeasurementBaseVolt baseVolt;

    /**
     * An observable list containing all relevant measurements in the experiment (with the data of interest).
     */
    protected ObservableList<Measurement> measurementsList;

    /**
     * AtomicBoolean representing if the arduino has sent the currently expected RDY message. Used to recognize if
     * Lightbringer is currently measuring.
     */
    protected AtomicBoolean rdyReceived = new AtomicBoolean(false);
    //protected SimpleBooleanProperty rdyReceived = new SimpleBooleanProperty(false);

    /**
     * AtomicBoolean used for synchornization when finding the port connected to Lightbringer.
     */
    private final AtomicBoolean isLightbringer = new AtomicBoolean(false);

    /**
     *{@link StateExperiment} wrapped around a property used for informing other components of the current state of the experiment.
     */
    protected SimpleObjectProperty<StateExperiment> currState;

    /**
     * {@link SerialPort} bound to the experiment. This object handles the communication between Lightbringer and the experiment.
     */
    protected SerialPort arduinoPort = null;

    /**
     * Cosntructor for Experiment.
     * @param conf the configuration (delays, ammounts of values per measurement, etc) of the experiment.
     */
    public Experiment(Configuration conf){
        measurementsList = FXCollections.observableArrayList();
        this.conf = conf;
        this.currState = new SimpleObjectProperty<>(StateExperiment.READY);
    }


    /**
     * Finds Lightbringer and creates a Serial port connection to it.
     * @return true if connecting to Lightbringer was successful, false otherwise.
     * @throws LightBringerNotFoundException if no active port connection was found or none could be identified as Lightbringer, i.e
     * the port did not received the RDY message.
     * @throws SerialPortException if there was an unexpected error with the port @see SerialPortException.
     */
    private boolean connectToArduino() throws LightBringerNotFoundException, SerialPortException {
        String[] portNames = SerialPortList.getPortNames();
        if(portNames.length == 0){
            throw new LightBringerNotFoundException("No connection with a port was found");
        }
        for (String portName : portNames){
            System.out.println(portName);
            SerialPort tempPort = new SerialPort(portName);
            synchronized (isLightbringer){
                try{
                    tempPort.openPort();
                    tempPort.setParams(
                            SerialPort.BAUDRATE_57600,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE
                    );
                    tempPort.setEventsMask(SerialPort.MASK_RXCHAR); //Tells which kind of events are to be sent through the port.
                    tempPort.addEventListener(new ArduinoConnectedEventListener(isLightbringer));
                    isLightbringer.wait(1000);

                    //If the port is connected to Lightbringer, we remove the event and reset the mask.
                    if(isLightbringer.get()){
                        tempPort.removeEventListener();
                        tempPort.readString();
                        tempPort.setEventsMask(SerialPort.MASK_RXCHAR);
                        arduinoPort = tempPort;
                        break;
                    }
                } catch (InterruptedException | SerialPortException e) {
                    System.out.println("Cannot use " + portName);
                }
            }
        }
        if(!isLightbringer.get()){
            throw new LightBringerNotFoundException("None of the found ports was recognized as Lightbringer.");
        }
        return isLightbringer.get();
    }

    /**
     * Sends Lightbringer the configuration for this experiment.
     * @throws SerialPortException if there was an unexpected error with the port @see SerialPortException.
     */
    private void sendConfiguration() throws SerialPortException {
        arduinoPort.writeString("CON%");
        byte[] temp1 = conf.getDelayOnAsBytes();
        byte[] temp2 = conf.getDelayOffAsBytes();
        byte[] sendArray = new byte[2 + temp1.length + temp2.length];
        sendArray[0] = conf.getDelayBetweenValues();
        sendArray[1] = conf.getNumberOfValuesPerMeasurement();
        for(int i = 0; i < temp1.length; i++){
            sendArray[i + 2] = temp1[i];
        }
        for(int i = 0; i < temp2.length; i++){
            sendArray[i + 2 + temp1.length] = temp2[i];
        }
        arduinoPort.writeBytes(sendArray);
    }

    /**
     * Detaches the current listener in the serial port (if any) and closes it.
     * @throws SerialPortException if there was an unexpected error with the port @see SerialPortException.
     */
    public void disconnectArduino() throws SerialPortException {
        System.out.println("Disconnecting Arduino");
        if(arduinoPort != null) {
            try{
                arduinoPort.writeString("BYE%");
                arduinoPort.removeEventListener();
            } catch (SerialPortException e) {
                //
            }
            if (arduinoPort.isOpened()) {
                arduinoPort.closePort();
            }
        }
    }

    /**
     * Starts an experiment by connecting to Lightbringer, sending the configuration and measuring the base voltage of the circuit.
     * @throws LightBringerNotFoundException if no active port connection was found or none could be identified as Lightbringer, i.e
     * the port did not received the RDY message.
     * @throws SerialPortException if there was an unexpected error with the port @see SerialPortException.
     */
    public void start() throws LightBringerNotFoundException, SerialPortException {
        this.connectToArduino();
        this.sendConfiguration();
        this.measureBaseVolt();
    }

    /**
     * Measures the base voltage of the circuit.
     * @throws SerialPortException if there was an unexpected error with the port @see SerialPortException.
     */
    public void measureBaseVolt() throws SerialPortException {
        baseVolt = new MeasurementBaseVolt();
        System.out.println("Adding listener");
        this.arduinoPort.addEventListener(new SaveValuesEventListener(this,baseVolt));
        this.arduinoPort.writeString("ME1%");
        this.setRdyReceived(false);
        this.currState.set(StateExperiment.MEASURING);
    }

    /**
     * Getter for the {@link SerialPort} connected to Lightbringer.
     * @return {@link SerialPort} connected to Lightbringer.
     */
    public SerialPort getArduinoPort() {
        return arduinoPort;
    }

    /**
     * Getter for isRdyNextMeasurement.
     * @return true if the last expected RDY message has been received, false otherwise.
     */
    public boolean isRdyNextMeasurement() {
        return rdyReceived.get();
    }

    /**
     * Getter for the AtomicBoolean isRdyReceived.
     * @return the AtomicBoolean isRdyReceived.
     */
    public AtomicBoolean getAtomicRdyReceived() {
        return rdyReceived;
    }

    /**
     * Setter for the value of isReadyReceived
     * @param isReady new value.
     */
    public void setRdyReceived(boolean isReady) {
        this.rdyReceived.set(isReady);
    }

    /**
     * Getter for the property containing the current state of the experiment.
     * @return property containing the current state of the experiment.
     */
    public SimpleObjectProperty<StateExperiment> getPropertyCurrState() {
        return currState;
    }

    /**
     * Setter for {@link StateExperiment} wrapped around the ObjectProperty.
     * @param currState new value.
     */
    public void setCurrState(StateExperiment currState) {
        this.currState.set(currState);
    }

    /**
     * Getter for the ObservableList containing all experiment measurements (i.e. not base voltage, blank, etc).
     * @return ObservableList of Measurements.
     */
    public ObservableList<Measurement> getMeasurementsList() {
        return measurementsList;
    }

    public Configuration getConf() {
        return conf;
    }

    /**
     * For testing.
     */
    public static void test(){
        Experiment exp = new Experiment(new Configuration((byte)50,(byte) 25,16777215,0,0,0));
        try {
            exp.start();
        } catch (LightBringerNotFoundException e) {
            e.printStackTrace();
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }
}

package backend.tests;

import backend.experiment.Configuration;
import backend.experiment.Experiment;
import backend.experiment.ExperimentCalibrate;
import backend.experiment.ExperimentConcentration;
import backend.measurement.Measurement;
import backend.measurement.MeasurementBaseVolt;
import backend.measurement.MeasurementConcentration;
import backend.measurement.MeasurementResistance;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class Test {

    //Idea: use a hand shake to find arduion port
    SerialPort arduinoPort = null;
    String[] portNames = SerialPortList.getPortNames();
    int i = 0;



    /**
     * Configures the connecticion between java and the arduino in the port.
     * @param port port where the Arduino is connected
     * @return
     */
    public boolean connectArduino(String port){
        System.out.println("Connecting Arduino");
        boolean success = false;
        SerialPort tempPort = new SerialPort(port);
        try{
            tempPort.openPort();
            tempPort.setParams(
                    SerialPort.BAUDRATE_57600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE
                    );
            tempPort.setEventsMask(SerialPort.MASK_RXCHAR); //Tells which kind of events are to be sent through the port.
            tempPort.addEventListener( (SerialPortEvent serialPortEvent) -> {
                if(serialPortEvent.isRXCHAR()){
                    try{
                        String read = tempPort.readString(serialPortEvent.getEventValue());
                        if(read.equals("I'm Ready")){
                            System.out.println("it's ready!");
                            arduinoPort.writeString("CON");
                        } else{
                            System.out.println(read);
                        }
                    } catch (SerialPortException e) {
                        e.printStackTrace();
                    }
                }
            });

            arduinoPort = tempPort;
            success = true;
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
        return success;
    }

    public void disconnectArduino(){
        System.out.println("Disconnecting Arduino");
        if(arduinoPort != null){
            try{
                arduinoPort.removeEventListener();
                if(arduinoPort.isOpened()){
                    arduinoPort.closePort();
                }
                arduinoPort = null;
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        //Measurement.test();
        //MeasurementBaseVolt.test();
        //MeasurementResistance.test();
        //MeasurementConcentration.test();
        //Configuration.test();
        Experiment.test();
        /*String test = "";
        String[] values = test.split("%");
        for(String val: values){
            System.out.println(val);
        }
        System.out.println(values.length);*/
        //ExperimentCalibrate.test();
        //ExperimentConcentration.test();
        //System.out.println(~Configuration.getDelayOnAsBytes(31280)[0]);
        //System.out.println(Configuration.getDelayOnAsBytes(31280)[1] );
        /*int i = 62768;
        System.out.print((byte)(~(i >> 8)));*/


    }


}


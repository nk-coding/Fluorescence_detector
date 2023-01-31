package backend.tests;

import backend.Constants;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class TestSketch {

    //Idea: use a hand shake to find arduion port
    SerialPort arduinoPort = null;
    String[] portNames = SerialPortList.getPortNames();



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
                        if(read.equals("RDY")){
                            System.out.println("It's ready");
                            this.run();
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

    public void run(){
        byte delayBetweenMeasurements = 50;
        byte ammountPointsPerMeasurement = 50;
        byte delayOn1 = 0b10111;
        byte delayOn2 = 0b0111000; //3000 ms
        byte delayOff1 = 0b11;
        byte delayOff2 = 0b1110100;
        try {
            this.arduinoPort.writeString("CON" + Constants.SEPARATOR);
            this.arduinoPort.writeBytes(new byte[]{delayBetweenMeasurements,ammountPointsPerMeasurement,delayOn1,delayOn2,delayOff1,delayOff2});
            this.arduinoPort.writeString("ME1" + Constants.SEPARATOR);
            this.arduinoPort.writeString("MES" + Constants.SEPARATOR);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    public void disconnectArduino(){
        System.out.println("Disconnecting Arduino");
        if(arduinoPort != null){
            try{
                arduinoPort.removeEventListener();
                if(arduinoPort.isOpened()){
                    arduinoPort.closePort();
                }
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        TestSketch myTest = new TestSketch();
        myTest.connectArduino(myTest.portNames[0]);


    }
}

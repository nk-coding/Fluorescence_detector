package backend.experiment.listener;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import java.util.concurrent.atomic.AtomicBoolean;

public class ArduinoConnectedEventListener implements SerialPortEventListener{

    private AtomicBoolean isLightbringer;
    private SerialPort tempPort;
    private String readSting = "";

    public ArduinoConnectedEventListener(AtomicBoolean isLightbringer, SerialPort tempPort){
        this.isLightbringer = isLightbringer;
        this.tempPort = tempPort;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        synchronized(isLightbringer){
            if(serialPortEvent.isRXCHAR()){
                try{
                    readSting += tempPort.readString(serialPortEvent.getEventValue());
                    if(readSting.contains("RDY")){
                        tempPort.writeString("ACK%");
                        System.out.println("RDY recieved");
                        isLightbringer.set(true);
                        isLightbringer.notify();
                    }
                } catch (SerialPortException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

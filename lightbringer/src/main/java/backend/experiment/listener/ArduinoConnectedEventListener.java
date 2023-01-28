package backend.experiment.listener;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import java.util.concurrent.atomic.AtomicBoolean;

public class ArduinoConnectedEventListener extends BaseListener{

    private final AtomicBoolean isLightbringer;

    public ArduinoConnectedEventListener(AtomicBoolean isLightbringer){
        this.isLightbringer = isLightbringer;
    }

    @Override
    void handleMessage(String message, SerialPort port) {
        synchronized(isLightbringer){
            if(message.contains("RDY")){
                try {
                    port.writeString("ACK%");
                } catch (SerialPortException e) {
                    throw new RuntimeException("Cannot write ACK", e);
                }
                System.out.println("RDY recieved");
                isLightbringer.set(true);
                isLightbringer.notify();
            }
        }
    }
}

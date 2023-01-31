package backend.experiment.listener;

import backend.Constants;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public abstract class BaseListener implements SerialPortEventListener {

    String lastFrag = "";

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        final SerialPort port = serialPortEvent.getPort();
        if (serialPortEvent.isRXCHAR()) {
            try {
                if (serialPortEvent.getEventValue() > 0) {
                    String read = port.readString(serialPortEvent.getEventValue());
                    String readWithLast = lastFrag + read;
                    this.lastFrag = "";
                    String[] values = readWithLast.split(Constants.SEPARATOR);
                    for (int i = 0; i < values.length; i++) {
                        String value = values[i];
                        if (i == values.length - 1 && !read.endsWith(Constants.SEPARATOR)) {
                            lastFrag = value;
                        } else {
                            System.out.println("message: " + value);
                            this.handleMessage(value, port);
                        }
                    }
                }
            } catch (SerialPortException e) {
                System.out.println("exp in Save values");
                e.printStackTrace();
            }
        }
    }

    abstract void handleMessage(String message, SerialPort port);

}

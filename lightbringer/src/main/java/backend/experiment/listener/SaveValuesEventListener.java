package backend.experiment.listener;

import backend.experiment.Experiment;
import backend.experiment.StateExperiment;
import backend.measurement.Measurement;
import backend.measurement.MeasurementConcentration;
import backend.measurement.MeasurementConcentrationBlank;
import backend.measurement.MeasurementResistance;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class SaveValuesEventListener implements SerialPortEventListener {

    Measurement mes;
    Experiment exp;
    String lastFrag = "";

    public SaveValuesEventListener(Experiment exp, Measurement measurement) {
        this.exp = exp;
        this.mes = measurement;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        synchronized (this.exp.getAtomicRdyReceived()) {
            if (serialPortEvent.isRXCHAR()) {
                try {
                    if (serialPortEvent.getEventValue() > 0) {
                        String read = this.exp.getArduinoPort().readString(serialPortEvent.getEventValue());
                        String readWithLast = lastFrag + read;
                        this.lastFrag = "";
                        String[] values = readWithLast.split("%");
                        for (int i = 0; i < values.length; i++) {
                            String value = values[i];
                            if (i == values.length - 1 && !read.endsWith("%")) {
                                lastFrag = value;
                            } else {
                                System.out.println("value: " + value);
                                if (value.contains("RDY")) {
                                    System.out.println("REmoving Listener");
                                    this.exp.getArduinoPort().removeEventListener();
                                    this.exp.setRdyReceived(true);
                                    this.exp.setCurrState(StateExperiment.READY);
                                    this.exp.getAtomicRdyReceived().notify();
                                } else if (value.matches("[0-9]+")) {
                                    mes.saveValue(Integer.parseInt(value));
                                } else {
                                    System.err.println("Warning: received unknown message: " + value);
                                }
                            }
                        }
                    }
                } catch (SerialPortException e) {
                    System.out.println("exp in Save values");
                    e.printStackTrace();
                }
            }
        }
    }

    private void printDebugInfo() {
        if (mes instanceof MeasurementConcentration mesC) {
            System.out.println("mes Con: " + mesC.getConcentration());
            System.out.println("mes devCon: " + mesC.getDevConcentration());
        } else if (mes instanceof MeasurementConcentrationBlank blank) {
            System.out.println("blank res: " + blank.getResistance());
            System.out.println("blank dev res: " + blank.getRelativeDevResistance());
            System.out.println("blank Con: " + blank.getConcentration());
        } else if (mes instanceof MeasurementResistance res) {
            System.out.println("res res: " + res.getResistance());
            System.out.println("res dev res: " + res.getRelativeDevResistance());
        } else {
            System.out.println("new Mes Res: " + mes.getAverage());
            System.out.println("new Mes ResDev: " + mes.getRelativeDeviation());
        }
    }
}

package backend.experiment.listener;

import backend.experiment.Experiment;
import backend.experiment.StateExperiment;
import backend.measurement.Measurement;
import backend.measurement.MeasurementConcentration;
import backend.measurement.MeasurementConcentrationBlank;
import backend.measurement.MeasurementResistance;
import jssc.SerialPort;
import jssc.SerialPortException;

public class SaveValuesEventListener extends BaseListener {

    Measurement mes;
    Experiment exp;

    public SaveValuesEventListener(Experiment exp, Measurement measurement) {
        this.exp = exp;
        this.mes = measurement;
    }

    @Override
    void handleMessage(String message, SerialPort port) {
        synchronized (this.exp.getAtomicRdyReceived()) {
            try {
                if (message.contains("RDY")) {
                    System.out.println("Removing Listener");
                    this.exp.getArduinoPort().removeEventListener();
                    this.exp.setRdyReceived(true);
                    this.exp.setCurrState(StateExperiment.READY);
                    this.exp.getAtomicRdyReceived().notify();
                } else if (message.matches("[0-9]+")) {
                    mes.saveValue(Integer.parseInt(message));
                } else {
                    System.out.println("Warning: received unknown message: " + message);
                }
            } catch (SerialPortException e) {
                System.out.println("exp in Save values");
                e.printStackTrace();
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

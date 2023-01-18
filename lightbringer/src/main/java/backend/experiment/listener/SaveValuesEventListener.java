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

    public SaveValuesEventListener(Experiment exp, Measurement measurement){
        this.exp = exp;
        this.mes = measurement;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        synchronized (this.exp.getAtomicRdyReceived()){
            if(serialPortEvent.isRXCHAR()){
                try{
                    if(serialPortEvent.getEventValue() > 0){
                        String read = this.exp.getArduinoPort().readString(serialPortEvent.getEventValue());
                        System.out.println("Event: "+read);
                        System.out.println("lastFrag: "+lastFrag + "!");
                        //String read = lastFrag + this.exp.getArduinoPort().readString(serialPortEvent.getEventValue());
                        String[] values = read.split("%");
                        for(int i = 0; i < values.length; i++){
                            String value = values[i];
                            if (i == values.length - 1){
                                lastFrag = read.endsWith("%") ? "" : value;
                            }

                            System.out.println(value+"---xd");
                            if(value.contains("RDY")){
                                System.out.println("REmoving Listener");
                                this.exp.getArduinoPort().removeEventListener();
                                this.exp.setRdyReceived(true);
                                this.exp.setCurrState(StateExperiment.READY);
                                this.exp.getAtomicRdyReceived().notify();
                                //Debuggig:
                                /*if(mes instanceof MeasurementConcentration){
                                    MeasurementConcentration mesC = (MeasurementConcentration) mes;
                                    System.out.println("mes Con: "+mesC.getConcentration());
                                    System.out.println("mes devCon: "+mesC.getDevConcentration());
                                } else if(mes instanceof MeasurementConcentrationBlank){
                                    MeasurementConcentrationBlank blank = (MeasurementConcentrationBlank) mes;
                                    System.out.println("blank res: "+blank.getResistance());
                                    System.out.println("blank dev res: "+blank.getRelativeDevResistance());
                                    System.out.println("blank Con: "+blank.getConcentration());
                                } else if(mes instanceof MeasurementResistance){
                                    MeasurementResistance res = (MeasurementResistance) mes;
                                    System.out.println("res res: "+res.getResistance());
                                    System.out.println("res dev res: "+res.getRelativeDevResistance());
                                } else{
                                    System.out.println("new Mes Res: "+mes.getAverage());
                                    System.out.println("new Mes ResDev: "+mes.getRelativeDeviation());
                                }*/
                            } else {
                                mes.saveValue(Integer.parseInt(value));
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
}

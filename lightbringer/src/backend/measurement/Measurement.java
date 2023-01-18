package backend.measurement;

import java.util.ArrayList;
import java.util.List;

/**
 * Measurement contains the information of a measurement performed by Lightbringer, that is, the raw received values, which
 * are saved in a list. It also presents methods to calculate some values on the data, like average, deviation and others,
 * depending on the concrete class.
 */
public class Measurement{

    /**
     * List with all the raw voltages (between 0 and 1023) of the measurement.
     */
    protected List<Integer> valueList;

    /**
     * Average of the list's values. 0 if the list is empty.
     */
    private double average;

    /**
     * Standard deviation of the list's values. 0 if the list is empty.
     */
    private double deviation;

    /**
     * Constructor for Measurement. Initializes the valuesList as an ArrayList with 20 (empty) entries.
     */
    public Measurement(){
        valueList = new ArrayList<>(20);
    }

    /**
     * Constructor for Measurement. Initializes the valuesList as an ArrayList with as many (empty) entries as specified..
     * @param numberValues initial number of entries in the ArrayList.
     */
    public Measurement(int numberValues){
        valueList = new ArrayList<>(numberValues);
    }

    /**
     * Saves a single value by adding to the valuesList.
     * @param value
     */
    public void saveValue(int value){
        valueList.add(value);
    }

    /**
     * Getter for the average of the measurement. If the average is 0, it is computed again.
     * @return average of the values in valuesList.
     */
    public double getAverage(){
        if(this.average == 0){
            for( int value : valueList){
                this.average += value;
            }
            this.average = valueList.size() == 0 ? 0 : (this.average /= valueList.size());
        }
        return  this.average;
    }

    /**
     * Getter for the relative standard error of the mean of the measurement. If the standard deviation is 0, it is computed again.
     * @return the absolute standard deviation.
     */
    public double getRelativeDeviation(){
        if(this.deviation == 0){
            for(int value : valueList){
                this.deviation += Math.pow((this.getAverage()- value),2);
            }
            this.deviation = valueList.size() == 0 ? 0 : (Math.sqrt(deviation) /(this.getAverage() * Math.sqrt(valueList.size()*(valueList.size()-1))));
        }
        return this.deviation;
    }

    /**
     * Gives a String representation of the Measurement formatted as an entry for a CSV table.
     * First all values are shown.
     * Then the average.
     * Then the standard deviation.
     * @return CSV-entry representation of the Measurement.
     */
    public String toCSVString(){
        String result  = "";
        for (double value : this.valueList){
            result += value + ",";
        }
        result += this.getAverage() +"," + this.getRelativeDeviation();
        return result;
    }

    /**
     * For testing.
     */
    public static void test(){
        int[] values = {100,200,300,400,500};
        Measurement mes = new Measurement();
        for (int val : values){
            mes.saveValue(val);
        }
        System.out.println("Computed deviation: "+mes.getRelativeDeviation()+". Expected: "+0.2357);
        System.out.println("Computed average: "+mes.getAverage()+". Expected: "+300);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString(){
        return "Average: "+ this.getAverage() +". Dev: "+this.getRelativeDeviation();
    }
}

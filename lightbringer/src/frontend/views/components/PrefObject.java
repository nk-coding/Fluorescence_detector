package frontend.views.components;

import java.io.Serializable;

public class PrefObject implements Serializable {

    private double resistance;
    private double gamma;
    private double devGamma;
    private double k;
    private double devK;
    private double blankResistance;
    private double blankDevResistance;

    private int timeBetweenValues;
    private int valuesPerPoint;
    private int delayOn;
    private int delayOff;
    private int numberOfMeasurements;
    private int timeBetweenMeasurements;

    private PrefObject(){

    }

    public double getResistance() {
        return resistance;
    }

    public void setResistance(double resistance) {
        this.resistance = resistance;
    }

    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public double getDevGamma() {
        return devGamma;
    }

    public void setDevGamma(double devGamma) {
        this.devGamma = devGamma;
    }

    public double getK() {
        return k;
    }

    public void setK(double k) {
        this.k = k;
    }

    public double getDevK() {
        return devK;
    }

    public void setDevK(double devK) {
        this.devK = devK;
    }

    public double getBlankResistance() {
        return blankResistance;
    }

    public void setBlankResistance(double blankResistance) {
        this.blankResistance = blankResistance;
    }

    public double getBlankDevResistance() {
        return blankDevResistance;
    }

    public void setBlankDevResistance(double blankDevResistance) {
        this.blankDevResistance = blankDevResistance;
    }

    public int getTimeBetweenValues() {
        return timeBetweenValues;
    }

    public void setTimeBetweenValues(int timeBetweenValues) {
        this.timeBetweenValues = timeBetweenValues;
    }

    public int getValuesPerPoint() {
        return valuesPerPoint;
    }

    public void setValuesPerPoint(int valuesPerPoint) {
        this.valuesPerPoint = valuesPerPoint;
    }

    public int getDelayOn() {
        return delayOn;
    }

    public void setDelayOn(int delayOn) {
        this.delayOn = delayOn;
    }

    public int getDelayOff() {
        return delayOff;
    }

    public void setDelayOff(int delayOff) {
        this.delayOff = delayOff;
    }

    public int getNumberOfMeasurements() {
        return numberOfMeasurements;
    }

    public void setNumberOfMeasurements(int numberOfMeasurements) {
        this.numberOfMeasurements = numberOfMeasurements;
    }

    public int getTimeBetweenMeasurements() {
        return timeBetweenMeasurements;
    }

    public void setTimeBetweenMeasurements(int timeBetweenMeasurements) {
        this.timeBetweenMeasurements = timeBetweenMeasurements;
    }

    static PrefObject getDefault(){
        PrefObject result = new PrefObject();

        result.setResistance(1500000);
        result.setGamma(0.8);
        result.setDevGamma(0);
        result.setK(3.758);
        result.setDevK(0);
        result.setBlankResistance(1600000);
        result.setBlankDevResistance(0);

        result.setTimeBetweenValues(50);
        result.setValuesPerPoint(50);
        result.setDelayOn(5000);
        result.setDelayOff(500);
        result.setNumberOfMeasurements(12);
        result.setTimeBetweenMeasurements(12000);


        return result;

    }
}

package teamlld.nik.uniobuda.hu.walletapp.models;

/**
 * Created by admin on 2017. 05. 01..
 */

public class Currency {

    private String name;
    private double value;

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public Currency() {
    }

    public Currency(String name, double value) {
        this.name = name;
        this.value = value;
    }
}

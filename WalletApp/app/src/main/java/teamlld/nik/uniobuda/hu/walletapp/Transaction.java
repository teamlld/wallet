package teamlld.nik.uniobuda.hu.walletapp;

import java.util.Date;

/**
 * Created by Atee on 2017. 03. 11..
 */

public class Transaction {
    private String name;
    private int value;
    private boolean income;
    private Date date;
    private String type; //TODO típus jó lesz stringként?

    public Transaction(String name, int value, boolean income, String type) {
        this.name = name;
        this.value = value;
        this.income = income;
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public boolean isIncome() {
        return income;
    }
}

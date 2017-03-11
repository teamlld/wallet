package teamlld.nik.uniobuda.hu.walletapp;

/**
 * Created by Atee on 2017. 03. 11..
 */

public class Transaction {
    private String name;
    private int amount;
    private String currency;
    private boolean income;

    public Transaction(String name, int amount, String currency, boolean income) {
        this.name = name;
        this.amount = amount;
        this.currency = currency;
        this.income = income;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public boolean isIncome() {
        return income;
    }
}

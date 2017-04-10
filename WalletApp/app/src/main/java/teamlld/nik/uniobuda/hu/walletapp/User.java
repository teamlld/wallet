package teamlld.nik.uniobuda.hu.walletapp;

/**
 * Created by Atee on 2017. 04. 09..
 */

public class User {
    private String name;
    private int balance;

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getName() {

        return name;
    }

    public int getBalance() {
        return balance;
    }

    public User(String name, int balance) {

        this.name = name;
        this.balance = balance;
    }
}

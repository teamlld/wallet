package teamlld.nik.uniobuda.hu.walletapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Atee on 2017. 04. 09..
 */

public class User implements Parcelable{
    private int id;
    private String name;
    private int balance;
    private int startingBalance;

    protected User(Parcel in) {
        id = in.readInt();
        name = in.readString();
        balance = in.readInt();
        startingBalance=in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(balance);
        dest.writeInt(startingBalance);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public String getName() {

        return name;
    }

    public int getBalance() {
        return balance;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStartingBalance(){return startingBalance;}

    public void setStartingBalance(int stBalance){startingBalance=stBalance;}

    public User(String name, int startingBalance, int id) {

        this.name = name;
        this.startingBalance = startingBalance;
        this.balance=startingBalance;
        this.id = id;
    }

}

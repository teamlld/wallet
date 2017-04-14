package teamlld.nik.uniobuda.hu.walletapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Atee on 2017. 03. 11..
 */

public class Transaction implements Parcelable{
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

    protected Transaction(Parcel in) {
        name = in.readString();
        value = in.readInt();
        income = in.readByte() != 0;
        type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(value);
        dest.writeByte((byte) (income ? 1 : 0));
        dest.writeString(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

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

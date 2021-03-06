package teamlld.nik.uniobuda.hu.walletapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Atee on 2017. 03. 11..
 */

public class Transaction implements Parcelable {
    private String name;
    private int value;
    private boolean income;
    private int typeId;
    private long date;

    public Transaction(String name, int value, boolean income, int typeId,long dateTime) {
        this.name = name;
        this.value = value;
        this.income = income;
        this.typeId = typeId;
        this.date=dateTime;
    }

    protected Transaction(Parcel in) {
        name = in.readString();
        value = in.readInt();
        income = in.readByte() != 0;
        typeId = in.readInt();
        date = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(value);
        dest.writeByte((byte) (income ? 1 : 0));
        dest.writeInt(typeId);
        dest.writeLong(date);
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

    public long getDate() {
        return date;
    }

    public int getTypeId() {
        return typeId;
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

package teamlld.nik.uniobuda.hu.walletapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2017. 05. 01..
 */

public class Currency implements Parcelable {

    private String name;
    private double value;

    protected Currency(Parcel in) {
        name = in.readString();
        value = in.readDouble();
    }

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

    @Override
    public String toString() {
        return this.name + " " + this.value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(value);
    }

    public static final Creator<Currency> CREATOR = new Creator<Currency>() {
        @Override
        public Currency createFromParcel(Parcel in) {
            return new Currency(in);
        }

        @Override
        public Currency[] newArray(int size) {
            return new Currency[size];
        }
    };
}

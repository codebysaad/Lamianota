package com.androidkudus.lamianota.database.keu;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Keu implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "money")
    private String money;
    //ket. money in/out
    @ColumnInfo(name = "from")
    private String from;
    @ColumnInfo(name = "date")
    private String dateIn;
    //if status true = money In
    //else status false = money out
    @ColumnInfo(name = "statusIn")
    private boolean statusIn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDateIn() {
        return dateIn;
    }

    public void setDateIn(String dateIn) {
        this.dateIn = dateIn;
    }

    public boolean isStatusIn() {
        return statusIn;
    }

    public void setStatusIn(boolean statusIn) {
        this.statusIn = statusIn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.money);
        dest.writeString(this.from);
        dest.writeString(this.dateIn);
        dest.writeByte(this.statusIn ? (byte) 1 : (byte) 0);
    }

    public Keu() {
    }

    protected Keu(Parcel in) {
        this.id = in.readInt();
        this.money = in.readString();
        this.from = in.readString();
        this.dateIn = in.readString();
        this.statusIn = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Keu> CREATOR = new Parcelable.Creator<Keu>() {
        @Override
        public Keu createFromParcel(Parcel source) {
            return new Keu(source);
        }

        @Override
        public Keu[] newArray(int size) {
            return new Keu[size];
        }
    };
}

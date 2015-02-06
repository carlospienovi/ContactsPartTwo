package com.carlospienovi.contactsparttwo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import java.io.ByteArrayOutputStream;

public class Contact implements Parcelable {

    public static final String ID = "_id";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String NICKNAME = "nickname";
    public static final String IMAGE = "image";

    @DatabaseField(generatedId = true, columnName = ID)
    private int id;
    @DatabaseField(columnName = FIRST_NAME)
    private String firstName;
    @DatabaseField(columnName = LAST_NAME)
    private String lastName;
    @DatabaseField(columnName = NICKNAME)
    private String nickname;
    @DatabaseField(columnName = IMAGE, dataType = DataType.BYTE_ARRAY)
    private byte[] image;

    public Contact() {

    }

    public Contact(String firstName, String lastName, String nickname, Bitmap image) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickname = nickname;
        this.image = bitmapToByteArray(image);
    }

    public Contact(Parcel in) {
        this.setFirstName(in.readString());
        this.setLastName(in.readString());
        this.setNickname(in.readString());
        this.setImage((Bitmap) in.readParcelable(getClass().getClassLoader()));
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Bitmap getImage() {
        return byteArrayToBitmap(image);
    }

    public void setImage(Bitmap image) {
        this.image = bitmapToByteArray(image);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel destination, int flags) {
        destination.writeString(this.getFirstName());
        destination.writeString(this.getLastName());
        destination.writeString(this.getNickname());
        destination.writeParcelable(this.getImage(), flags);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        public Contact[] newArray(int size) {
            return new Contact[size];
        }

    };

    public String toString() {
        return this.firstName + " : " + this.lastName + " : " + this.nickname;
    }

    private byte[] bitmapToByteArray(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private Bitmap byteArrayToBitmap(byte[] array) {
        return BitmapFactory.decodeByteArray(array, 0, array.length);
    }
}

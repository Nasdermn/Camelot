package com.example.camelot;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class AudioModel implements Parcelable {
    String path;
    String title;
    String duration;
    String artist;
    Date dateAdded;

    public AudioModel(String path, String title, String duration, String artist) {
        this.path = path;
        this.title = title;
        this.duration = duration;
        this.dateAdded = new Date(); // Установка текущей даты при создании объекта
        this.artist = artist;
    }

    private boolean isPlaying;

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return duration;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public String getArtist() {
        return artist;
    }

    // Реализация Parcelable
    protected AudioModel(Parcel in) {
        path = in.readString();
        title = in.readString();
        duration = in.readString();
        dateAdded = new Date(in.readLong());
    }

    public static final Parcelable.Creator<AudioModel> CREATOR = new Parcelable.Creator<AudioModel>() {
        @Override
        public AudioModel createFromParcel(Parcel in) {
            return new AudioModel(in);
        }

        @Override
        public AudioModel[] newArray(int size) {
            return new AudioModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(title);
        dest.writeString(duration);
        dest.writeLong(dateAdded.getTime());
    }
}
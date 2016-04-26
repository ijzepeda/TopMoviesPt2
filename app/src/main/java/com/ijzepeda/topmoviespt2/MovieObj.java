package com.ijzepeda.topmoviespt2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ivan.zepeda on 24/11/2015.
 */
public class MovieObj implements Parcelable {
    public static final Creator<MovieObj> CREATOR = new Creator<MovieObj>() {
        public MovieObj createFromParcel(Parcel source) {
            MovieObj movieObj = new MovieObj();
            movieObj.id = source.readString();
            movieObj.title = source.readString();
            movieObj.poster = source.readString();
            movieObj.originalTitle = source.readString();
            movieObj.releaseDate = source.readString();
            movieObj.popularity = source.readString();
            movieObj.overview = source.readString();
            movieObj.votedAvg = source.readString();
//            movieObj. = source.readInt();

            return movieObj;
        }

        public MovieObj[] newArray(int size) {
            return new MovieObj[size];
        }
    };
    public String id;
    public String title;
    public String poster;
    public String originalTitle;
    public String releaseDate;
    public String popularity;
    public String overview;
    public String votedAvg;

    public MovieObj(String id, String title, String poster, String originalTitle, String releaseDate, String popularity, String overview, String votedAvg) {
        this.id = id;
        this.title = title;
        this.poster = poster;
        this.originalTitle = originalTitle;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
        this.overview = overview;
        this.votedAvg = votedAvg;
    }

    public MovieObj() {
    }

    public MovieObj(String id) {
        this.id = id;
    }

    public String getVotedAvg() {
        return votedAvg;
    }

    public void setVotedAvg(String votedAvg) {
        this.votedAvg = votedAvg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(poster);
        parcel.writeString(originalTitle);
        parcel.writeString(releaseDate);
        parcel.writeString(popularity);
        parcel.writeString(overview);
        parcel.writeString(votedAvg);
//        parcel.writeInt();
    }
}

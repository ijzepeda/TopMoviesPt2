package com.ijzepeda.topmoviespt2.models;

/**
 * Created by ivan.zepeda on 14/01/2016.
 */
public class Video {
    String videoId;
    String videoISO;
    String videoKey;
    String videoName;
    String videoSite;
    String videoSize;
    String videoType;

    public Video() {
    }

    public Video(String videoId, String videoName, String videoSite, String videoType, String videoKey) {
        this.videoId = videoId;
        this.videoName = videoName;
        this.videoSite = videoSite;
        this.videoType = videoType;
        this.videoKey = videoKey;
    }

    public Video(String videoKey, String videoName, String videoSite, String videoType) {
        this.videoKey = videoKey;
        this.videoName = videoName;
        this.videoSite = videoSite;
        this.videoType = videoType;
    }

    public Video(String videoId, String videoISO, String videoKey, String videoName, String videoSite, String videoSize, String videoType) {
        this.videoId = videoId;
        this.videoISO = videoISO;
        this.videoKey = videoKey;
        this.videoName = videoName;
        this.videoSite = videoSite;
        this.videoSize = videoSize;
        this.videoType = videoType;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoISO() {
        return videoISO;
    }

    public void setVideoISO(String videoISO) {
        this.videoISO = videoISO;
    }

    public String getVideoKey() {
        return videoKey;
    }

    public void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoSite() {
        return videoSite;
    }

    public void setVideoSite(String videoSite) {
        this.videoSite = videoSite;
    }

    public String getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(String videoSize) {
        this.videoSize = videoSize;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }
}

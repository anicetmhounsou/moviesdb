package com.hawksteam.movies.data.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.hawksteam.movies.data.remote.ApiEndPoint;
import com.hawksteam.movies.utils.AppConstants;

@Entity(tableName = "video")
public class VideoEntity {

    public static final String SITE_YOUTUBE = "YouTube";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "site")
    private String site;

    @ColumnInfo(name = "videoid")
    private String videoId;

    @ColumnInfo(name = "size")
    private int size;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "movieid")
    private String movieid;

    public VideoEntity(String id, String name, String site, String videoId, int size, String type, String movieid) {
        this.id = id;
        this.name = name;
        this.site = site;
        this.videoId = videoId;
        this.size = size;
        this.type = type;
        this.movieid = movieid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMovieid() {
        return movieid;
    }

    public void setMovieid(String movieid) {
        this.movieid = movieid;
    }

    public static String getUrl(VideoEntity video) {
        if (SITE_YOUTUBE.equalsIgnoreCase(video.getSite())) {
            return String.format(ApiEndPoint.YOUTUBE_VIDEO_URL, video.getVideoId());
        } else {
            return AppConstants.EMPTY;
        }
    }

    public static String getThumbnailUrl(VideoEntity video) {
        if (SITE_YOUTUBE.equalsIgnoreCase(video.getSite())) {
            return String.format(ApiEndPoint.YOUTUBE_THUMBNAIL_URL, video.getVideoId());
        } else {
            return AppConstants.EMPTY;
        }
    }
}

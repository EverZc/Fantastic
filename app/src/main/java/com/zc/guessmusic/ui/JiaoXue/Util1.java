package com.zc.guessmusic.ui.JiaoXue;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/20 0020.
 */
public class Util1 implements Serializable {
    private String id;
    private String category;
    private String cover;
    private String length;
    private String title;
    private String url;

    public Util1() {

    }

    public Util1(String id, String category, String cover, String length, String title, String url) {
        this.id = id;
        this.category = category;
        this.cover = cover;
        this.length = length;
        this.title = title;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

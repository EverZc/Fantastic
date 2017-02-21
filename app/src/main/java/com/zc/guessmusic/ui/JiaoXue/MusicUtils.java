package com.zc.guessmusic.ui.JiaoXue;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/26 0026.
 */
public class MusicUtils implements Serializable {
    private String disc_id;
    private String disc_img_url;
    private String disc_name;
    private String page_id;
    private String is_free;

    public MusicUtils() {
    }

    public MusicUtils(String disc_id, String disc_img_url, String disc_name, String page_id, String is_free) {
        this.disc_id = disc_id;
        this.disc_img_url = disc_img_url;
        this.disc_name = disc_name;
        this.page_id = page_id;
        this.is_free = is_free;
    }

    public String getDisc_id() {
        return disc_id;
    }

    public void setDisc_id(String disc_id) {
        this.disc_id = disc_id;
    }

    public String getDisc_img_url() {
        return disc_img_url;
    }

    public void setDisc_img_url(String disc_img_url) {
        this.disc_img_url = disc_img_url;
    }

    public String getDisc_name() {
        return disc_name;
    }

    public void setDisc_name(String disc_name) {
        this.disc_name = disc_name;
    }

    public String getPage_id() {
        return page_id;
    }

    public void setPage_id(String page_id) {
        this.page_id = page_id;
    }

    public String getIs_free() {
        return is_free;
    }

    public void setIs_free(String is_free) {
        this.is_free = is_free;
    }
}

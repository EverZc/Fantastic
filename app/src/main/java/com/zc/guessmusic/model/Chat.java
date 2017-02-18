package com.zc.guessmusic.model;

import org.litepal.crud.DataSupport;

/**
 * Created by Panda_Program on 2017/2/16 0016.
 */

public class Chat extends DataSupport {
    private int id;
    private int type;
    private String text;
    private String name;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

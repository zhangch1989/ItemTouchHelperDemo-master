package com.zch.tooldemos.demochannel;

import java.io.Serializable;

/**
 * 频道实体类
 * Created by YoKeyword on 15/12/29.
 */
public class ChannelEntity implements Serializable {

    private long id;
    private String name;
    private String url;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ChannelEntity(){

    }

    public ChannelEntity(String name){
        this.name = name;
    }

    public ChannelEntity(Long id, String name){
        this.id = id;
        this.name = name;
    }
}

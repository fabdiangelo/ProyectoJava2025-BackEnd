package com.Tisj.api.pojo;

import com.google.api.services.youtube.YouTube;

import java.util.Date;

public class Snippet{
    public Date publishedAt;
    public String channelId;
    public String title;
    public String description;
    public YouTube.Thumbnails thumbnails;
    public String channelTitle;
    public String categoryId;
    public String liveBroadcastContent;
    public Localized localized;
}

package com.Tisj.api.pojo;

import com.google.api.services.youtube.model.PageInfo;

import java.util.ArrayList;

public class Root{
    public String kind;
    public String etag;
    public ArrayList<Item> items;
    public PageInfo pageInfo;
}

package com.example.grubmate.grubmate.dataClass;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tianhangliu on 10/12/17.
 */

public class Post implements Serializable {
    public Integer postID;
    public Integer posterID;
    public String title;
    public Boolean isHomeMade;
    public String[] postPhotos;
    public String[] tags;
    public String category;
    public String timePeriod;
    public String description;
    public Double[] address;
    public Integer[] groupIDs;
    public Integer totalQuantity;
    public Integer leftQuantity;
    public ArrayList<Integer> requestsIDs;
    public Boolean isActive;
    public Boolean[] allergyInfo;
}

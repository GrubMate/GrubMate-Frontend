package com.example.grubmate.grubmate.dataClass;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {
    public Integer groupID;
    public Integer groupOwnerID;
    public String groupName;
    public ArrayList<Integer> memberIDs;
    public boolean allFriendFlag;
}

package com.example.grubmate.grubmate.dataClass;

import java.io.Serializable;

public class Group implements Serializable {
    public Integer groupID;
    public Integer groupOwnerID;
    public String groupName;
    public Integer[] memberIDs;
    public boolean allFriendFlag;
}

package com.example.grubmate.grubmate;

import com.example.grubmate.grubmate.utilities.PersistantDataManager;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class PersistantDataManagerUnitTest {

    @BeforeClass
    public static void setUp(){

    }

    @Before
    public void setUpForEachTest(){

    }

    @Test
    public void UserID() throws Exception {
        PersistantDataManager.setUserID(123);
        assertEquals(123, PersistantDataManager.getUserID());
    }

    @Test
    public void GroupID() throws Exception {
        ArrayList<Integer> groupIDs = new ArrayList<Integer>();
        for(int i =0 ;i<5;i++){
            groupIDs.add(i);
        }
        PersistantDataManager.setGroupIDs(groupIDs);
        assertEquals(groupIDs, PersistantDataManager.getGroupIDs());
    }


}
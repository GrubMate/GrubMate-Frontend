package com.example.grubmate.grubmate;

import com.example.grubmate.grubmate.dataClass.Notification;
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
    public void testSetGroupID() throws Exception {
        ArrayList<Integer> groupIDs = new ArrayList<Integer>();
        for(int i =0 ;i<5;i++){
            groupIDs.add(i);
        }
        PersistantDataManager.setGroupIDs(groupIDs);
        assertEquals(groupIDs, PersistantDataManager.getGroupIDs());
    }

    @Test
    public void testGetGroupID() {
        assertNotNull("getGroupIDs should not return null", PersistantDataManager.getGroupIDs());
    }

    @Test
    public void testGetNotificationCache() {
        assertNotNull("getNotification should not return null", PersistantDataManager.getNotificationCache());
    }

    @Test
    public void testSetNotificationCache() {
        ArrayList<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification());
        PersistantDataManager.setNotificationCache(notifications);
        assertSame("notification should be updated", notifications.get(0), PersistantDataManager.getNotificationCache().get(0));
    }

    @Test
    public void testAddNotification() {
        int previousLength = PersistantDataManager.getNotificationCache().size();
        Notification notification = new Notification();
        PersistantDataManager.addNotification(notification);
        assertSame("notificationCache should be updated", previousLength+1, PersistantDataManager.getNotificationCache().size());
        assertSame("new notification should be placed at front", notification, PersistantDataManager.getNotificationCache().get(0));
    }


}
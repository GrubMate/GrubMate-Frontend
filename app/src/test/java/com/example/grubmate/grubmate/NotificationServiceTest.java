package com.example.grubmate.grubmate;

import android.content.Intent;
import android.os.IBinder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

/**
 * Created by tianhangliu on 10/28/17.
 */
public class NotificationServiceTest {
    NotificationService notificationService;
    NotificationService.NotificationTask notificationTask;
    @Before
    public void setUp() throws Exception {
        notificationService = new NotificationService();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testOnBind() throws Exception {
        Intent intent = new Intent();
         IBinder binder = notificationService.onBind(intent);
         assertNotNull("Should not be null", binder);
         assertThat(binder, instanceOf(IBinder.class));
    }

    @Test
    public void testIsResponseValid() {
        assertSame("Reponse should not be null",false, notificationService.isResponseValid(null));
        assertSame("Reponse should not be empty",false, notificationService.isResponseValid(""));
        assertSame("Reponse should not contain error",false, notificationService.isResponseValid("error"));
        assertSame("Regular response should be true", true, notificationService.isResponseValid("{}"));
    }


}
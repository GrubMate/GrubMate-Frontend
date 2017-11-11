package com.example.grubmate.grubmate.utilities;

import android.widget.Spinner;

/**
 * Created by tianhangliu on 11/10/17.
 */

public class SpinnerUtilities {
    //private method of your class
    public static int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }
}

package com.healthyscan;

import android.content.Context;
import android.content.SharedPreferences;

public class preferenceManager {

    Context context;
    String saveName;
    SharedPreferences sharedPreferences;

    public preferenceManager(Context context, String saveName) {
        this.context = context;
        this.saveName = saveName;
        sharedPreferences = context.getSharedPreferences(saveName, Context.MODE_PRIVATE);
    }

    public void setFirstTimeLaunch(int isfirstTime) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("key", isfirstTime);
        editor.apply();
    }

    public int checkFirstTimeLaunch() {
        return sharedPreferences.getInt("key", 0);
    }

}

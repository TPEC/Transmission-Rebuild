package com.dc.transimissionr.gameData;

import android.content.res.Resources;

/**
 * Created by Tony on 2017/4/26.
 */

public class GDB {
    private Resources res;

    public void load(Resources res){
        this.res=res;
    }

    private static final GDB ourInstance = new GDB();

    public static GDB getInstance() {
        return ourInstance;
    }

    private GDB() {}
}

package com.dc.transimissionr.gameData;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;

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

    public Bitmap decodeResource(int id){
        return decodeResource(res,id);
    }

    public Bitmap decodeResource(Resources resources, int id){
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, opts);
    }
}

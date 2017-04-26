package com.dc.transimissionr.scene;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.dc.transimissionr.MainActivity;
import com.dc.transimissionr.R;
import com.dc.transimissionr.TWidget.TButton;
import com.dc.transimissionr.gameData.GDB;

/**
 * Created by XIeQian on 2016/12/23.
 */

public class SceneMain {
    private GDB gdb;
    private TButton btnMask;
    private Paint paint;
    private Bitmap bmpBackGroud;

    public SceneMain(){
        gdb=GDB.getInstance();
        bmpBackGroud=gdb.decodeResource(R.drawable.background);
        btnMask=new TButton(0,0,1280,720);
        paint=new Paint();

    }

    public void logic(){

    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(bmpBackGroud,0,0,paint);
    }

    public boolean onTouchEvent(MotionEvent event){
        if(btnMask.onTouchEvent(event)) {
            MainActivity.ma.changeSV(0);
        }
        return true;
    }
}

package com.dc.transimissionr.scene;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.dc.transimissionr.MainActivity;
import com.dc.transimissionr.R;
import com.dc.transimissionr.TSurfaceView;
import com.dc.transimissionr.TWidget.TButton;
import com.dc.transimissionr.gameData.GDB;
import com.dc.transimissionr.glCore.TexFactory;

/**
 * Created by XIeQian on 2016/12/23.
 */

public class SceneMenu {
    private GDB gdb;
    private TButton btnSettings;
    private TButton btnLevel;
    private Paint paint;
    private Bitmap bmpBackGroud;

    public SceneMenu(){
        gdb=GDB.getInstance();
        bmpBackGroud=gdb.decodeResource(R.drawable.background);
        btnLevel=new TButton(320,180,640,120);
        btnSettings=new TButton(320,420,640,120);
        btnLevel.setBmp(gdb.res,R.drawable.level,0);
        btnSettings.setBmp(gdb.res,R.drawable.settings,0);

        paint=new Paint();

    }

    public void logic(){

    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(bmpBackGroud,0,0,paint);
        btnLevel.draw(canvas);
        btnSettings.draw(canvas);
    }

    public boolean onTouchEvent(MotionEvent event){
        if(btnLevel.onTouchEvent(event)) {
            TSurfaceView.tsv.setScene(TSurfaceView.SceneStateEnum.sseLevel);
        }else if(btnSettings.onTouchEvent(event)){
            TSurfaceView.tsv.setScene(TSurfaceView.SceneStateEnum.sseSettings);
        }
        return true;
    }
}

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

/**
 * Created by XIeQian on 2016/12/23.
 */

public class SceneLevel {
    private GDB gdb;
    private TButton[] btnLevel;
    private TButton btnBack;
    private Paint paint;
    private Bitmap bmpBackGroud;

    public SceneLevel(){
        gdb=GDB.getInstance();
        bmpBackGroud=gdb.decodeResource(R.drawable.background);

        btnLevel=new TButton[1];
        for(int i=0;i<btnLevel.length;i++) {
            btnLevel[i] = new TButton(320,180,640,120);
            btnLevel[i].setBmp(gdb.res,R.drawable.level01,0);
        }
        btnBack=new TButton(320,420,640,120);
        btnBack.setBmp(gdb.res,R.drawable.back,0);

        paint=new Paint();

    }

    public void logic(){

    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(bmpBackGroud,0,0,paint);
        btnBack.draw(canvas);
        for(TButton b:btnLevel)
            b.draw(canvas);
    }

    public boolean onTouchEvent(MotionEvent event){
        if(btnBack.onTouchEvent(event)) {
            TSurfaceView.tsv.setScene(TSurfaceView.SceneStateEnum.sseMenu);
        }
        for(TButton b:btnLevel){
            if(b.onTouchEvent(event)){
                MainActivity.ma.changeSV(0);
            }
        }
        return true;
    }
}

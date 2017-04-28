package com.dc.transimissionr.scene;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.view.MotionEvent;

import com.dc.transimissionr.MainActivity;
import com.dc.transimissionr.R;
import com.dc.transimissionr.TSurfaceView;
import com.dc.transimissionr.TWidget.TButton;
import com.dc.transimissionr.TWidget.TLabel;
import com.dc.transimissionr.gameData.Constant;
import com.dc.transimissionr.gameData.GDB;

/**
 * Created by XIeQian on 2016/12/23.
 */

public class SceneSettings {
    private GDB gdb;
    private TButton btnSensitive;
    private TButton btnBack;
    private TButton btnSound;
    private Paint paint;
    private Bitmap bmpBackGroud;

    private TLabel lbSen;
    private TLabel lbVol;


    public SceneSettings(){
        gdb=GDB.getInstance();
        bmpBackGroud=gdb.decodeResource(R.drawable.background);
        btnSensitive=new TButton(320,120,640,120);
        btnSensitive.setBmp(gdb.res,R.drawable.sensitive,0);
        lbSen=new TLabel(1000,140,240,120);
        lbSen.setTextColor(Color.WHITE);
        lbSen.setTextSize(60);
        lbSen.setText(String.valueOf(Constant.CAM_SCALE_RATE));
        btnSound=new TButton(320,300,640,120);
        btnSound.setBmp(gdb.res,R.drawable.sound,0);
        lbVol=new TLabel(1000,320,240,120);
        lbVol.setTextColor(Color.WHITE);
        lbVol.setTextSize(60);
        lbVol.setText(String.valueOf(Constant.Volume));
        btnBack=new TButton(320,480,640,120);
        btnBack.setBmp(gdb.res,R.drawable.back,0);

        paint=new Paint();

    }

    public void logic(){

    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(bmpBackGroud,0,0,paint);
        btnSensitive.draw(canvas);
        btnSound.draw(canvas);
        btnBack.draw(canvas);
        lbSen.drawLabel(canvas);
        lbVol.drawLabel(canvas);
    }

    public boolean onTouchEvent(MotionEvent event){
        if(btnSensitive.onTouchEvent(event)) {
            Constant.CAM_SCALE_RATE++;
            if(Constant.CAM_SCALE_RATE>3)
                Constant.CAM_SCALE_RATE=1;
            Constant.CAM_SCALE=(float)Constant.CAM_SCALE_RATE * 0.003f;
            lbSen.setText(String.valueOf(Constant.CAM_SCALE_RATE));
        }else if(btnSound.onTouchEvent(event)){
            Constant.Volume++;
            if(Constant.Volume>3)
                Constant.Volume=0;
            MainActivity.ma.am.setStreamVolume(AudioManager.STREAM_MUSIC,MainActivity.ma.vol[Constant.Volume],AudioManager.FLAG_PLAY_SOUND);
            lbVol.setText(String.valueOf(Constant.Volume));
        } else if(btnBack.onTouchEvent(event)) {
            TSurfaceView.tsv.setScene(TSurfaceView.SceneStateEnum.sseMenu);
        }
        return true;
    }
}

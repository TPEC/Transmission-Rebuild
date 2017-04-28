package com.dc.transimissionr;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.dc.transimissionr.TWidget.TLabel;
import com.dc.transimissionr.gameData.GDB;
import com.dc.transimissionr.scene.SceneLevel;
import com.dc.transimissionr.scene.SceneMain;
import com.dc.transimissionr.scene.SceneMenu;
import com.dc.transimissionr.scene.SceneSettings;
import com.dc.transimissionr.scene.SceneVictory;

/**
 * Created by XIeQian on 2016/12/23.
 */

public class TSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable {
    public static TSurfaceView tsv;

    public boolean pause=false;

    public enum SceneStateEnum{sseMain,sseVictory,sseMenu,sseSettings,sseLevel}
    private SceneStateEnum sse;
    private SurfaceHolder sh;
    private Thread th;
    private boolean flag;
    private Canvas canvas;
    private Paint paint,paintBlack;
    private int FPS;
    private boolean scaleToWidth;
    private float scaleRate,scaleTrans;
    private TLabel tlTest;

    private SceneMain sceneMain;
    private SceneVictory sceneVictory;
    private SceneMenu sceneMenu;
    private SceneLevel sceneLevel;
    private SceneSettings sceneSettings;

    private GDB gdb;

    public TSurfaceView(Context context) {
        super(context);
        TSurfaceView.tsv=this;
        sh=this.getHolder();
        sh.addCallback(this);
        scaleRate=1;
        scaleTrans=0;
        scaleToWidth=true;
        paintBlack=new Paint();
        paint=new Paint();
        paint.setTextSize(32);
        paint.setColor(Color.WHITE);

        tlTest=new TLabel(0,0,640,72);
        tlTest.setTextSize(32);
        tlTest.setTextColor(Color.WHITE);

        gdb=GDB.getInstance();
        gdb.load(context.getResources());
        sceneMain=new SceneMain();
        sceneVictory=new SceneVictory();
        sceneMenu=new SceneMenu();
        sceneLevel=new SceneLevel();
        sceneSettings=new SceneSettings();

        sse=SceneStateEnum.sseMain;
    }

    public void setScene(SceneStateEnum sse){
        this.sse=sse;
    }

    private void drawSV(){
        try{
            canvas=sh.lockCanvas();
            if(canvas!=null){
                canvas.save();
                canvas.scale(scaleRate,scaleRate);
                if(scaleToWidth)
                    canvas.translate(0,scaleTrans);
                else
                    canvas.translate(scaleTrans,0);

                //canvas.drawColor(Color.BLACK);
                switch (sse) {
                    case sseMain:
                        sceneMain.draw(canvas);
                        break;
                    case sseVictory:
                        sceneVictory.draw(canvas);
                        break;
                    case sseMenu:
                        sceneMenu.draw(canvas);
                        break;
                    case sseSettings:
                        sceneSettings.draw(canvas);
                        break;
                    case sseLevel:
                        sceneLevel.draw(canvas);
                        break;
                }


//                tlTest.setText("FPS:"+String.valueOf(FPS)+"  "+String.valueOf(scaleToWidth));
//                tlTest.drawLabel(canvas);

                if(scaleToWidth) {
                    canvas.drawRect(0,-scaleTrans,1280,0,paintBlack);
                    canvas.drawRect(0,720,1280,720+scaleTrans,paintBlack);
                }else{
                    canvas.drawRect(scaleTrans,0,0,720,paintBlack);
                    canvas.drawRect(1280,0,1280+scaleTrans,720,paintBlack);
                }
                canvas.restore();
            }
        } catch (Exception e){

        } finally {
            if(canvas!=null)
                sh.unlockCanvasAndPost(canvas);
        }
    }

    private void logicSV(){
        switch (sse) {
            case sseMain:
                sceneMain.logic();
                break;
            case sseVictory:
                sceneVictory.logic();
                break;
            case sseMenu:
                sceneMenu.logic();
                break;
            case sseSettings:
                sceneSettings.logic();
                break;
            case sseLevel:
                sceneLevel.logic();
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (scaleToWidth) {
            event.setLocation(event.getX() / scaleRate, event.getY() / scaleRate - scaleTrans);
            if(event.getY()<0||event.getY()>=720)
                return false;
        }else {
            event.setLocation(event.getX() / scaleRate - scaleTrans, event.getY() / scaleRate);
            if(event.getX()<0||event.getX()>=1280)
                return false;
        }
        switch (sse) {
            case sseMain:
                return sceneMain.onTouchEvent(event);
            case sseVictory:
                return sceneVictory.onTouchEvent(event);
            case sseMenu:
                return sceneMenu.onTouchEvent(event);
            case sseSettings:
                return sceneSettings.onTouchEvent(event);
            case sseLevel:
                return sceneLevel.onTouchEvent(event);
        }
        return false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        flag=true;
        th=new Thread(this);
        th.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        float srw,srh;
        srw=(float)width/1280;
        srh=(float)height/720;
        if(srw>=srh){
            scaleRate=srh;
            scaleToWidth=false;
            scaleTrans=(width/srh-1280)/2;
        }else{
            scaleRate=srw;
            scaleToWidth=true;
            scaleTrans=(height/srw-720)/2;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        flag=false;
    }

    @Override
    public void run() {
        while(flag){
            if(pause){
                try {
                    th.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                try {
                    long tinterval = System.currentTimeMillis();
                    logicSV();
                    drawSV();
                    tinterval = System.currentTimeMillis() - tinterval;
                    if (tinterval < 17) {
                        FPS = 60;
                        th.sleep(17 - tinterval);
                    } else {
                        FPS = (int) (1000 / tinterval);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
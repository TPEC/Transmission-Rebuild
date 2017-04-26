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

/**
 * Created by XIeQian on 2016/12/23.
 */

public class TSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable {
    public enum SceneStateEnum{sseMain}
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

    private GDB gdb;

    public TSurfaceView(Context context) {
        super(context);
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
        sse=SceneStateEnum.sseMain;
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

                sceneMain.draw(canvas);

                tlTest.setText("FPS:"+String.valueOf(FPS)+"  "+String.valueOf(scaleToWidth));
                tlTest.drawLabel(canvas);

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
            try{
                long tinterval=System.currentTimeMillis();
                logicSV();
                drawSV();
                tinterval=System.currentTimeMillis()-tinterval;
                if(tinterval<17){
                    FPS=60;
                    th.sleep(17-tinterval);
                }
                else{
                    FPS=(int)(1000/tinterval);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
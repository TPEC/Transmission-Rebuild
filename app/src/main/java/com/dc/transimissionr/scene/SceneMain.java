package com.dc.transimissionr.scene;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.dc.transimissionr.TWidget.TButton;
import com.dc.transimissionr.gameData.GDB;

/**
 * Created by XIeQian on 2016/12/23.
 */

public class SceneMain {
    private int frameMap;
    private GDB gdb;
    private TButton btnMask;
    private Paint paint;

    public SceneMain(){
        frameMap=0;
        btnMask=new TButton(320,0,960,720);
        paint=new Paint();
        paint.setColor(Color.GRAY);
        gdb=GDB.getInstance();
    }

    public void logic(){
        frameMap=(frameMap+1)%32;
        gdb.getPlayer().logic();
    }

    public void draw(Canvas canvas){

    }

    public boolean onTouchEvent(MotionEvent event){
        if(btnMask.onTouchEvent(event)){
            int px,py;
            px= (int) (event.getX()+gdb.getPlayer().getPos().x-800);
            py= (int) (event.getY()+gdb.getPlayer().getPos().y-360);
            if(px/96>=0 && px/96<gdb.getMap().getWidth() && py/96>=0 && py/96<gdb.getMap().getHeight() && gdb.getMap().getMee(px/96,py/96)/10==1){
                int tx,ty;
                tx=px%96-48;
                ty=py%96-48;
                if(Math.abs(tx)>=5 || Math.abs(ty)>=5){
                    if(tx>ty){
                        if(tx>-ty)
                            px+=96;
                        else
                            py-=96;
                    }else {
                        if(tx>-ty)
                            py+=96;
                        else
                            px-=96;
                    }
                }
            }
            px/=96;py/=96;
            if(gdb.getPlayer().setPath(px,py)){
                gdb.getPlayer().setPse(Player.PlayerStateEnum.pseRun);
            }
        }
        return true;
    }
}

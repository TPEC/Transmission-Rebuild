package com.dc.transimissionr.TWidget;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.MotionEvent;

/**
 * Created by XieQian on 2016/11/27.
 */

public class TCrossButton {
    private Rect rectDst,rectSrc;
    private Rect[] rectDstS;
    private Bitmap bmpBtnBG;
    private Bitmap bmpBtnSF;
    private Paint paint,paintGray;
    private boolean enabled;
    private boolean clickdown;
    private int clickhead;

    public TCrossButton(int px, int py, int pw, int ph) {
        rectDst=new Rect(px,py,px+pw,py+ph);
        rectSrc=new Rect(0,0,pw,ph);
        rectDstS=new Rect[4];
        rectDstS[0]=new Rect(px+pw/4,py+ph/2,px+pw*3/4,py+ph);
        rectDstS[1]=new Rect(px,py+ph/4,px+pw/2,py+ph*3/4);
        rectDstS[2]=new Rect(px+pw/4,py,px+pw*3/4,py+ph/2);
        rectDstS[3]=new Rect(px+pw/2,py+ph/4,px+pw,py+ph*3/4);
        paint=new Paint();
        paintGray=new Paint();
        ColorMatrix cm=new ColorMatrix();
        cm.setSaturation(0);
        paintGray.setColorFilter(new ColorMatrixColorFilter(cm));
        bmpBtnBG=null;
        bmpBtnSF=null;
        clickdown=false;
        enabled=true;
        clickhead=-1;
    }

    public void setBmp(Resources res, int bmpBGid, int bmpSFid){
        if(bmpBGid!=0)
            bmpBtnBG = decodeResource(res, bmpBGid);
        else
            bmpBtnBG=null;
        if(bmpSFid!=0)
            bmpBtnSF = decodeResource(res, bmpSFid);
        else
            bmpBtnSF=null;
    }

    public void setEnabled(boolean blnEnabled){
        if(enabled!=blnEnabled) {
            enabled = blnEnabled;
            clickdown = false;
        }
    }

    public boolean isEnabled(){
        return enabled;
    }

    public boolean onTouchEvent(MotionEvent event){
        if(enabled) {
            int px=(int)event.getX()-rectDst.centerX();
            int py=(int)event.getY()-rectDst.centerY();
            if (px*px+py*py<=rectDst.width()*rectDst.height()/4) {
                if(event.getAction()==MotionEvent.ACTION_UP) {
                    clickdown = false;
                    clickhead=-1;
                }else if(event.getAction()==MotionEvent.ACTION_DOWN) {
                    clickdown = true;
                }
                if(clickdown){
                    if(px<py){
                        if(px>-py)
                            clickhead=0;
                        else
                            clickhead=1;
                    }else{
                        if(px<-py)
                            clickhead=2;
                        else
                            clickhead=3;
                    }
                }
                return clickdown;
            }else{
                if(clickdown && event.getAction()==MotionEvent.ACTION_UP) {
                    clickdown = false;
                    clickhead=-1;
                }
                return false;
            }
        }
        return false;
    }

    public void draw(Canvas canvas) {
        Rect rectSrc_ = new Rect(rectSrc);
        if (enabled) {
            if (bmpBtnBG != null)
                canvas.drawBitmap(bmpBtnBG, rectSrc, rectDst, paint);
            if (bmpBtnSF != null) {
                for(int i=0;i<4;i++) {
                    if(clickdown && clickhead==i)
                        rectSrc_.offsetTo(i*rectSrc_.width(),rectSrc_.height());
                    else
                        rectSrc_.offsetTo(i*rectSrc_.width(),0);
                    canvas.drawBitmap(bmpBtnSF, rectSrc_, rectDstS[i], paint);
                }
            }
        } else {
            if (bmpBtnBG != null)
                canvas.drawBitmap(bmpBtnBG, rectSrc, rectDst, paintGray);
            if (bmpBtnSF != null) {
                for(int i=0;i<4;i++) {
                    rectSrc_.offsetTo(i*rectSrc_.width(),0);
                    canvas.drawBitmap(bmpBtnSF, rectSrc_, rectDstS[i], paintGray);
                }
            }
        }
    }

    public int getClickhead(){
        return clickhead;
    }

    private Bitmap decodeResource(Resources resources, int id){
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, opts);
    }
}

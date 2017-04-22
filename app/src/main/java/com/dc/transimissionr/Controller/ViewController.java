package com.dc.transimissionr.Controller;

import android.graphics.RectF;
import android.view.MotionEvent;

import com.dc.transimissionr.gameData.Constant;

/**
 * Created by Irene on 2017/4/9.
 */

public class ViewController{
    private int viewID=-1;
    private float lastView[] = new float[2];
    private float thisView[] = new float[2];
    private float[] viewVector = new float[2];
    private int viewCounter=0;

    private RectF rectDst;
    private RectF[] rectExc;

    public ViewController(RectF rectDst, RectF[] rectExc){
        this.rectDst=rectDst;
        this.rectExc=rectExc;
    }

    public float[] getMoveVector(){
        return viewVector;
    }

    public boolean getClickDown(){
        return (viewID!=-1);
    }

    public boolean onTouchEvent (MotionEvent event){
        int pointerCount = event.getPointerCount();
        int action = event.getActionMasked();
        float x = 0, y = 0;
        if (action== MotionEvent.ACTION_DOWN || action== MotionEvent.ACTION_POINTER_DOWN) {
            if (viewID < 0) {
                for (int i = 0; i < pointerCount; i++) {
                    x = event.getX(i)* Constant.ratio_width;
                    y = event.getY(i)* Constant.ratio_height;
                    if(rectDst.contains(x,y)){
                        boolean flag=true;
                        for(RectF rec:rectExc){
                            if(rec.contains(x,y)){
                                flag=false;
                                break;
                            }
                        }
                        if (flag) {
                            viewID = event.getPointerId(i);
                            thisView[0]=0;
                            thisView[1]=0;
                            lastView[0]=0;
                            lastView[1]=0;
                            viewVector[0]=0;
                            viewVector[1]=0;
                            break;
                        }
                    }
                }
            }
        }else if (action== MotionEvent.ACTION_UP || action== MotionEvent.ACTION_POINTER_UP) {
            if (event.getPointerId(event.getActionIndex()) == viewID) {
                viewID = -1;
                viewCounter=0;
                return true;
            }
        }
        if (viewID>=0){
            viewCounter=viewCounter+1;
            for (int i = 0; i < pointerCount; i++) {
                if (event.getPointerId(i)==viewID) {
                    lastView[0]=thisView[0];
                    lastView[1]=thisView[1];
                    thisView[0] = event.getX(i)* Constant.ratio_width;
                    thisView[1] = event.getY(i)* Constant.ratio_height;
                    break;
                }
            }
            if (viewCounter==1){
                viewVector[0]=0;
                viewVector[1]=0;
            }else {
                viewVector[0] = thisView[0] - lastView[0];
                viewVector[1] = thisView[1] - lastView[1];
                return true;
            }
        }
        return false;
    }
}

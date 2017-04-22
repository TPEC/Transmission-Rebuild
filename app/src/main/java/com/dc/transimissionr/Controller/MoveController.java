package com.dc.transimissionr.Controller;

import android.graphics.RectF;
import android.view.MotionEvent;

import com.dc.transimissionr.R;
import com.dc.transimissionr.gameData.Constant;
import com.dc.transimissionr.glCore.MatrixState;
import com.dc.transimissionr.glCore.TexFactory;
import com.dc.transimissionr.glCore.VecFactory;
import com.dc.transimissionr.glObjects.TextureRect;


/**
 * Created by Irene on 2017/4/8.
 */

public class MoveController {
    private float[] moveVector = new float[2];
    private int moveID=-1;

    private RectF rectDst;
    private float rectDstMX;
    private float rectDstMY;

    private TextureRect textureRect;
    private int texId;

    public MoveController(RectF rectDst){
        this.rectDst=rectDst;
        rectDstMX=rectDst.centerX();
        rectDstMY=rectDst.centerY();
    }

    public void draw() {
        MatrixState.pushMatrix();
        MatrixState.translate(rectDstMX-640,360-rectDstMY,0);
        MatrixState.scale(rectDst.width(),rectDst.height(),1f);
        textureRect.drawSelf(texId);
        MatrixState.popMatrix();
    }

    public void initTexture(){
        texId=TexFactory.getNewTexture(R.drawable.floor1,-1);
        textureRect=new TextureRect(TexFactory.glSv,1f,1f);
    }

    public float[] getMoveVector(){
        return moveVector;
    }

    public boolean getClickDown(){
        return (moveID!=-1);
    }

    public boolean onTouchEvent(MotionEvent event){
        int pointerCount = event.getPointerCount();
        int action = event.getActionMasked();
        float x = 0, y = 0;
        if (action== MotionEvent.ACTION_DOWN || action== MotionEvent.ACTION_POINTER_DOWN) {
            if (moveID < 0) {
                for (int i = 0; i < pointerCount; i++) {
                    x = event.getX(i)* Constant.ratio_width;
                    y = event.getY(i)* Constant.ratio_height;
                    if (rectDst.contains(x,y)) {
                        moveID = event.getPointerId(i);
                        break;
                    }
                }
            }
        }else if (action== MotionEvent.ACTION_UP || action== MotionEvent.ACTION_POINTER_UP) {
            if (event.getPointerId(event.getActionIndex()) == moveID) {
                moveID = -1;
                return true;
            }
        }
        if (moveID>=0){
            for (int i = 0; i < pointerCount; i++) {
                if (event.getPointerId(i)==moveID) {
                    x = event.getX(i)* Constant.ratio_width;
                    y = event.getY(i)* Constant.ratio_height;
                    break;
                }
            }
            moveVector[0] = x - rectDstMX;
            moveVector[1] = y - rectDstMY;
            VecFactory.unitize2(moveVector);
            return true;
        }
        return false;
    }
}

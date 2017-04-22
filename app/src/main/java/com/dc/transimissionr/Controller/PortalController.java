package com.dc.transimissionr.Controller;

import android.graphics.RectF;
import android.view.MotionEvent;

import com.dc.transimissionr.R;
import com.dc.transimissionr.glCore.MatrixState;
import com.dc.transimissionr.glCore.TexFactory;
import com.dc.transimissionr.glObjects.TextureRect;

import static com.dc.transimissionr.gameData.Constant.ratio_height;
import static com.dc.transimissionr.gameData.Constant.ratio_width;

/**
 * Created by Irene on 2017/4/9.
 */

public class PortalController{
    private int portalID=-1;
    private boolean clicked=false;

    private RectF rectDst;

    private TextureRect textureRect;
    private int texId;

    public PortalController(RectF rectDst){
        this.rectDst=rectDst;
    }

    public void initTexture(){
        texId= TexFactory.getNewTexture(R.drawable.floor1,-1);
        textureRect=new TextureRect(TexFactory.glSv,1f,1f);
    }

    public void draw() {
        MatrixState.pushMatrix();
        MatrixState.translate(rectDst.centerX()-640,360-rectDst.centerY(),0);
        MatrixState.scale(rectDst.width(),rectDst.height(),1f);
        textureRect.drawSelf(texId);
        MatrixState.popMatrix();
    }

    public boolean onTouchEvent (MotionEvent event){
        int pointerCount = event.getPointerCount();
        int action = event.getActionMasked();
        float x = 0, y = 0;
        if (action== MotionEvent.ACTION_DOWN || action== MotionEvent.ACTION_POINTER_DOWN) {
            if (portalID < 0) {
                for (int i = 0; i < pointerCount; i++) {
                    x = event.getX(i)*ratio_width;
                    y = event.getY(i)*ratio_height;
                    if (rectDst.contains(x,y)) {
                        portalID = event.getPointerId(i);
                        break;
                    }
                }
            }
        }else if (action== MotionEvent.ACTION_UP || action== MotionEvent.ACTION_POINTER_UP) {
            if (event.getPointerId(event.getActionIndex()) == portalID) {
                clicked=true;
                portalID = -1;
                return true;
            }
        }
        if (portalID>=0){
            for (int i = 0; i < pointerCount; i++) {
                if (event.getPointerId(i)==portalID) {
                    x = event.getX(i)*ratio_width;
                    y = event.getY(i)*ratio_height;
                    break;
                }
            }
            return true;
        }
        return false;
    }

    public boolean getClicked(){
        if(clicked){
            clicked=false;
            return true;
        }
        return false;
    }
}

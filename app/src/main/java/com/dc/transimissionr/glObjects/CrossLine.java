package com.dc.transimissionr.glObjects;

import com.dc.transimissionr.R;
import com.dc.transimissionr.glCore.MatrixState;
import com.dc.transimissionr.glCore.TexFactory;

/**
 * Created by XIeQian on 2017/4/16.
 */

public class CrossLine {
    private int texId;
    private TextureRect textureRect;
    private float width;
    private float height;

    public CrossLine(float width,float height){
        this.width=width;
        this.height=height;
    }

    public void draw(){
        MatrixState.pushMatrix();
        MatrixState.scale(width,height,1f);
        textureRect.drawSelf(texId);
        MatrixState.popMatrix();
        MatrixState.pushMatrix();
        MatrixState.translate(0f,0f,-1f);
        MatrixState.scale(height,width,1f);
        textureRect.drawSelf(texId);
        MatrixState.popMatrix();
    }

    public void initTexture(){
        texId= TexFactory.getNewTexture(R.drawable.floor1,-1);
        textureRect=new TextureRect(TexFactory.glSv,1f,1f);
    }
}

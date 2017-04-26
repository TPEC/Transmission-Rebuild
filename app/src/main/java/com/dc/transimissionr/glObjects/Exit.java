package com.dc.transimissionr.glObjects;

import com.dc.transimissionr.R;
import com.dc.transimissionr.gameData.Constant;
import com.dc.transimissionr.glCore.MatrixState;
import com.dc.transimissionr.glCore.TexFactory;

/**
 * Created by Tony on 2017/4/26.
 */

public class Exit {
    private float[] pos;

    private TextureRect textureRect;
    private int texId;

    public Exit(){
        pos=new float[3];
        textureRect=new TextureRect(TexFactory.glSv,1f,1f);
        texId=TexFactory.getNewTexture(R.drawable.finish,TexFactory.PMOD_REPEAT);
    }

    public void setPos(float[] pos){
        this.pos=pos;
    }

    public boolean getToExit(float[] p){
        if(Math.abs(p[0]-pos[0])<= Constant.UNIT_SIZE/2 && Math.abs(p[2]-pos[2])<=Constant.UNIT_SIZE/2){
            if(p[1]>=pos[1] && p[1]<pos[1]+Constant.UNIT_SIZE){
                return true;
            }
        }
        return false;
    }

    public void draw(){
        MatrixState.pushMatrix();
        MatrixState.translate(pos[0],pos[1],pos[2]);
        MatrixState.scale(1f,1f,1f);
        MatrixState.rotate(-90,1,0,0);
        textureRect.drawSelf(texId);
        MatrixState.popMatrix();
    }
}
